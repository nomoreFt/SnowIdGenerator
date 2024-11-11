package com.nomoreft.snowflake;

import com.nomoreft.snowflake.v1.SnowFlake;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnowFlakePerformanceTest {
    @Test
    public void nextId_withSingleThread() {
        int iterations = 1000000; // 1 million

        SnowFlake snowflake = new SnowFlake(897);
        long beginTimestamp = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            snowflake.nextId();
        }
        long endTimestamp = System.currentTimeMillis();

        long cost = (endTimestamp - beginTimestamp);
        long costMs = iterations/cost;
        System.out.println("Single Thread:: IDs per ms: " + costMs);
    }

    @Test
    public void nextId_withMultipleThreads() throws InterruptedException {
        int iterations = 1000000; // 1 million
        int numThreads = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        SnowFlake snowflake = new SnowFlake(897);

        long beginTimestamp = System.currentTimeMillis();
        for(int i = 0; i < iterations; i++) {
            executorService.submit(() -> {
                snowflake.nextId();
                latch.countDown();
            });
        }

        latch.await();
        long endTimestamp = System.currentTimeMillis();
        long cost = (endTimestamp - beginTimestamp);
        long costMs = iterations/cost;
        System.out.println(numThreads + " Threads:: IDs per ms: " + costMs);
    }
}
