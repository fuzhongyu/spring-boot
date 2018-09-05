package com.fzy.core.util.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程池（单例）
 * @author Fucai
 * @date 2018/3/19
 */
public class ExcutorProcessPool{

    private Logger logger= LoggerFactory.getLogger(ExcutorProcessPool.class);

    private final static int CORE_POOL_SIZE=50;
    private final static int MAXIMUM_POOL_SIZE=150;
    private final static int KEEP_ALIVE_TIME=20;

    private static ExcutorProcessPool excutorProcessPool=new ExcutorProcessPool();

    private ExecutorServiceFactory executorServiceFactory= ExecutorServiceFactory.getInstance();

    private ExecutorService executorService;

    private ExcutorProcessPool(){

        executorService=executorServiceFactory.createThreadPool(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    }

    public static ExcutorProcessPool getInstance(){return excutorProcessPool;}


    /**
     * 获取当前线程池中运行的线程数
     * @return
     */
    public Integer getThreadCount(){
        return ((ThreadPoolExecutor)executorService).getActiveCount();
    }

    /**
     * 关闭线程池
     */
    public void shutdown(){
        if (logger.isDebugEnabled()){
            logger.debug("---关闭线程池ExcutorProcessPool---");
        }
        executorService.shutdown();
    }

    /**
     * 提交任务到线程池，无返回值
     * @param runnable
     */
    public void excute(Runnable runnable){
        executorService.execute(runnable);
    }

    /**
     * 提交任务到线程池，可以接受返回值
     * @param callable
     * @return
     */
    public Future<?> submit(Callable<?> callable){
        return executorService.submit(callable);
    }

}
