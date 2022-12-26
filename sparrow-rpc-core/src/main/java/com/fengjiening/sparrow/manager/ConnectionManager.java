package com.fengjiening.sparrow.manager;

import com.fengjiening.sparrow.utill.NamedThreadFactory;
import com.fengjiening.sparrow.factory.SparrowChannelFactory;
import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.pool.SparrowChannelPool;
import com.fengjiening.sparrow.pool.Url;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.util.concurrent.*;

/**
 * <p>
 *  Connection Manager.
 *  Manages connection pool of each address.
 * </p>
 *
 * @author Jay
 * @date 2022/01/08 15:43
 */
@Slf4j
public class ConnectionManager {

    private final ConcurrentHashMap<String, FutureTask<SparrowChannelPool>> connPoolTasks = new ConcurrentHashMap<>(256);

    private final ConcurrentHashMap<String, FutureTask<Integer>> healTasks = new ConcurrentHashMap<>(256);
    /**
     * connection factory, produces connections of the same protocol
     */
    private SparrowChannelFactory connectionFactory;


    /**
     * async connect executor.
     * Background threads used to create connections.
     */
    private final ExecutorService asyncConnectExecutor = new ThreadPoolExecutor(2, 2,
            0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            new NamedThreadFactory("async-connect-thread", true));


    public ConnectionManager(){

    }

    public ConnectionManager(SparrowChannelFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        // init connection factory
        this.connectionFactory.init();
        // bind manager with factory
        this.connectionFactory.setConnectionManager(this);
    }

    public void add(Connection connection)  {
        try{
            SparrowChannelPool pool = this.getConnectionPoolAndCreateIfAbsent(connection.getUrl());
            if(pool != null){
                pool.add(connection);
            }
        }catch (ConnectException e){
            throw new RuntimeException("Create connect pool failed", e);
        }
    }

    /**
     * create a connection
     * @param url url {@link Url}
     * @return {@link Connection}
     * @throws Exception exceptions
     */
    @Deprecated
    public Connection createConnection(Url url) throws Exception {
        return this.connectionFactory.create(url,15000);
    }

    /**
     * Close connection pool or connect tasks
     * @param poolKey connection pool key
     */
    public void removeSparrowChannelPool(String poolKey) {
        FutureTask<SparrowChannelPool> task = connPoolTasks.remove(poolKey);
        task.cancel(true);
        FutureTask<Integer> healTask = healTasks.remove(poolKey);
        healTask.cancel(true);
    }
    /**
     * Close connection pool or connect tasks
     * @param poolKey connection pool key
     */
    public void removeConnectionPool(String poolKey) {
        FutureTask<SparrowChannelPool> task = connPoolTasks.remove(poolKey);
        task.cancel(true);
        FutureTask<Integer> healTask = healTasks.remove(poolKey);
        healTask.cancel(true);
    }
    /**
     * get a connection from managed connection pool
     * @param url {@link Url}
     * @return {@link Connection}
     */
    public Connection getConnection(Url url) throws ConnectException {
        // get connection pool creation task
        FutureTask<SparrowChannelPool> future = connPoolTasks.get(url.getPoolKey());
        // get pool from task
        try{
            if(!future.isDone()){
                throw new IllegalStateException("task is not done yet");
            }
            SparrowChannelPool connectionPool = future.get();
            return connectionPool != null ? connectionPool.getConnection(asyncConnectExecutor) : null;
        }catch (Exception e){
            throw new ConnectException("Connect error");
        }

    }

    /**
     * get a connection.
     * If connection pool absent, create a new connection pool
     * @param url {@link Url}
     * @return {@link Connection}
     */
    public Connection getConnectionAndCreateIfAbsent(Url url) throws ConnectException {
        SparrowChannelPool pool = getConnectionPoolAndCreateIfAbsent(url);
        return pool != null ? pool.getConnection(asyncConnectExecutor) : null;
    }

