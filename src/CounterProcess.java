
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

public class CounterProcess extends SimProcess {

    private Restaurant_Model model;
    
    public CarProcess car = null; // the current car he is handling

    public CounterProcess(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (Restaurant_Model) owner;
    }
     
    public void lifeCycle() throws SuspendExecution {
        while (true) {
        	// wait for car arrival
        	if (model.queueCounter.isEmpty()) {
        		model.queueFreeCounters.insert(this);
        		passivate(); 
        	}
        	
        	car = model.queueCounter.removeFirst();
        	car.myCounterProcess = this; // set this as the counter for the car
    		sendTraceNote("Bestellung wird zubereitet.");		
    		
    		hold(new TimeSpan(model.getMakingTime())); // wait
    		
    		sendTraceNote("Bestellung wird uebergeben.");
    		car.activate(); // give car the order
    		
    		car = null;
    		
    		if (model.queueCounter.size() <= Restaurant_Model.MAX_COUNTER_QUEUE_SIZE - 1) {
    			if (model.orderProcess.busy && model.orderProcess.car.ordered) {
        			sendTraceNote("Tell car to move into next queue.");
        			model.orderProcess.car.activate();
    			}
    		}
    		
    		
        }
    }
}