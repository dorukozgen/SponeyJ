package com.dorukozgen.sponeyj.streamer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static final ConcurrentHashMap<Integer, Streamer> threads = new ConcurrentHashMap<>();

    private static ExecutorService executor;

    private static boolean running = false;

    public static void addThread(Streamer thread) {
        threads.put(Integer.valueOf(threads.size()), thread);
    }

    public static void removeThread(int id) {
        ((Streamer)threads.get(Integer.valueOf(id))).interrupt();
        threads.remove(Integer.valueOf(id));
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        ThreadManager.running = running;
    }

    public static ConcurrentHashMap<Integer, Streamer> getThreads() {
        return threads;
    }

    public static void runThreads() {
        setRunning(true);
        Values.setThreadCount(threads.size());
        executor = Executors.newFixedThreadPool(threads.size());
        for (Streamer thread : threads.values()) {
            executor.execute(thread);
            Utils.wait(1);
        }
    }

    public static void stopThreads() {
        try {
            threads.forEach((k, v) -> v.interrupt());
            if (executor != null) {
                executor.shutdown();
                while (!executor.isShutdown())
                    Utils.wait(1);
            }
            threads.clear();
            Utils.clearProcesses();
            setRunning(false);
        } catch (Exception exception) {}
    }
}