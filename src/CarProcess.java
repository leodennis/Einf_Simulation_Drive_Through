
import desmoj.core.exception.DelayedInterruptException;
import desmoj.core.exception.InterruptException;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * Car process representing a car which wants to order at the restaurant.
 */
public class CarProcess extends SimProcess {
	
	// maximum size of queue before driving away
	private final int CAR_INSERT_MAX_QUEUE = 3; // equals 4 cars with the one who just ordered

    // reference to model
    private Restaurant_Model restaurantModell;
    
    // order process where he ordered
    public OrderProcess myOrderProcess = null;
    public CounterProcess myCounterProcess = null;
    
    boolean ordered = false;
    boolean orderMade = false;
    boolean finished = false;
        
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
		hold(new TimeSpan(restaurantModell.getOrderTime())); // wait ordering
        sendTraceNote("Kunde hat Bestellung aufgegeben.");
        ordered = true;
        
    	if (restaurantModell.queueCounter.size() >= Restaurant_Model.MAX_COUNTER_QUEUE_SIZE - (restaurantModell.numCounters - restaurantModell.queueFreeCounters.size())) {
    		passivate(); // wait to move into next queue
    	}
    	
    	myOrderProcess.activate(); // tell order process that he is leaving
    	restaurantModell.queueCounter.insert(this);
    	
    	hold(new TimeSpan(restaurantModell.getMakingTime())); // wait making
    	orderMade = true;
    	
		if (!restaurantModell.queueFreeCounters.isEmpty()) {
			if (restaurantModell.queueCounter.first() == this) {
				// first in line, pay now
				myCounterProcess = restaurantModell.queueFreeCounters.removeFirst();
				myCounterProcess.activate(); // prepare for taking order
			} else {
				// lock for counter with ticket system
				for (CounterProcess counter : restaurantModell.queueFreeCounters) {
					if (counter.ticketSystem) {
						myCounterProcess = restaurantModell.queueFreeCounters.removeFirst();
						myCounterProcess.activate(); // prepare for taking order
						System.out.println(1);
						break;
					}
				}
			}
		}
		
		sendTraceNote("Kunde wartet auf Bestellung.");
		
		passivate(); // wait to pay order
		
		finished = true;
		sendTraceNote("Kunde faehrt weg.");
		servedCars++;
    }
    
    public void traceGotOrder() {
    	sendMessage(new TraceNote(currentModel(), "Kunde hat bestellung angenommen.",
				presentTime(), this, currentEvent()));
    }
}
