
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("CONTADOR MULTIHILO: JAVA vs GO");
        System.out.println("Rango: 1 -> 50.000.000");
        System.out.println();

        System.out.print("Cuantos hilos quieres usar: ");
        int numHilos = 4;
        try {
            numHilos = Integer.parseInt(sc.nextLine().trim());
            if (numHilos < 1) {
                System.out.println("Valor invalido, usando 1 hilo.");
                numHilos = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido, usando 4 hilos.");
            numHilos = 4;
        }

        System.out.println();
        System.out.println("FASE 1: JAVA con " + numHilos + " hilo(s)");
        System.out.println();

        long tiempoJavaInicio = System.currentTimeMillis();

        try {
            Counter.ejecutar(numHilos);
        } catch (OutOfMemoryError e) {
            System.err.println("JAVA ERROR: Demasiados hilos. Se agoto la memoria.");
            System.err.println("Causa: " + e.getMessage());
        }

        long tiempoJavaTotal = System.currentTimeMillis() - tiempoJavaInicio;

        System.out.println();
        System.out.println("FASE 2: GO con " + numHilos + " goroutine(s)");
        System.out.println();

        long tiempoGoTotal = -1;

        try {
            long tiempoGoInicio = System.currentTimeMillis();

            ProcessBuilder pb = new ProcessBuilder(
                    "go", "run", "counter.go", String.valueOf(numHilos)
            );
            pb.directory(new File("../go"));
            pb.redirectErrorStream(true);

            Process proceso = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream())
            );
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            int exitCode = proceso.waitFor();
            tiempoGoTotal = System.currentTimeMillis() - tiempoGoInicio;

            if (exitCode != 0) {
                System.err.println("GO termino con codigo de error: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("No se pudo ejecutar Go. Verifica que este instalado y en el PATH.");
            System.err.println("Detalle: " + e.getMessage());
        }

        System.out.println();
        System.out.println("RESUMEN COMPARATIVO");
        System.out.println();
        System.out.println("Hilos usados:  " + numHilos);
        System.out.println();
        System.out.println("[JAVA] Tiempo: " + tiempoJavaTotal + " ms  |  " + (tiempoJavaTotal / 1000.0) + " seg");

        if (tiempoGoTotal >= 0) {
            System.out.println("[GO]   Tiempo: " + tiempoGoTotal + " ms  |  " + (tiempoGoTotal / 1000.0) + " seg");
            System.out.println();

            if (tiempoJavaTotal < tiempoGoTotal) {
                double diff = (double) (tiempoGoTotal - tiempoJavaTotal) / tiempoGoTotal * 100;
                System.out.printf("JAVA fue %.1f%% mas rapido que GO%n", diff);
            } else if (tiempoGoTotal < tiempoJavaTotal) {
                double diff = (double) (tiempoJavaTotal - tiempoGoTotal) / tiempoJavaTotal * 100;
                System.out.printf("GO fue %.1f%% mas rapido que JAVA%n", diff);
            } else {
                System.out.println("Ambos tardaron exactamente lo mismo.");
            }
        } else {
            System.out.println("[GO]   No se pudo ejecutar correctamente.");
        }

        sc.close();
    }
}
