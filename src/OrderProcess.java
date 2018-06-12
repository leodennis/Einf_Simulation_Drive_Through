
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

public class OrderProcess extends SimProcess {

    private Restaurant_Model model;

    public OrderProcess(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        model = (Restaurant_Model) owner;
    }
     
    public void lifeCycle() throws SuspendExecution {
        while (true) {
        	// wait for car arrival
        	if (model.queueOrder.isEmpty()) {
        		model.queueFreeOrders.insert(this);
        		passivate(); 
        	}
        	
    		CarProcess car = model.queueOrder.removeFirst();
    		car.myOrderProcess = this; // set this as the process where it ordered
    		sendTraceNote("Bestellung wird angenommen.");
    		car.activate();
    		
    		// we do not need to wait because car is waiting
    		
    		passivate(); // wait for car to drive away
    		
    		if (!model.queueOrder.isEmpty()) {
    			model.queueOrder.first().activate(); // tell car to drive forward
    		}
        	
        }
    }
}