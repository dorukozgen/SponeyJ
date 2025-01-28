package com.dorukozgen.sponeyj.streamer;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.atomic.AtomicInteger;

public class Values {
    private static final AtomicInteger threadCount = new AtomicInteger(0);

    private static final AtomicInteger totalStreams = new AtomicInteger(0);

    private static final AtomicInteger successfulStreams = new AtomicInteger(0);

    private static final AtomicInteger failedStreams = new AtomicInteger(0);

    private static final AtomicDouble earning = new AtomicDouble(0.0D);

    public static synchronized int getThreadCount() {
        return threadCount.get();
    }

    public static synchronized int addThreadCount() {
        return threadCount.incrementAndGet();
    }

    public static synchronized int removeThreadCount() {
        return threadCount.decrementAndGet();
    }

    public static synchronized void setThreadCount(int count) {
        threadCount.set(count);
    }

    public static synchronized int getTotalStreams() {
        return totalStreams.get();
    }

    public static synchronized int addTotalStreams() {
        return totalStreams.incrementAndGet();
    }

    public static synchronized int getSuccessfulStreams() {
        return successfulStreams.get();
    }

    public static synchronized int addSuccessfulStreams() {
        addTotalStreams();
        return successfulStreams.incrementAndGet();
    }

    public static synchronized int getFailedStreams() {
        return failedStreams.get();
    }

    public static synchronized int addFailedStreams() {
        addTotalStreams();
        return failedStreams.incrementAndGet();
    }

    public static synchronized double getEarning() {
        return earning.get();
    }

    public static synchronized void addEarning(double amount) {
        earning.addAndGet(amount);
    }

    public static synchronized void reset() {
        threadCount.set(0);
        totalStreams.set(0);
        successfulStreams.set(0);
        failedStreams.set(0);
        earning.set(0.0D);
    }
}
