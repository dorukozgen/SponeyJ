package com.dorukozgen.sponeyj.websocket.responses;

public class UpdateResponse {
    private int threadCount;

    private int totalStreams;

    private int successfulStreams;

    private int failedStreams;

    private double earning;

    public UpdateResponse() {}

    public UpdateResponse(int threadCount, int totalStreams, int successfulStreams, int failedStreams, double earning) {
        this.threadCount = threadCount;
        this.totalStreams = totalStreams;
        this.successfulStreams = successfulStreams;
        this.failedStreams = failedStreams;
        this.earning = earning;
    }

    public int getThreadCount() {
        return this.threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getTotalStreams() {
        return this.totalStreams;
    }

    public void setTotalStreams(int totalStreams) {
        this.totalStreams = totalStreams;
    }

    public int getSuccessfulStreams() {
        return this.successfulStreams;
    }

    public void setSuccessfulStreams(int successfulStreams) {
        this.successfulStreams = successfulStreams;
    }

    public int getFailedStreams() {
        return this.failedStreams;
    }

    public void setFailedStreams(int failedStreams) {
        this.failedStreams = failedStreams;
    }

    public double getEarning() {
        return this.earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }
}