    /**
     * get SparrowChannelPool and create if absent
     * @param url {@link Url} target url
     */
    public SparrowChannelPool getConnectionPoolAndCreateIfAbsent(Url url) throws ConnectException {
        if(connPoolTasks.get(url.getPoolKey()) == null){
            // no cached conn pool task
            FutureTask<SparrowChannelPool> task = new FutureTask<>(new CreatePoolCallable(url, 1));
            // try to put new task into cache
            if(connPoolTasks.putIfAbsent(url.getPoolKey(), task) == null){
                // successfully put a task into cache, run task
                task.run();
            }
        }
        // get cached task
        FutureTask<SparrowChannelPool> connTask = connPoolTasks.get(url.getPoolKey());
        try{

            if(!connTask.isDone()){
                throw new IllegalStateException("task is not done yet");
            }
            return connTask.get();
        }catch (Exception e){
            connPoolTasks.remove(url.getPoolKey());
            throw new ConnectException("connect error");
        }
    }

    /**
     * this method is mainly used to heal connection pool.
     * But it will also create the pool if absent.
     * @param url {@link Url}
     * @throws Exception exceptions from healing task
     */
    public void createConnectionPoolAndHealIfNeeded(Url url) throws Exception {
        // get or create a pool
        SparrowChannelPool pool = getConnectionPoolAndCreateIfAbsent(url);
        if (pool != null) {
            // heal pool
            this.healConnectionPool(pool, url);
        }else{
            log.warn("heal task failed, pool hasn't been created yet, url:{}", url);
        }
    }

    /**
     * heal connection pool async
     * @param url {@link Url}
     */
    public void healConnectionPool(SparrowChannelPool pool, Url url) throws Exception {
        int originalPoolSize = pool.size();
        String poolKey = url.getPoolKey();
        // check if there's an async warm up running & if the pool size is enough
        if(pool.isAsyncWarmUpDone() && originalPoolSize < url.getExpectedConnectionCount()){
            FutureTask<Integer> task = healTasks.get(poolKey);
            // check if there's a running heal task
            if(task == null){
                // create a new heal task
                task = new FutureTask<>(new HealPoolCallable(pool, url.getExpectedConnectionCount()));
                if(healTasks.putIfAbsent(poolKey, task) == null){
                    // successfully put into task cache, run heal task
                    task.run();
                }
            }
            try{
                // get healing result
                int sizeAfterHeal = task.get();
                log.info("connection pool {} healed, expected: {}, current size: {}, increment: {}", poolKey, url.getExpectedConnectionCount(), sizeAfterHeal, (sizeAfterHeal - originalPoolSize));
            } catch (ExecutionException | InterruptedException e) {
                log.error("heal connection pool failed, poolKey:  {}",poolKey, e);
                throw e;
            }
            healTasks.remove(poolKey);
        }
    }

    /**
     * create pool task callable
     */
    final class CreatePoolCallable implements Callable<SparrowChannelPool> {

        private final Url url;
        private final int syncCreateConnCount;

        public CreatePoolCallable(Url url, int syncCreateConnCount) {
            this.url = url;
            this.syncCreateConnCount = syncCreateConnCount;
        }

        @Override
        public SparrowChannelPool call() throws Exception {
            // create a pool, using Random Select Strategy temporarily
            SparrowChannelPool pool = new SparrowChannelPool(url, connectionFactory);
            int expectedPoolSize = url.getExpectedConnectionCount();
            int connTimeOut = 15000;
            // async heal connection pool
            pool.healConnectionPool(asyncConnectExecutor, expectedPoolSize, connTimeOut, syncCreateConnCount);
            return pool;
        }
    }

    /**
     * heal connection pool callable
     * This callable returns pool's size after healing
     */
    final class HealPoolCallable implements Callable<Integer> {
        private final SparrowChannelPool pool;
        private final int expectedCount;
        public HealPoolCallable(SparrowChannelPool pool, int expectedCount) {
            this.pool = pool;
            this.expectedCount = expectedCount;
        }

        @Override
        public Integer call() {
            try{
                // call connection pool's heal()
                pool.healConnectionPool(asyncConnectExecutor, expectedCount, 15000, 0);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            // return pool size after healing
            return pool.size();
        }
    }

    public void shutdown(){
        this.connectionFactory.shutdown();
    }
}
