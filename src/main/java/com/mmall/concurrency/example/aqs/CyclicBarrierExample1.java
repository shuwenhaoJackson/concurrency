package com.mmall.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CyclicBarrierExample1 {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
    public static void main(String[] args)  throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int threadNum = i;
            Thread.sleep(1000);
            executorService.execute(()->{
                try{
                    score(threadNum);
                }catch (Exception e){
                    log.error("exception",e);
                }
            });
        }
        executorService.shutdown();

    }

    private static void score(int threadNum) throws Exception{
            log.info("{}is ready",threadNum);
            cyclicBarrier.await();
            log.info("{} is continue",threadNum);
    }
}
