
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

public class CounterProcess extends SimProcess {

    private Restaurant_Model model;
    
    public CarProcess car = null; // the current car he is handling
    public boolean takeFastestOrder; // if the counter should just take the fastest order

    public CounterProcess(Model owner, String name, boolean showInTrace, boolean takeFastestOrder) {
        super(owner, name, showInTrace);
        model = (Restaurant_Model) owner;
        this.takeFastestOrder = takeFastestOrder;
    }
     
    public void lifeCycle() throws SuspendExecution {
        while (true) {
        	// wait for car arrival
        	if (model.queueCounter.isEmpty()) {
        		model.queueFreeCounters.insert(this);
        		passivate(); 
        	}
        	
        	if (takeFastestOrder) {
        		car = removeFastest(model.queueCounter);
        	} else {
        		car = model.queueCounter.removeFirst();
        	}
        	
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
    
    private CarProcess removeFastest(ProcessQueue<CarProcess> queue) {
    	int fastest = 0;
    	double time = Double.MAX_VALUE;
    	
    	for (int i = 0; i < queue.size(); i++) {
    		CarProcess car = queue.get(i);
    		if (time >  car.orderTime) {
    			time = car.orderTime;
    			fastest = i;
    		}
    	}
    	
    	System.out.println(fastest);
    	
    	CarProcess car = queue.get(fastest);
    	queue.remove(fastest);
    	return car;
    }
}