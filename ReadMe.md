
# Tshegofatso Kgole
# CSC3002F
# OS Assignment 2 README

# Description
This program simulates a CPU scheduler using a busy bar analogy. Jobs are represented by drinks numbered 1 to 5, each with different preparation times. These "orders" are submitted to Andre, who prepares and fills them. The program implements two scheduling algorithms: First-Come-First-Served (FCFS) and Shortest Job First (SJF).

# How to run file
1. You can use an ide or terminal
2. You have to use the Makefile to run the program
3. By Default the program running is FCFS
4. To change between FCFS and SJF change the sched value in SchedulingSimulation.Java(O=FCFS, 1=SJF)
5. First Compile the program with make(run the command in the project directory on the terminal)
6. Use make run to run the simulation
6. The program will create a new file named turnaround_time_0 if you are running FCFS and turnaround_time_1 if you are running SJF


# Input Format
This program can run with input parameters but you can also run it with command-line arguments

# Output Format

The program will output one the patron IDs served in the order they were served and for every patron it will also output arrival time,preparation time, turnaround time, waiting time and response time, after the last patron is served it will include at the end of the file, the total duration of time it took to serve all drinks, the number of patrons served and their throughput, the number of drinks and the throughput