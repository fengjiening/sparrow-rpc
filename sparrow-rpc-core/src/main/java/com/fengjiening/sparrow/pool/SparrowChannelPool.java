package com.fengjiening.sparrow.pool;

import com.fengjiening.sparrow.config.vo.Channel;
import com.fengjiening.sparrow.factory.SparrowChannelFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: ChannelPool
 * @Description: 通道池
 * @Date: 2022/11/1 11:19
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Slf4j
public class SparrowChannelPool {

    /**
     * Connection pool's url
     */
    private final Url url;
    /**
     * connections list
     */
    private final CopyOnWriteArrayList<Connection> connections;

    /**
     * Connection Factory used to create new Connections.
     */
    private final SparrowChannelFactory connectionFactory;

    /**
     * whether this pool is warming up.
     * If this pool is warming up, other threads can't add new Connection into it.
     */
    private final AtomicBoolean warmingUp = new AtomicBoolean(false);

    public SparrowChannelPool(Url url, SparrowChannelFactory connectionFactory){
        this.connectionFactory = connectionFactory;
        this.connections = new CopyOnWriteArrayList<>();
        this.url = url;
    }

    public void add(Connection connection){
        this.connections.add(connection);
    }

    public void remove(Connection connection){
        this.connections.remove(connection);
    }

    /**
     * get a connection using select strategy
     * @param asyncExecutor {@link ExecutorService} executor to heal connection pool if needed
     * @return {@link Connection}
     */
    public Connection getConnection(ExecutorService asyncExecutor) throws ConnectException {
        ensureConnected(asyncExecutor);
        ArrayList<Connection> snapshot = new ArrayList<>(this.connections);
        // select a connection+
        if(snapshot == null || snapshot.isEmpty()){
            return null;
        }
        // only 1 candidate
        if(snapshot.size() == 1){
            return snapshot.get(0);
        }
        // random select
        Random random = new Random(System.nanoTime());
        return snapshot.get(random.nextInt(snapshot.size()));
    }

    /**
     * make sure the connection pool is all connected
     * This method will be called before getConnection then remove all closed connections.
     * @param asyncExecutor {@link ExecutorService} executor to heal connection pool
     */
    private void ensureConnected(ExecutorService asyncExecutor) throws ConnectException{
        // remove close connections
        connections.removeIf(Connection::isClosed);
        if(!warmingUp.get() && connections.size() < url.getExpectedConnectionCount()){
            // heal connection pool
            this.healConnectionPool(asyncExecutor, url.getExpectedConnectionCount(), 1500, 1);
        }
    }


    /**
     * heal a connection pool using an executor
     * @param executor {@link ExecutorService}
     * @param timeout connect timeout
     * @param expectedCount expected connection count
     * @param syncCount  sync create count
     */
    public void healConnectionPool(ExecutorService executor, int expectedCount, int timeout, int syncCount) throws ConnectException, IllegalArgumentException {
        markAsyncWarmUpStart();
        for(int i = 0; i < syncCount; i++){
            Connection connection = connectionFactory.create(url, timeout);
            connections.add(connection);
        }
        Runnable warmUpTask = ()->{
            long startTime = System.currentTimeMillis();
            for (int cur = connections.size(); cur < expectedCount; cur++){
                try{
                    // create a connection
                    Connection connection = connectionFactory.create(url, timeout);
                    // add to pool
                    connections.add(connection);
                }catch (Exception e){
                    log.error("connection warm up failed", e);
                    break;
                }
            }
            log.info("connection pool warm up for {} finished, poolSize: {}, expected: {} time used: {}ms", url, connections.size(), expectedCount, (System.currentTimeMillis() - startTime));
            // finish warming up
            markAsyncWarmUpDone();
        };

        // submit task to executor
        executor.submit(warmUpTask);
    }

    /**
     * mark Async warm up start.
     * This method uses CAS of AtomicBoolean, it only tries once.
     */
    private void markAsyncWarmUpStart(){
        if(warmingUp.compareAndSet(false, true)){
            return;
        }
        throw new IllegalStateException("warm up already started");
    }

    /**
     * mark Async warm up done.
     * This method uses CAS of AtomicBoolean, it only tries once.
     */
    private void markAsyncWarmUpDone(){
        if(warmingUp.compareAndSet(true, false)){
            return;
        }
        throw new IllegalStateException("warm up not started yet");
    }

    public boolean isAsyncWarmUpDone(){
        return warmingUp.get();
    }

    public int size(){
        return connections.size();
    }
}
