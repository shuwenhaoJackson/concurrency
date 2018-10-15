package com.mmall.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CountDownLatchExample2 {

    private static final int threadCount = 200;

    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.execute(()->{
                try{
                        show(threadNum);
                }catch (Exception e){
                    log.error("exception",e);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(50, TimeUnit.MILLISECONDS);
        log.info("finish");
        executorService.shutdown();
    }

    private static void show(int threadNum) throws Exception {
        Thread.sleep(30);
        log.info("{}",threadNum);
    }
}
