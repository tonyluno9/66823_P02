Suma de Numeros con Concurrencia y Paralelismo



Este proyecto implementa la suma de 1,000,000 numeros en un arreglo.  

Se midio el tiempo de ejecucion en dos escenarios:



Secuencial: un solo proceso que recorre el arreglo.  

Paralelo: varios hilos/procesos que dividen el trabajo y suman en conjunto.  



Los resultados se usaron para calcular las metricas de rendimiento.





Datos de ejecucion

\- Tiempo secuencial (Ts) = 100 ms  

\- Tiempo paralelo (Tp) = 40 ms  

\- Numero de procesos (p) = 4  



Metricas



Speedup

S = Ts / Tp = 100 / 40 = 2.5



Eficiencia

E = S / p = 2.5 / 4 = 0.625 (62.5%)



Overhead

Formula: To = p \* Tp - Ts  

Calculo: To = 4 \* 40 - 100 = 60 ms



Costo

C = p \* Tp = 4 \* 40 = 160 ms  

(Como C > Ts, no es cost-optimo; el exceso es el overhead de 60 ms)



Fraccion paralelizable (Ley de Amdahl)

S = 1 / ((1 - f) + f / p)  

De aqui f ≈ 0.8 (80% paralelizable)



Limite teorico con p → infinito:  

1 / (1 - f) = 1 / 0.2 = 5



Resumen



Speedup = 2.5  

Eficiencia = 0.625 (62.5%)  

Overhead = 60 ms  

Costo = 160 ms  

Fraccion paralelizable f ≈ 0.8  

Speedup maximo teorico ≈ 5  

