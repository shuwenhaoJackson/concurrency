package com.mmall.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CyclicBarrierExample3 {
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5,()->{
        //表示到达屏障时优先执行该内容
        log.info("callback running");
    });
    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int threadNum = i;
            Thread.sleep(1000);
           executorService.execute(()->{
               try{
                    show(threadNum);
               }catch (Exception e){
                   log.error("exception",e);
               }
           });
        }
    }

    private static void show(int threadNum) throws Exception{
        Thread.sleep(1000);
        log.info("{} is ready");
        cyclicBarrier.await();
        log.info("{} is continue");
    }
}
