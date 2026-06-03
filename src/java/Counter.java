
public class Counter {

    static final long TOTAL_INICIO = 1L;
    static final long TOTAL_FIN = 50_000_000L;

    public static void ejecutar(int numHilos) throws InterruptedException {

        System.out.println("Rango total: " + TOTAL_INICIO + " -> " + TOTAL_FIN);
        System.out.println("Hilos Java:  " + numHilos);
        System.out.println();

        long totalNumeros = TOTAL_FIN - TOTAL_INICIO + 1;
        long rangoPorHilo = totalNumeros / numHilos;
        long resto = totalNumeros % numHilos;

        Thread[] hilos = new Thread[numHilos];
        ThreadReport reporte = new ThreadReport(numHilos);

        long inicio = TOTAL_INICIO;
        for (int i = 0; i < numHilos; i++) {
            int idHilo = i + 1;
            long rangoInicio = inicio;
            long rangoFin = inicio + rangoPorHilo - 1;

            if (i == numHilos - 1) {
                rangoFin = rangoFin + resto;
            }

            final long rInicioFinal = rangoInicio;
            final long rFinFinal = rangoFin;

            System.out.println("Hilo " + idHilo + ": rango [" + rInicioFinal + " ... " + rFinFinal + "]");

            hilos[i] = new Thread(() -> {
                long tInicioHilo = System.currentTimeMillis();

                for (long n = rInicioFinal; n <= rFinFinal; n++) {
                    System.out.println("[Hilo-" + idHilo + "] " + n);
                }

                long tiempoMs = System.currentTimeMillis() - tInicioHilo;

                reporte.registrar(idHilo, rInicioFinal, rFinFinal, tiempoMs);
            });

            hilos[i].start();
            inicio = rFinFinal + 1;
        }

        System.out.println();
        System.out.println("Todos los hilos iniciados. Esperando...");

        for (int i = 0; i < numHilos; i++) {
            hilos[i].join();
        }

        reporte.imprimir();
    }

    public static void main(String[] args) throws InterruptedException {

        int numHilos = 4;
        if (args.length > 0) {
            try {
                numHilos = Integer.parseInt(args[0]);
                if (numHilos < 1) {
                    numHilos = 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Uso: java Counter <numero_de_hilos>");
                System.exit(1);
            }
        }

        System.out.println("CONTADOR MULTIHILO - JAVA");
        System.out.println();

        long inicio = System.currentTimeMillis();

        try {
            ejecutar(numHilos);
        } catch (OutOfMemoryError e) {
            System.err.println("ERROR: Demasiados hilos. Se agoto la memoria.");
            System.err.println("Causa: " + e.getMessage());
            System.exit(2);
        }

        long total = System.currentTimeMillis() - inicio;

        System.out.println("Todos los hilos completados.");
        System.out.println("Hilos usados:  " + numHilos);
        System.out.println("Tiempo total:  " + total + " ms");
        System.out.println("Tiempo total:  " + (total / 1000.0) + " seg");
        System.out.println("Tiempo total:  " + (total / 60000.0) + " min");
    }
}
