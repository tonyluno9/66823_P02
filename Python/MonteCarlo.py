import threading
import random

# 1. Variables en Memoria Compartida
global_count = 0  # Variable compartida que todos los hilos quieren modificar
lock = threading.Lock()  # Cerrajo (Lock) para sincronizar el acceso

def monte_carlo_pi(num_samples, thread_id):
    """
    Función que ejecuta cada hilo.
    Genera puntos aleatorios y cuenta cuántos caen dentro del círculo.
    """
    local_count = 0
    for _ in range(num_samples):
        # Generar un punto aleatorio (x, y) en el rango [0, 1)
        x = random.random()
        y = random.random()
        # Verificar si el punto está dentro del círculo (x² + y² <= 1)
        if x**2 + y**2 <= 1.0:
            local_count += 1

    # 2. ¡SECCIÓN CRÍTICA!
    # Múltiples hilos intentan actualizar global_count al mismo tiempo.
    # Usamos el lock para garantizar que solo un hilo lo haga a la vez.
    global global_count
    with lock:
        print(f"Hilo {thread_id}: añadiendo {local_count} puntos al total.")
        global_count += local_count

def main():
    total_samples = 1_000_000
    num_threads = 4
    samples_per_thread = total_samples // num_threads

    threads = []

    # Crear y lanzar los hilos
    for i in range(num_threads):
        thread = threading.Thread(target=monte_carlo_pi, args=(samples_per_thread, i))
        threads.append(thread)
        thread.start()

    # Esperar a que todos los hilos terminen
    for thread in threads:
        thread.join()

    # 3. Todos los hilos han terminado. El resultado final está en la memoria compartida.
    pi_approx = (4 * global_count) / total_samples
    print(f"\nNúmero total de puntos: {total_samples}")
    print(f"Puntos dentro del círculo: {global_count}")
    print(f"Aproximación de pi: {pi_approx}")
    print(f"Error: {abs(pi_approx - 3.1415926535)}")


if __name__ == "__main__":
    main()