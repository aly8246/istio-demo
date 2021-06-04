package com.aly8246.common.core;

import com.aly8246.common.util.IDUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SnowFlakeIdTest {
    @RequiredArgsConstructor
    public static class TestSnowflakeId implements Runnable {
        private final Lock lock = new ReentrantLock();
        private volatile Set<Long> skipListSet;
        private volatile CountDownLatch countDownLatch;

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                try {
                    lock.lock();
                    skipListSet.add(IDUtil.nextSnowflakeId());
                    lock.unlock();
                } catch (Exception ignored) {
                } finally {
                }
            }
            countDownLatch.countDown();
        }

        public TestSnowflakeId(Set<Long> skipListSet, CountDownLatch countDownLatch) {
            this.skipListSet = skipListSet;
            this.countDownLatch = countDownLatch;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        Set<Long> skipListSet = new ConcurrentSkipListSet<>();
        int testNumber = 50;
        final CountDownLatch countDownLatch = new CountDownLatch(testNumber);

        for (int i = 0; i < testNumber; i++) {
            TestSnowflakeId o = new TestSnowflakeId(skipListSet, countDownLatch);
            new Thread(o).start();
        }

        countDownLatch.await();
        System.out.println("执行次数:" + testNumber + " 结果数量:" + skipListSet.size());
    }
}
