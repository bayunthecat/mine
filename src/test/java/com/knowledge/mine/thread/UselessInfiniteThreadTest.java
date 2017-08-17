package com.knowledge.mine.thread;

import org.junit.Test;
import static org.junit.Assert.*;

public class UselessInfiniteThreadTest {

    @Test
    public void testUselessThreadStop() throws InterruptedException {
        Thread thread = new Thread(new UselessInfiniteThread(1000));
        thread.start();
        thread.interrupt();
        assertEquals(true, thread.isInterrupted());
    }
}