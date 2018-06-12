
import desmoj.core.exception.DelayedInterruptException;
import desmoj.core.exception.InterruptException;
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * Car process representing a car which wants to order at the restaurant.
 */
public class CarProcess extends SimProcess {
	
	// maximum size of queue before driving away
	private final int CAR_INSERT_MAX_QUEUE = 5;

    // reference to model
    private Restaurant_Model restaurantModell;
    
    // order process where he ordered
    public OrderProcess myOrderProcess = null;
    public CounterProcess myCounterProcess = null;

    /**
     * Constructor
     * @param owner the model
     * @param name the name of the process
     * @param showInTrace if it should be displayed in trace
     */
    public CarProcess(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        restaurantModell = (Restaurant_Model) owner;
    }

    public void lifeCycle() throws SuspendExecution{
    	if (restaurantModell.queueOrder.size() > CAR_INSERT_MAX_QUEUE) {
    		sendTraceNote("Schlange zu lang, Kunde faehrt weg.");
    		return;
    	}
    	
    	restaurantModell.queueOrder.insert(this);
    	
    	if (restaurantModell.queueOrder.size() > 1 || restaurantModell.queueFreeOrders.isEmpty()) {
    		passivate(); // wait to order
    	} else {
    		myOrderProcess = restaurantModell.queueFreeOrders.removeFirst();
    		myOrderProcess.activate(); // prepare for ordering
    	}
    	
    	order();
    }
    
    public void order() throws DelayedInterruptException, InterruptException, SuspendExecution {
    	// order
		hold(new TimeSpan(restaurantModell.getOrderTime())); // wait
        sendTraceNote("Kunde hat Bestellung aufgegeben.");
        
    	if (restaurantModell.queueCounter.size() == Restaurant_Model.MAX_COUNTER_QUEUE_SIZE) {
    		passivate(); // wait to move into next queue
    	}
    	
    	myOrderProcess.activate(); // tell order process that he is leaving
    	restaurantModell.queueCounter.insert(this);
    	    	
		if (restaurantModell.queueCounter.size() == 1 && !restaurantModell.queueFreeCounters.isEmpty()) {
			myCounterProcess = restaurantModell.queueFreeCounters.removeFirst();
			myCounterProcess.activate(); // prepare for taking order
		}
		
		passivate(); // wait to take order
		hold(new TimeInstant(Restaurant_Model.CAR_DRIVE_AWAY_TIME));
		sendTraceNote("Kunde hat Bestellung angenommen und faehrt weg.");
    }
}
