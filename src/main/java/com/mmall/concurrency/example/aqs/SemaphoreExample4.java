package com.mmall.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SemaphoreExample4 {

    private static int threadCount = 20;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum =i;
            executorService.execute(()->{
                try{
                    if (semaphore.tryAcquire(5000,TimeUnit.MILLISECONDS)) {
                        test(threadNum);
                        semaphore.release();
                    }
                }catch (Exception e){
                    log.error("exception",e);
                }
            });
        }
        executorService.shutdown();
    }

    private static void test(int threadNum) throws Exception {

        log.info("{}",threadNum);
        Thread.sleep(1000);
    }
}
