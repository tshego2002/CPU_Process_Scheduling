/* M. M. Kuttel 2024 mkuttel@gmail.com
Edited by Tshegofatso Kgole
*/

package barScheduling;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class DrinkOrder implements Comparable<DrinkOrder>  {

    //DO NOT change the code below
    public enum Drink { 
        Beer("Beer", 10),
        Cider("Cider", 10),
        GinAndTonic("Gin and Tonic", 30),
        Martini("Martini", 50),
        Cosmopolitan("Cosmopolitan", 80),
        BloodyMary("Bloody Mary", 90),
        Margarita("Margarita", 100),
        Mojito("Mojito", 120),
        PinaColada("Pina Colada", 200),
        LongIslandIcedTea("Long Island Iced Tea", 300),
    	B52("B52", 500);
    	    	
        private final String name;
        private final int preparationTime; // in minutes
        

        Drink(String name, int preparationTime) {
            this.name = name;
            this.preparationTime = preparationTime;
        }

        public String getName() {
            return name;
        }

        public int getPreparationTime() {
            return preparationTime;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final Drink drink;
    private static final Random random = new Random();
    private int orderer;
    private AtomicBoolean orderComplete;
        //Constructor
        public DrinkOrder(int patron) {
            drink=getRandomDrink();
            orderComplete = new AtomicBoolean(false);
            orderer=patron;
        }

    
    public static Drink getRandomDrink() {
        Drink[] drinks = Drink.values();  // Get all enum constants
        int randomIndex = random.nextInt(drinks.length);  // Generate a random index
        return drinks[randomIndex];  // Return the randomly selected drink
    }

    //getting preparation times for drinks using their name
    public static int getPreparationTimeByName(String drinkName) {
        for (Drink drink : Drink.values()) {
            if (drink.getName().equalsIgnoreCase(drinkName)) {
                return drink.getPreparationTime();
            }
        }
        throw new IllegalArgumentException("Invalid drink name: " + drinkName);//throwing an error for an invalid drink name
    }
    

    public int getExecutionTime() {
        return drink.getPreparationTime();
    }
    
    //barman signals when order is done
    public synchronized void orderDone() {
    	orderComplete.set(true);
        this.notifyAll();
    }
    
    //patrons wait for their orders
    public synchronized void waitForOrder() throws InterruptedException {
    	while(!orderComplete.get()) {
    		this.wait();
    	}
    }
    
    @Override
    public String toString() {
        return Integer.toString(orderer) +": "+ drink.getName();
    }

    @Override
    public int compareTo(DrinkOrder order) {
        return Integer.compare(this.getExecutionTime(), order.getExecutionTime());
    }
}