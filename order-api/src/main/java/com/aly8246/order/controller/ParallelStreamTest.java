package com.aly8246.order.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ParallelStreamTest {
    static class MyTask {
        private final int duration;
        public MyTask(int duration) {
            this.duration = duration;
        }
        public int calculate() {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(duration * 1000L);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            return duration;
        }
    }
    public static void runSequentially(List<MyTask> tasks) {
        long start = System.nanoTime();
        List<Integer> result = tasks.stream()
                .map(MyTask::calculate)
                .collect(toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }
    public static void useParallelStream(List<MyTask> tasks) {
        long start = System.nanoTime();
        List<Integer> result = tasks.parallelStream()
                .map(MyTask::calculate)
                .collect(toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }

    public static void useCompletableFuture(List<MyTask> tasks) {
        long start = System.nanoTime();
        List<CompletableFuture<Integer>> futures =
                tasks.stream()
                        .map(t -> CompletableFuture.supplyAsync(t::calculate))
                        .collect(Collectors.toList());

        List<Integer> result =
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }

    public static void main(String[] args) {
        List<MyTask> tasks = IntStream.range(0, 5)
                .mapToObj(i -> new MyTask(1))
                .collect(toList());

        //串行
        runSequentially(tasks);
        System.out.println("----");

        //并行
        useParallelStream(tasks);
        System.out.println("----");

        //cf
        useCompletableFuture(tasks);
    }
}
