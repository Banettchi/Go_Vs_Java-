
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("MULTITHREAD COUNTER: JAVA vs GO");
        System.out.println("Range: 1 -> 50,000,000");
        System.out.println();

        System.out.print("How many threads do you want to use: ");
        int numThreads = 4;
        try {
            numThreads = Integer.parseInt(sc.nextLine().trim());
            if (numThreads < 1) {
                System.out.println("Invalid value, using 1 thread.");
                numThreads = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid value, using 4 threads.");
            numThreads = 4;
        }

        System.out.println();
        System.out.println("PHASE 1: JAVA with " + numThreads + " thread(s)");
        System.out.println();

        long javaStartTime = System.currentTimeMillis();

        try {
            Counter.ejecutar(numThreads);
        } catch (OutOfMemoryError e) {
            System.err.println("JAVA ERROR: Too many threads. Memory ran out.");
            System.err.println("Cause: " + e.getMessage());
        }

        long javaTotalTime = System.currentTimeMillis() - javaStartTime;

        System.out.println();
        System.out.println("PHASE 2: GO with " + numThreads + " goroutine(s)");
        System.out.println();

        long goTotalTime = -1;

        try {
            long goStartTime = System.currentTimeMillis();

            ProcessBuilder pb = new ProcessBuilder(
                    "go", "run", "counter.go", String.valueOf(numThreads)
            );
            pb.directory(new File("../go"));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            goTotalTime = System.currentTimeMillis() - goStartTime;

            if (exitCode != 0) {
                System.err.println("GO finished with error code: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("Could not run Go. Make sure it is installed and on your PATH.");
            System.err.println("Detail: " + e.getMessage());
        }

        System.out.println();
        System.out.println("COMPARATIVE SUMMARY");
        System.out.println();
        System.out.println("Threads used:  " + numThreads);
        System.out.println();
        System.out.println("[JAVA] Time: " + javaTotalTime + " ms  |  " + (javaTotalTime / 1000.0) + " sec");

        if (goTotalTime >= 0) {
            System.out.println("[GO]   Time: " + goTotalTime + " ms  |  " + (goTotalTime / 1000.0) + " sec");
            System.out.println();

            if (javaTotalTime < goTotalTime) {
                double diff = (double) (goTotalTime - javaTotalTime) / goTotalTime * 100;
                System.out.printf("JAVA was %.1f%% faster than GO%n", diff);
            } else if (goTotalTime < javaTotalTime) {
                double diff = (double) (javaTotalTime - goTotalTime) / javaTotalTime * 100;
                System.out.printf("GO was %.1f%% faster than JAVA%n", diff);
            } else {
                System.out.println("Both took exactly the same amount of time.");
            }
        } else {
            System.out.println("[GO]   Could not run correctly.");
        }

        sc.close();
    }
}
