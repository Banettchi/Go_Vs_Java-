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
	RANGE_START int64 = 1
	RANGE_END   int64 = 50_000_000
)

func main() {
	numGoroutines := 4
	if len(os.Args) > 1 {
		n, err := strconv.Atoi(os.Args[1])
		if err != nil || n < 1 {
			fmt.Println("Invalid argument. Usage: go run counter.go <number_of_goroutines>")
			os.Exit(1)
		}
		numGoroutines = n
	}

	fmt.Println("MULTI-GOROUTINE COUNTER - GO")
	fmt.Printf("Total range:   %d -> %d\n", RANGE_START, RANGE_END)
	fmt.Printf("Goroutines:    %d\n", numGoroutines)
	fmt.Printf("Logical CPUs:  %d\n", runtime.NumCPU())
	fmt.Println()

	totalNumbers := RANGE_END - RANGE_START + 1
	rangePerG := totalNumbers / int64(numGoroutines)
	remainder := totalNumbers % int64(numGoroutines)

	var wg sync.WaitGroup

	startTime := time.Now()

	start := RANGE_START
	for i := 0; i < numGoroutines; i++ {
		goroutineId := i + 1
		end := start + rangePerG - 1

		if i == numGoroutines-1 {
			end = end + remainder
		}

		rangeStart := start
		rangeEnd := end

		fmt.Printf("Goroutine %d: range [%d ... %d]\n", goroutineId, rangeStart, rangeEnd)

		wg.Add(1)
		go func(id int, rStart int64, rEnd int64) {
			defer wg.Done()

			tStart := time.Now()

			for n := rStart; n <= rEnd; n++ {
				fmt.Printf("[Goroutine-%d] %d\n", id, n)
			}

			elapsed := time.Since(tStart)
			fmt.Printf("Goroutine %d finished in %d ms\n", id, elapsed.Milliseconds())
		}(goroutineId, rangeStart, rangeEnd)

		start = end + 1
	}

	fmt.Println()
	fmt.Println("All goroutines started. Waiting...")

	wg.Wait()

	totalTime := time.Since(startTime)

	fmt.Println()
	fmt.Println("ALL GOROUTINES COMPLETED")
	fmt.Printf("Goroutines used: %d\n", numGoroutines)
	fmt.Printf("Total range:     %d -> %d\n", RANGE_START, RANGE_END)
	fmt.Printf("Total time:      %d ms\n", totalTime.Milliseconds())
	fmt.Printf("Total time:      %.2f seconds\n", totalTime.Seconds())
	fmt.Printf("Total time:      %.4f minutes\n", totalTime.Minutes())
}
