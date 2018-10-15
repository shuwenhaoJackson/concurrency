package com.mmall.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
public class SemaphoreExample1 {

    private static final int threadCount = 200;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.execute(()->{
                try{
                    //获取一个许可
                    semaphore.acquire();
                    test(threadNum);
                    //释放一个许可
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception",e);
                }
            });
        }
        log.info("finish");
        executorService.shutdown();

    }

    private static void test(int threadNum) throws Exception{
        log.info("{}",threadNum);
        Thread.sleep(1000);
    }
}
