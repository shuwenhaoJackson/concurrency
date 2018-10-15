package com.mmall.concurrency.example.aqs;

import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class ForkJoinTaskExample extends RecursiveTask<Integer> {

    public static final int threadshold = 2;
    private int start;
    private int end;

    public ForkJoinTaskExample(int start, int end){
        this.start = start;
        this.end = end;
    }
    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected Integer compute() {
        int sum = 0;

        //如果任务足够小就计算任务
        boolean canCompute = (end-start) <= threadshold;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        }else {
            //如果任务大于阀值，就分裂成两个子任务计算
            int middle = (start + end) /2;
            ForkJoinTaskExample leftTask = new ForkJoinTaskExample(start, middle);
            ForkJoinTaskExample rightTask = new ForkJoinTaskExample(middle + 1, end);

            //执行子任务
            leftTask.fork();
            rightTask.fork();

            //等待任务执行结束合并其结果
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();

            //合并子任务
            sum = leftResult+rightResult;
        }

        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //生成一个子计算任务，
        ForkJoinTaskExample task = new ForkJoinTaskExample(1, 100);

        //执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);

        try {
            log.info("result:{}", result.get());
        } catch (Exception e) {
            log.error("exception",e);
        }
    }
}
