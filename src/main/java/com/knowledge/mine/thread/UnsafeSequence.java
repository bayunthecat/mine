package com.knowledge.mine.thread;

public class UnsafeSequence {

    private volatile int value;

    public int getNext() {
        return value++;
    }
}
