import java.util.ArrayList;
import java.util.List;

public class ThreadReport {

    private int totalThreads;
    private List<String> records = new ArrayList<>();

    public ThreadReport(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public synchronized void registrar(int id, long start, long end, long elapsedMs) {
        records.add("Thread:  " + id + "\nStart:   " + start + "\nEnd:     " + end + "\nTime:    " + elapsedMs + " ms");
    }

    public void imprimir() {
        System.out.println();
        System.out.println("Thread Report");
        System.out.println("Total threads: " + totalThreads);
        System.out.println();
        for (String r : records) {
            System.out.println(r);
            System.out.println();
        }
    }
}
