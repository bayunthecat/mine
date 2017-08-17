package com.knowledge.mine.thread;


public class UselessInfiniteThread implements Runnable {

    private final long sleepInterval;

    public UselessInfiniteThread(long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("I'm working.");
        }
    }

}
