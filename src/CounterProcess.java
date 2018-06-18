
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

public class CounterProcess extends SimProcess {

    private Restaurant_Model model;
    
    public CarProcess car = null; // the current car he is handling
    public boolean ticketSystem; // if the counter works with a ticket system

    public CounterProcess(Model owner, String name, boolean showInTrace, boolean ticketSystem) {
        super(owner, name, showInTrace);
        model = (Restaurant_Model) owner;
        this.ticketSystem = ticketSystem;
    }
     
    public void lifeCycle() throws SuspendExecution {
        while (true) {
        	// wait for car arrival
        	if (model.queueCounter.isEmpty() || (!ticketSystem && !model.queueCounter.first().orderMade) || (ticketSystem && getOrderFinished(model.queueCounter) < 0)) {
        		model.queueFreeCounters.insert(this);
        		passivate(); 
        	}
        	
        	if (ticketSystem) {
        		car = removeOrderFinished(model.queueCounter);
        	} else {
        		car = model.queueCounter.removeFirst();
        	}
        	
        	car.myCounterProcess = this; // set this as the counter for the car
    		sendTraceNote("Es wird bezahlt.");
    		
    		car.traceGotOrder();
    		
    		hold(new TimeSpan(model.getPayingTime()));
    		    		
    		sendTraceNote("Bestellung ist uebergeben und wurde bezahlt.");

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
    
    private CarProcess removeOrderFinished(ProcessQueue<CarProcess> queue) {
    	int index = getOrderFinished(queue);
    	
    	if (index < 0) {
    		System.err.println("Invalid index in CarProcess::removeOrderFinished");
    		return null;
    	}
    	
    	CarProcess car = queue.get(index);
    	queue.remove(index);
    	return car;
    }
    
    private int getOrderFinished(ProcessQueue<CarProcess> queue) {   	
    	for (int i = 0; i < queue.size(); i++) {
    		CarProcess car = queue.get(i);
    		if (car.orderMade) {
    			return i;
    		}
    	}
    	return -1;
    }
}