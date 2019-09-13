package ru.shabarov.concurrency.future;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Example from 'Cay Horstmann 'Core Java Volume 1'
 */
public class FutureTest {

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            ExecutorService pool = Executors.newCachedThreadPool();
            System.out.println("Enter base directory: ");
            String dir = in.nextLine();
            System.out.println("Enter keyword: ");
            String keyword = in.nextLine();
            MatchCounter counter = new MatchCounter(new File(dir), keyword, pool);
            FutureTask<Integer> task = new FutureTask<>(counter);
            pool.execute(task);
            if (pool instanceof ThreadPoolExecutor) {
                int activeCount;
                do {
                    activeCount = ((ThreadPoolExecutor) pool).getActiveCount();
                    Thread.sleep(10);
                } while (activeCount > 0);
            }
            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println(task.get() + " matching files.");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MatchCounter implements Callable<Integer> {

        private File dir;
        private String keyword;
        private int count;
        private ExecutorService pool;

        public MatchCounter(File dir, String keyword, ExecutorService pool) {
            this.dir = dir;
            this.keyword = keyword;
            this.pool = pool;
        }

        @Override
        public Integer call() throws Exception {
            count = 0;
            try {
                if (dir.isFile()) {
                    return search(dir) ? 1 : 0;
                }
                File[] files = dir.listFiles();
                List<Future<Integer>> results = new ArrayList<>();
                for (File file : files) {
                    if (file.isDirectory()) {
                        MatchCounter counter = new MatchCounter(file, keyword, pool);
                        FutureTask<Integer> task = new FutureTask<>(counter);
                        results.add(task);
                        pool.execute(task);
                    } else {
                        if (search(file)) count++;
                    }
                }
                for (Future<Integer> result : results) {
                    try {
                        count += result.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return count;
        }

        public boolean search(File file) {
            try {
                try (Scanner in = new Scanner(file)) {
                    boolean found = false;
                    while (!found && in.hasNextLine()) {
                        String line = in.nextLine();
                        if (line.contains(keyword)) found = true;
                    }
                    return found;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
