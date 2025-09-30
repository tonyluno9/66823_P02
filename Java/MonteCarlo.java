package Java;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class MonteCarlo {

    // 1. Variables en Memoria Compartida
    private static long globalCount = 0;               // variable compartida
    private static final ReentrantLock lock = new ReentrantLock(); // cerrojo

    static class Worker implements Runnable {
        private final long numSamples;
        private final int threadId;

        Worker(long numSamples, int threadId) {
            this.numSamples = numSamples;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            long localCount = 0;
            ThreadLocalRandom rng = ThreadLocalRandom.current();

            // Generar puntos aleatorios y contar aciertos
            for (long i = 0; i < numSamples; i++) {
                double x = rng.nextDouble(); // [0,1)
                double y = rng.nextDouble();
                if (x * x + y * y <= 1.0) localCount++;
            }

            // 2. ¡SECCIÓN CRÍTICA!
            lock.lock();
            try {
                System.out.printf("Hilo %d: añadiendo %d puntos al total.%n", threadId, localCount);
                globalCount += localCount;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long totalSamples = (args.length > 0) ? Long.parseLong(args[0]) : 1_000_000L;
        int numThreads    = (args.length > 1) ? Integer.parseInt(args[1]) : 4;

        long samplesPerThread = totalSamples / numThreads;
        Thread[] threads = new Thread[numThreads];

        // Crear y lanzar hilos
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new Worker(samplesPerThread, i));
            threads[i].start();
        }

        // Esperar a que terminen
        for (Thread t : threads) t.join();

        // 3. Resultado final en memoria compartida
        double piApprox = (4.0 * globalCount) / (double) totalSamples;
        System.out.println("\nNúmero total de puntos: " + totalSamples);
        System.out.println("Puntos dentro del círculo: " + globalCount);
        System.out.println("Aproximación de pi: " + piApprox);
        System.out.println("Error: " + Math.abs(piApprox - 3.1415926535));
    }
}
