import mpi.MPI;
import java.util.SplittableRandom;

public class MonteCarloMPI {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank(); // id del proceso
        int size = MPI.COMM_WORLD.Size(); // total de procesos

        long totalSamples = 1_000_000L;
        long samplesPerProcess = totalSamples / size;

        // 2. Cada proceso calcula su parte (semilla distinta por rank)
        long localCount = 0L;
        SplittableRandom rng = new SplittableRandom(rank); // similar a random.seed(rank)
        for (long i = 0; i < samplesPerProcess; i++) {
            double x = rng.nextDouble(); // [0,1)
            double y = rng.nextDouble();
            if (x * x + y * y <= 1.0) localCount++;
        }

        System.out.printf("Proceso %d: calculó %d puntos.%n", rank, localCount);

        // 3. COMUNICACIÓN: Gather de contadores hacia el proceso 0
        long[] sendBuf = new long[] { localCount };
        long[] recvBuf = (rank == 0) ? new long[size] : null;
        MPI.COMM_WORLD.Gather(sendBuf, 0, 1, MPI.LONG, recvBuf, 0, 1, MPI.LONG, 0);

        // 4. Proceso 0 agrega y calcula pi
        if (rank == 0) {
            long globalCount = 0L;
            for (long c : recvBuf) globalCount += c;

            double piApprox = (4.0 * globalCount) / (double) totalSamples;
            System.out.println("\nNumero total de puntos: " + totalSamples);
            System.out.println("Puntos dentro del circulo: " + globalCount);
            System.out.println("Aproximacion de pi: " + piApprox);
            System.out.println("Error: " + Math.abs(piApprox - 3.1415926535));
        }

        MPI.Finalize();
    }
}
