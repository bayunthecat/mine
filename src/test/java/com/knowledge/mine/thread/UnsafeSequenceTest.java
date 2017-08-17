package com.knowledge.mine.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Occasionally, test fill be successful proving that volatile
 * keyword does not grant atomicity to increment operation.
 */
public class UnsafeSequenceTest {

    private List<Thread> threads;

    private List<SequenceUser> sequenceUsers;

    private static final int NUMBER_OF_THREADS = 10;

    private static final int SEQUENCE_USER_TIMES = 1000;

    @Before
    public void init() {
        UnsafeSequence sequence = new UnsafeSequence();
        threads = new ArrayList<>(NUMBER_OF_THREADS);
        sequenceUsers = new ArrayList<>(NUMBER_OF_THREADS);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            SequenceUser user = new SequenceUser(sequence, SEQUENCE_USER_TIMES);
            sequenceUsers.add(user);
            threads.add(new Thread(user));
        }
    }

    @Test
    public void testUnsafeSequence() {
        for (Thread user : threads) {
            user.start();
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread " + thread + " was interrupted: " + e.getMessage());
            }
        });
        Set<Integer> uniqueIds = new HashSet<>();
        boolean hasDuplicates = false;
        for (SequenceUser user : sequenceUsers) {
            for (Integer i : user.getUniqueIds()) {
                if (!uniqueIds.add(i)) {
                    hasDuplicates = true;
                }
            }
        }
        assertTrue(hasDuplicates);
    }

    private class SequenceUser implements Runnable {

        private List<Integer> uniqueIds;

        private UnsafeSequence sequence;

        private int times;

        SequenceUser(UnsafeSequence sequence, int times) {
            this.sequence = sequence;
            this.times = times;
            uniqueIds = new ArrayList<>();
        }

        @Override
        public void run() {
            for (int i = 0; i < times; i++) {
                uniqueIds.add(sequence.getNext());
            }
        }

        List<Integer> getUniqueIds() {
            return uniqueIds;
        }
    }
}