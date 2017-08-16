package com.knowledge.mine.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadedFactorialCounter implements FactorialCounter {

    private static final int DEFAULT_NUMBERS_PER_THREAD = 10;

    private static final int DEFAULT_TIMEOUT = 10;

    private final int numbersPerThread;

    private final int timeout;

    public ThreadedFactorialCounter() {
        this.numbersPerThread = DEFAULT_NUMBERS_PER_THREAD;
        this.timeout = DEFAULT_TIMEOUT;
    }

    public ThreadedFactorialCounter(int numbersPerThread, int timeout) {
        if (numbersPerThread <= 0) {
            this.numbersPerThread = DEFAULT_NUMBERS_PER_THREAD;
        } else {
            this.numbersPerThread = numbersPerThread;
        }
        this.timeout = timeout;
    }

    @Override
    public long factorial(long number) {
        long start = 1, finish;
        ExecutorService executorService = new ForkJoinPool();
        List<Callable<Long>> tasks = new ArrayList<>();
        do {
            finish = number > start + numbersPerThread ? start + numbersPerThread : number + 1;
            tasks.add(new FactorMultiplier(start, finish));
            start = finish;
        } while (finish < number);
        return countFinalFactorial(executorService, tasks);
    }

    private long countFinalFactorial(ExecutorService service, List<Callable<Long>> tasks) {
        long result = 1;
        try {
            List<Future<Long>> futures = service.invokeAll(tasks);
            service.shutdown();
            if (!service.awaitTermination(timeout, TimeUnit.SECONDS)) {
                throw new RuntimeException("Calculation timeout exceeded.");
            }
            for (Future<Long> partial : futures) {
                result *= partial.get();
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        } catch (ExecutionException e) {
            System.out.print(e.getMessage());
        }
        return result;
    }

    private class FactorMultiplier implements Callable<Long> {

        private final long from, to;

        FactorMultiplier(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Long call() throws Exception {
            long result = 1;
            for (long i = from; i < to; i++) {
                result *= i;
            }
            return result;
        }
    }
}