
public class Counter {

    static final long RANGE_START = 1L;
    static final long RANGE_END = 50_000_000L;

    public static void ejecutar(int numThreads) throws InterruptedException {

        System.out.println("Total range: " + RANGE_START + " -> " + RANGE_END);
        System.out.println("Java threads: " + numThreads);
        System.out.println();

        long totalNumbers = RANGE_END - RANGE_START + 1;
        long rangePerThread = totalNumbers / numThreads;
        long remainder = totalNumbers % numThreads;

        Thread[] threads = new Thread[numThreads];
        ThreadReport report = new ThreadReport(numThreads);

        long start = RANGE_START;
        for (int i = 0; i < numThreads; i++) {
            int threadId = i + 1;
            long rangeStart = start;
            long rangeEnd = start + rangePerThread - 1;

            if (i == numThreads - 1) {
                rangeEnd = rangeEnd + remainder;
            }

            final long rStartFinal = rangeStart;
            final long rEndFinal = rangeEnd;

            System.out.println("Thread " + threadId + ": range [" + rStartFinal + " ... " + rEndFinal + "]");

            threads[i] = new Thread(() -> {
                long threadStartTime = System.currentTimeMillis();

                for (long n = rStartFinal; n <= rEndFinal; n++) {
                    System.out.println("[Thread-" + threadId + "] " + n);
                }

                long elapsedMs = System.currentTimeMillis() - threadStartTime;

                report.registrar(threadId, rStartFinal, rEndFinal, elapsedMs);
            });

            threads[i].start();
            start = rEndFinal + 1;
        }

        System.out.println();
        System.out.println("All threads started. Waiting...");

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        report.imprimir();
    }

    public static void main(String[] args) throws InterruptedException {

        int numThreads = 4;
        if (args.length > 0) {
            try {
                numThreads = Integer.parseInt(args[0]);
                if (numThreads < 1) {
                    numThreads = 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Usage: java Counter <number_of_threads>");
                System.exit(1);
            }
        }

        System.out.println("MULTITHREAD COUNTER - JAVA");
        System.out.println();

        long start = System.currentTimeMillis();

        try {
            ejecutar(numThreads);
        } catch (OutOfMemoryError e) {
            System.err.println("ERROR: Too many threads. Memory ran out.");
            System.err.println("Cause: " + e.getMessage());
            System.exit(2);
        }

        long total = System.currentTimeMillis() - start;

        System.out.println("All threads completed.");
        System.out.println("Threads used:  " + numThreads);
        System.out.println("Total time:    " + total + " ms");
        System.out.println("Total time:    " + (total / 1000.0) + " sec");
        System.out.println("Total time:    " + (total / 60000.0) + " min");
    }
}
