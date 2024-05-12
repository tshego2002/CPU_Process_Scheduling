/* M. M. Kuttel 2024 mkuttel@gmail.com
Edited by Tshegofatso Kgole
*/

package barScheduling;
// the main class, starts all threads


import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class SchedulingSimulation {
	static int noPatrons=100; //number of customers - default value if not provided on command line
	static int sched=0; //which scheduling algorithm, 0= FCFS 1= SJF 
			
	static CountDownLatch startSignal;

	
	static Patron[] patrons; // array for customer threads
	static Barman Andre;
	static FileWriter writer;

	public static void writeToFile(String data) throws IOException {
	    synchronized (writer) {
	    	writer.write(data);
	    }
	}

		// Method to calculate and record metrics
	public void calculateAndRecordMetrics(int patronID, long arrivalTime,long totalPrepTime, long orderCompletionTime, long responseTime) throws IOException {
			// Calculate metrics
			long turnaroundTime = orderCompletionTime ;
			long waitingTime = turnaroundTime - totalPrepTime;
	
			// Record metrics for the current patron
			String metricsData = String.format("%d, %d, %d, %d, %d, %d\n", patronID,arrivalTime,totalPrepTime, turnaroundTime, waitingTime, responseTime);
			writeToFile(metricsData);
		}

	public static void main(String[] args) throws InterruptedException, IOException {
				
		//deal with command line arguments if provided
		if (args.length==1) {
			noPatrons=Integer.parseInt(args[0]);  //total people to enter room
		} else if (args.length==2) {
			noPatrons=Integer.parseInt(args[0]);  //total people to enter room
			sched=Integer.parseInt(args[1]); 
		}
		
		writer = new FileWriter("turnaround_time_"+Integer.toString(sched)+".txt", false); //file has identifier 1 or 0, each algorithm has its own file where results are saved
		Patron.fileW=writer;

		startSignal= new CountDownLatch(noPatrons+2);//Barman and patrons and main method must be raeady
		
		//create barman
        Andre= new Barman(startSignal,sched); 
     	Andre.start();
  
	    //create all the patrons, who all need access to Andre
		patrons = new Patron[noPatrons];
		for (int i=0;i<noPatrons;i++) {
			patrons[i] = new Patron(i,startSignal,Andre);
			patrons[i].start();
		}
		
		System.out.println("------Andre the Barman Scheduling Simulation------");
		System.out.println("-------------- with "+ Integer.toString(noPatrons) + " patrons---------------");

      	startSignal.countDown(); //main method ready
		long barStartTime = System.currentTimeMillis();
      	
      	//wait till all patrons done, otherwise race condition on the file closing!
      	for (int i=0;i<noPatrons;i++) {
			patrons[i].join();
		}

    	System.out.println("------Waiting for Andre------");
    	Andre.interrupt();   //tell Andre to close up
    	Andre.join(); //wait till he has
		long barCloseTime = System.currentTimeMillis();
		double throughput = ((double)noPatrons)/(barCloseTime-barStartTime); //throughput of patrons
		writeToFile("Total Duration: " +(double)(barCloseTime-barStartTime) + "\n"); //Operation Duration is
		writeToFile("Number of Patrons: "+ noPatrons  + "\n"); //Number of patrons
		writeToFile("Throughput of Patrons: " + throughput  + "\n"); //Throughput of patrons/barTime is
		writeToFile("Total Drinks: " + Andre.drinkCount()  + "\n"); //The number of drinks served
		writeToFile("Throughput of Drinks: "+ (double)Andre.drinkCount()/(barCloseTime-barStartTime)); //Throughput of drinks:
      	writer.close(); //all done, can close file
      	System.out.println("------Bar closed------");
	}

}
