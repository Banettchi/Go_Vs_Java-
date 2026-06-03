# Java vs Go — Multithreading Comparison

## What we did
We ran a practical test to see which of the two languages is faster at counting numbers.
The main goal is for the program to count from one to fifty million without skipping anything.
It also has to print every number on screen to prove that it is actually doing the work.
The interesting part is that the user gets to choose how many threads to use.
At the end the main program shows the exact times each one took so we can compare them properly.

## How we split the work
To make the test fair we divided the total range of numbers between the threads the user asked for.
This way the computer does not do the same work twice and does not get confused.

| Step | How we solved it |
| :--- | :--- |
| **Splitting the numbers** | Each thread gets a start number and an end number so they each work on their own section |
| **Leftover numbers** | If the division is not exact the remaining numbers are all assigned to the last thread |
| **Measuring time** | There is a main program that runs Java first and then Go to measure both under the exact same conditions |

## How each language handles threads
Each language has its own internal way of creating threads under the hood.
Even though both achieve the same result the amount of effort the computer has to make is very different.

| Language | How it creates threads | How it knows all threads finished |
| :--- | :--- | :--- |
| **Java** | Creates full threads that consume a lot more memory and resources on the computer | The main program uses a special command to stop and wait until every thread reports back that it finished |
| **Go** | Uses very fast virtual functions that barely use any memory on the computer | Uses an internal counter that goes down on its own as each thread completes its part of the work |

## What happens when we increase the number of threads
This is where the real difference between the two tools becomes clear.
When we push the machine to extreme limits we see which one is better built to stay fast.

| Number of threads | What happens with Java | What happens with Go |
| :--- | :--- | :--- |
| **Very few from 1 to 10** | Works great because the computer has plenty of space to handle all of them at the same time | Works very fast and completes all the work without any trouble |
| **A lot from 10000 to 50000** | Gets much slower because it takes too much effort to organize that many processes at the same time | Still works great because its threads are very lightweight and do not overwhelm the system |
| **An extreme case of 1 million** | Takes a huge amount of effort and over an hour to finish but somehow still manages to complete the count without failing | Takes some time but moves much faster and finishes the entire task in under an hour showing much better performance |

## The screen problem
There is one important detail that slows both programs down equally.
Forcing the computer to print fifty million lines of text on the console makes everything take longer.
The screen is much slower than the internal processor of the computer
and a big part of the time the program takes is just waiting for the screen to catch up and display all the numbers.
Even so when we run tests with millions of threads it is very clear that Go handles the pressure much better without slowing down as much.

## Real Test Results
To prove everything above we ran actual tests in the terminal.
The idea was to compare performance using a single thread all the way up to massive loads of one hundred thousand and one million threads.

| Test | Results | Conclusion |
| :--- | :--- | :--- |
| **Test with 1 thread** | Java took 2087 seconds and Go took 1964 seconds | With a single thread the difference is very small with Go being only about six percent faster |
| **Test with 100000 threads** | Java struggled and took 26116 seconds while Go only took 2100 seconds | With one hundred thousand threads Java ended up taking seven hours while Go finished in half an hour making it ninety two percent faster |
| **Extreme test with 1000000 threads** | Java finished in 4060 seconds and Go finished in 2821 seconds | Even at one million threads both programs managed to hold up and complete the task with Go finishing thirty percent faster than Java |

![Test with 1 Thread](images/1Hilo.png)
![Test with 100 thousand Threads](images/100kHilos.png)
![Test with 1 million Threads](images/1MHilos.png)
