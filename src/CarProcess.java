
import desmoj.core.exception.DelayedInterruptException;
import desmoj.core.exception.InterruptException;
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * Car process representing a car which wants to order at the restaurant.
 */
public class CarProcess extends SimProcess {
	
	// maximum size of queue before driving away
	private final int CAR_INSERT_MAX_QUEUE = 3; // equals 6 cars with the one who just ordered

    // reference to model
    private Restaurant_Model restaurantModell;
    
    // order process where he ordered
    public OrderProcess myOrderProcess = null;
    public CounterProcess myCounterProcess = null;
    
    boolean ordered = false;
    boolean finished = false;
    
    double orderTime;
    
    public static int missedCars = 0; // the number of cars who drove away without ordering
    public static int servedCars = 0; // the number of cars who got their order



    /**
     * Constructor
     * @param owner the model
     * @param name the name of the process
     * @param showInTrace if it should be displayed in trace
     */
    public CarProcess(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        restaurantModell = (Restaurant_Model) owner;
        orderTime = restaurantModell.getOrderTime();
    }

    public void lifeCycle() throws SuspendExecution{
    	if (restaurantModell.queueOrder.size() > CAR_INSERT_MAX_QUEUE) {
    		sendTraceNote("Schlange zu lang, Kunde faehrt weg.");
    		missedCars++;
    		return;
    	}
    	
    	restaurantModell.queueOrder.insert(this);
    	
    	if (restaurantModell.queueOrder.size() > 1 || restaurantModell.orderProcess.busy) {
    		passivate(); // wait to order
    	} else {
    		myOrderProcess = restaurantModell.orderProcess;
    		myOrderProcess.activate(); // prepare for ordering
    	}
    	
    	order();
    }
    
    public void order() throws DelayedInterruptException, InterruptException, SuspendExecution {
    	// order
		hold(new TimeSpan(orderTime)); // wait
        sendTraceNote("Kunde hat Bestellung aufgegeben.");
        ordered = true;
        
    	if (restaurantModell.queueCounter.size() >= Restaurant_Model.MAX_COUNTER_QUEUE_SIZE - (restaurantModell.numCounters - restaurantModell.queueFreeCounters.size())) {
    		passivate(); // wait to move into next queue
    	}
    	
    	myOrderProcess.activate(); // tell order process that he is leaving
    	restaurantModell.queueCounter.insert(this);
    	    	
		if (restaurantModell.queueCounter.size() == 1 && !restaurantModell.queueFreeCounters.isEmpty()) {
			myCounterProcess = restaurantModell.queueFreeCounters.removeFirst();
			myCounterProcess.activate(); // prepare for taking order
		}
		
		passivate(); // wait to take order
		sendTraceNote("Kunde hat Bestellung angenommen.");
		finished = true;
		hold(new TimeSpan(Restaurant_Model.CAR_DRIVE_AWAY_TIME));
		sendTraceNote("Kunde faehrt weg.");
		servedCars++;
    }
}
