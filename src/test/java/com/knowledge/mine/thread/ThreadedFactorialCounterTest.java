package com.knowledge.mine.thread;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadedFactorialCounterTest {

     private FactorialCounter counter;

     @Before
     public void init() {
         counter = new ThreadedFactorialCounter();
     }

    @Test
    public void testFactorialWithSmallNumber() {
        long result = counter.factorial(1);
        assertEquals(1, result);
    }

    @Test
    public void testFactorialWithZero() {
        long result = counter.factorial(0);
        assertEquals(1, result);
    }

     @Test
     public void testFactorialCounter() {
         long result = counter.factorial(3);
         assertEquals(6, result);
     }

     @Test
     public void testFactorialWithCustomSettings() {
         counter = new ThreadedFactorialCounter(5, 20);
         long result = counter.factorial(10);
         assertEquals(3_628_800, result);
     }

    @Test
    public void testFactorial() {
        counter = new ThreadedFactorialCounter(5, 20);
        long result = counter.factorial(11);
        assertEquals(39_916_800, result);
    }

    @Test
    public void testFactorialWithLargeNumber() {
        counter = new ThreadedFactorialCounter(5, 20);
        long result = counter.factorial(13);
        assertEquals(6_227_020_800L, result);
    }
}