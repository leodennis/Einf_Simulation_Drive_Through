
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * Process of a car ordering.
 */
public class OrderProcess extends SimProcess {

	// reference to model
    private Restaurant_Model model;
    
    public CarProcess car = null; // the current car he is handling
    public boolean busy = false;  // if the process is handling a customer currently

    public OrderProcess(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (Restaurant_Model) owner;
    }
     
    public void lifeCycle() throws SuspendExecution {
        while (true) {
        	// wait for car arrival
        	if (model.queueOrder.isEmpty()) {
        		busy  = false;
        		passivate(); 
        	}
        	busy = true;
        	
    		car = model.queueOrder.removeFirst();
    		sendTraceNote("Bestellung wird angenommen.");
    		if (car.myOrderProcess == null) {
        		car.myOrderProcess = this; // set this as the process where it ordered
        		car.activate();
    		}
    		
    		
    		// we do not need to wait because car is waiting
    		
    		passivate(); // wait for car to drive away
    		car = null; // finished with car
        }
    }
}