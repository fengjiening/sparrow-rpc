package com.fengjiening.sparrow.pool;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.utill.ThreadPoolUtil;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * <p>
 *  Default Invoke Future
 * </p>
 *
 * @author Jay
 * @date 2022/01/07 19:43
 */
public class DefaultInvokeFuture {

    private RemotingCommand response;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private final InvokeCallback callback;
    private int maxConnections;
    /**
     * 异步回调线程池
     */
    private final ExecutorService asyncExecutor = ThreadPoolUtil.newIoThreadPool("async-callback-");


    public DefaultInvokeFuture(InvokeCallback callback) {
        this.callback = callback;
    }

    public RemotingCommand awaitResponse() throws InterruptedException {
        countDownLatch.await();
        return response;
    }

    public RemotingCommand awaitResponse(long timeout, TimeUnit timeUnit) throws TimeoutException, InterruptedException {
        if(!countDownLatch.await(timeout, timeUnit)){
            throw new TimeoutException("future timeout");
        }
        return response;
    }

    public void putResponse(RemotingCommand response) {
        // response必须在countDown之前赋值
        this.response = response;
        countDownLatch.countDown();
        // callback
        this.executeCallback();
    }

    public void executeCallback() {
        // execute callback methods
        if(callback != null){
            Runnable callbackTask = ()->{
                try{
                    callback.onComplete(response);
                }catch (Exception e){
                    callback.exceptionCaught(e);
                }
            };
            // determine execute callback using executor thread or I/O thread
            if( callback.getExecutor() != null){
                callback.getExecutor().submit(callbackTask);
            } else{
                callbackTask.run();
            }
        }
    }
    class RpcInvokeCallback implements InvokeCallback {
        AsyncCallback callback;
        CompletableFuture<RpcResponse> future;

        RpcInvokeCallback(AsyncCallback callback, CompletableFuture<RpcResponse> future) {
            this.callback = callback;
            this.future = future;
        }
        @Override
        public void onComplete(RemotingCommand remotingCommand) {
            RpcResponse response = processResponse((RemotingCommand) remotingCommand);
            Optional<CompletableFuture<RpcResponse>> optionalFuture = Optional.ofNullable(future);
            Optional<AsyncCallback> optionalCallback = Optional.ofNullable(callback);
            Throwable exception = response.getException();
            if(exception != null){
                optionalCallback.ifPresent(c->c.exceptionCaught(exception));
                optionalFuture.ifPresent(f->f.completeExceptionally(exception));
            }else{
                callback.onResponse(response);
                optionalFuture.ifPresent(f->f.complete(response));
            }
        }

        @Override
        public void exceptionCaught(Throwable throwable) {
            Optional<CompletableFuture<RpcResponse>> optionalFuture = Optional.ofNullable(future);
            Optional<AsyncCallback> optionalCallback = Optional.ofNullable(callback);
            optionalCallback.ifPresent(c->c.exceptionCaught(throwable));
            optionalFuture.ifPresent(f->f.completeExceptionally(throwable));
        }

        @Override
        public void onTimeout(RemotingCommand remotingCommand) {
            Optional<CompletableFuture<RpcResponse>> optionalFuture = Optional.ofNullable(future);
            Optional<AsyncCallback> optionalCallback = Optional.ofNullable(callback);
            TimeoutException e = new TimeoutException("Request timeout, request id: " + remotingCommand.getId());
            optionalCallback.ifPresent(c->c.exceptionCaught(e));
            optionalFuture.ifPresent(f->f.completeExceptionally(e));
        }

        @Override
        public ExecutorService getExecutor() {
            return asyncExecutor;
        }


        /**
         * 处理收到的回复报文
         * 首先判断回复的命令类型，然后检查报文完整性
         * @param responseCommand {@link RpcRemotingCommand}
         * @return {@link RpcResponse}
         */
        private RpcResponse processResponse(RemotingCommand responseCommand){
            //if(responseCommand.getCommandCode().equals(RpcProtocol.RESPONSE)){
            //    // 获得response
            //    byte[] content = responseCommand.getContent();
            //    // 检查CRC32校验码
            //    if(checkCrc32(content, responseCommand.getCrc32())){
            //        // 解压数据部分
            //        byte compressorCode = responseCommand.getCompressor();
            //        if(compressorCode != -1){
            //            Compressor compressor = CompressorManager.getCompressor(compressorCode);
            //            content = compressor.decompress(content);
            //        }
            //        // 反序列化
            //        Serializer serializer = SerializerManager.getSerializer(responseCommand.getSerializer());
            //        return serializer.deserialize(content, RpcResponse.class);
            //    }
            //    else{
            //        // CRC32 错误，报文损坏
            //        throw new RuntimeException("network packet damaged during transport");
            //    }
            //}else if(responseCommand.getCommandCode().equals(RpcProtocol.ERROR)){
            //    // 服务端回复Error
            //    throw new RuntimeException(new String(responseCommand.getContent(), StandardCharsets.UTF_8));
            //}else{
            //    // 请求Timeout
            //    throw new RuntimeException(new String(responseCommand.getContent(), StandardCharsets.UTF_8));
            //}
            return null;
        }
    }
}
