from mpi4py import MPI
import random

def main():
    # 1. Inicializar el entorno MPI
    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()  # El identificador único de este proceso
    size = comm.Get_size()  # El número total de procesos

    total_samples = 1_000_000
    samples_per_process = total_samples // size

    # 2. Cada proceso calcula su parte de forma INDEPENDIENTE
    local_count = 0
    random.seed(rank)  # Asegurar que cada proceso tenga una secuencia aleatoria diferente
    for _ in range(samples_per_process):
        x = random.random()
        y = random.random()
        if x**2 + y**2 <= 1.0:
            local_count += 1

    print(f"Proceso {rank}: calculó {local_count} puntos.")

    # 3. COMUNICACIÓN POR MENSAJES: Recoger todos los resultados parciales en el proceso 0
    #    La operación 'Gather' envía el valor de 'local_count' de CADA proceso al proceso 0.
    all_counts = comm.gather(local_count, root=0)

    # 4. Solo el proceso coordinador (rank 0) calcula el resultado final
    if rank == 0:
        global_count = sum(all_counts)
        pi_approx = (4 * global_count) / total_samples
        print(f"\nNumero total de puntos: {total_samples}")
        print(f"Puntos dentro del circulo: {global_count}")
        print(f"Aproximacion de pi: {pi_approx}")
        print(f"Error: {abs(pi_approx - 3.1415926535)}")

if __name__ == "__main__":
    main()

# mpiexec -n 4 python MonteCarloMPI.py