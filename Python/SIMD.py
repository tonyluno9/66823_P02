import numpy as np
import time

def main():
    # Tamaño de los arrays (usamos 10 millones de elementos)
    n = 10_000_000
    
    # 1. Crear arrays de números aleatorios
    print("Creando arrays...")
    a = np.random.rand(n).astype(np.float32)
    b = np.random.rand(n).astype(np.float32)
    c = np.zeros(n, dtype=np.float32)
    
    # 2. Operación SCALAR (no SIMD) - como referencia
    print("Ejecutando operación escalar...")
    start_time = time.time()
    for i in range(n):
        c[i] = a[i] * b[i] + 2.0
    scalar_time = time.time() - start_time
    print(f"Tiempo escalar: {scalar_time:.4f} segundos")
    
    # 3. Operación VECTORIAL (SIMD) con NumPy
    print("Ejecutando operación vectorial SIMD...")
    start_time = time.time()
    c_simd = a * b + 2.0  # ¡ESTA ES LA OPERACIÓN SIMD!
    simd_time = time.time() - start_time
    print(f"Tiempo SIMD: {simd_time:.4f} segundos")
    
    # 4. Verificar que los resultados son iguales
    print(f"Resultados iguales: {np.allclose(c, c_simd)}")
    print(f"Speedup: {scalar_time/simd_time:.2f}x")
    
    # 5. Más operaciones SIMD comunes
    print("\nOtras operaciones SIMD:")
    
    # Suma de todos los elementos (reduce el array a un escalar)
    suma = np.sum(a)
    print(f"Suma de a: {suma:.4f}")
    
    # Operaciones trigonométricas vectorizadas
    seno_a = np.sin(a)
    print(f"Sin(a) calculado para {n} elementos")
    
    # Operaciones de comparación vectorizadas
    mayores = a > 0.5
    print(f"Elementos de a > 0.5: {np.sum(mayores)}")

if __name__ == "__main__":
    main()