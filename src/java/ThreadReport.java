import java.util.ArrayList;
import java.util.List;

public class ThreadReport {

    private int totalHilos;
    private List<String> registros = new ArrayList<>();

    public ThreadReport(int totalHilos) {
        this.totalHilos = totalHilos;
    }

    public synchronized void registrar(int id, long inicio, long fin, long tiempoMs) {
        registros.add("Hilo:   " + id + "\nInicio: " + inicio + "\nFin:    " + fin + "\nTiempo: " + tiempoMs + " ms");
    }

    public void imprimir() {
        System.out.println();
        System.out.println("Reporte de hilos");
        System.out.println("Total de hilos: " + totalHilos);
        System.out.println();
        for (String r : registros) {
            System.out.println(r);
            System.out.println();
        }
    }
}
