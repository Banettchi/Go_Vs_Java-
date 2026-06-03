package main

import (
	"fmt"
	"os"
	"runtime"
	"strconv"
	"sync"
	"time"
)

const (
	TOTAL_INICIO int64 = 1
	TOTAL_FIN    int64 = 50_000_000
)

func main() {
	numGoroutines := 4
	if len(os.Args) > 1 {
		n, err := strconv.Atoi(os.Args[1])
		if err != nil || n < 1 {
			fmt.Println("Argumento invalido. Uso: go run counter.go <numero_goroutines>")
			os.Exit(1)
		}
		numGoroutines = n
	}

	fmt.Println("CONTADOR MULTI-GOROUTINE - GO")
	fmt.Printf("Rango total:  %d -> %d\n", TOTAL_INICIO, TOTAL_FIN)
	fmt.Printf("Goroutines:   %d\n", numGoroutines)
	fmt.Printf("CPUs logicos: %d\n", runtime.NumCPU())
	fmt.Println()

	totalNumeros := TOTAL_FIN - TOTAL_INICIO + 1
	rangoPorG := totalNumeros / int64(numGoroutines)
	resto := totalNumeros % int64(numGoroutines)

	var wg sync.WaitGroup

	tiempoInicio := time.Now()

	inicio := TOTAL_INICIO
	for i := 0; i < numGoroutines; i++ {
		idGoroutine := i + 1
		fin := inicio + rangoPorG - 1

		if i == numGoroutines-1 {
			fin = fin + resto
		}

		rangoInicio := inicio
		rangoFin := fin

		fmt.Printf("Goroutine %d: rango [%d ... %d]\n", idGoroutine, rangoInicio, rangoFin)

		wg.Add(1)
		go func(id int, rInicio int64, rFin int64) {
			defer wg.Done()

			tInicioG := time.Now()

			for n := rInicio; n <= rFin; n++ {
				fmt.Printf("[Goroutine-%d] %d\n", id, n)
			}

			elapsed := time.Since(tInicioG)
			fmt.Printf("Goroutine %d terminada en %d ms\n", id, elapsed.Milliseconds())
		}(idGoroutine, rangoInicio, rangoFin)

		inicio = fin + 1
	}

	fmt.Println()
	fmt.Println("Todas las goroutines iniciadas. Esperando...")

	wg.Wait()

	tiempoTotal := time.Since(tiempoInicio)

	fmt.Println()
	fmt.Println("TODAS LAS GOROUTINES COMPLETADAS")
	fmt.Printf("Goroutines usadas: %d\n", numGoroutines)
	fmt.Printf("Rango total:       %d -> %d\n", TOTAL_INICIO, TOTAL_FIN)
	fmt.Printf("Tiempo total:      %d ms\n", tiempoTotal.Milliseconds())
	fmt.Printf("Tiempo total:      %.2f segundos\n", tiempoTotal.Seconds())
	fmt.Printf("Tiempo total:      %.4f minutos\n", tiempoTotal.Minutes())
}
