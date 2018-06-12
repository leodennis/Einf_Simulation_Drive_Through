
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * Continuously generates new cars to come to the restaurant
 */
public class NewCarProcess extends SimProcess {

    // reference to model
    private Restaurant_Model model;

    /**
     * Contructor
     * @param owner Model
     * @param name Name of the Event
     * @param showInTrace if it should be displayed in trace
     */
    public NewCarProcess (Model owner, String name, boolean showInTrace) {
	   super(owner, name, showInTrace);
	   model = (Restaurant_Model) owner;
    }
    
    public void lifeCycle() throws SuspendExecution {
        while (true) {
            // wait until next car should arrive
            hold (new TimeSpan(model.getCarArrivalTime()));
     
            // neuen Kunden erzeugen
            CarProcess newCar = new CarProcess (model, "Kunde", true);
            
            // activate car process
            newCar.activateAfter(this);
        }
    }
}