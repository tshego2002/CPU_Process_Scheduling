//M. M. Kuttel 2024 mkuttel@gmail.com
//edited by Tshegofatso Kgole
package barScheduling;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/*
 This is the basicclass, representing the patrons at the bar
 */

public class Patron extends Thread {
	
	private Random random = new Random();// for variation in Patron behaviour

	private CountDownLatch startSignal; //all start at once, actually shared
	private Barman theBarman; //the Barman is actually shared though

	private int ID; //thread ID 
	private int lengthOfOrder;
	private long startTime, endTime; //for all the metrics
	
	public static FileWriter fileW;
	private DrinkOrder [] drinksOrder;

	
	Patron( int ID,  CountDownLatch startSignal, Barman aBarman) {
		this.ID=ID;
		this.startSignal=startSignal;
		this.theBarman=aBarman;
		this.lengthOfOrder=random.nextInt(5)+1;//between 1 and 5 drinks
		drinksOrder=new DrinkOrder[lengthOfOrder];
	}
	
	public  void writeToFile(String data) throws IOException {
	    synchronized (fileW) {
	    	fileW.write(data);
	    }
	}
	
	
	public void run() {
		try {
			//Do NOT change the block of code below - this is the arrival times
			startSignal.countDown(); //this patron is ready
			startSignal.await(); //wait till everyone is ready
	        int arrivalTime = random.nextInt(300)+ID*100;  // patrons arrive gradually later
	        sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
			System.out.println("thirsty Patron "+ this.ID +" arrived");
			//END do not change

			int firstDrinkServedTime = Integer.MAX_VALUE; // Initialize with maximum value
			long firstDrinkReceivedTime = 1; // Initialize to 1 indicating no drinks received yet
			int totalDrinksTime = 0;			

	        //create drinks order
	        for(int i=0;i<lengthOfOrder;i++) {
	        	drinksOrder[i]=new DrinkOrder(this.ID);
				firstDrinkServedTime = Math.min(firstDrinkServedTime, drinksOrder[i].getExecutionTime());	        	
	        }


			System.out.println("Patron "+ this.ID + " submitting order of " + lengthOfOrder +" drinks"); //output in standard format  - do not change this
	        startTime = System.currentTimeMillis();//started placing orders

			for(int i=0;i<lengthOfOrder;i++) {
				System.out.println("Order placed by " + drinksOrder[i].toString());
				// Calculate preparation time for the current drink order
				String[] parts = drinksOrder[i].toString().split(":");
				int drinkPrepTime = DrinkOrder.getPreparationTimeByName(parts[1].trim());
				
				// Update total drinks time
				totalDrinksTime += drinkPrepTime;
				theBarman.placeDrinkOrder(drinksOrder[i]);
			}

			for(int i=0;i<lengthOfOrder;i++) {
				drinksOrder[i].waitForOrder();
				// Check if the first drink has been received yet by comparing expected prep time
				if(firstDrinkReceivedTime == 1) { //check if the first drink still has not yet arrived.
					if (drinksOrder[i].getExecutionTime() == firstDrinkServedTime) {
						firstDrinkReceivedTime = System.currentTimeMillis();
					}
				}
			}

			endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
	
			SchedulingSimulation schedulingSimulation = new SchedulingSimulation();
			schedulingSimulation.calculateAndRecordMetrics(this.ID, arrivalTime, totalDrinksTime, totalTime, (firstDrinkReceivedTime-startTime));
			//writeToFile( String.format("%d,%d,%d\n",ID,arrivalTime,totalTime));
			System.out.println("Patron "+ this.ID + " got order in " + totalTime);
			
			
		} catch (InterruptedException e1) {  //do nothing
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
}
}
	

