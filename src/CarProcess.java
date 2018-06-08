
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

// stellt die Kundenaktivitaeten als Prozess dar
public class CarProcess extends SimProcess {

    // nuetzliche Referenz auf entsprechendes Modell
    private Schalter_Model order;
    private Schalter_Model output;

    // Konstruktor
	  // Par 1: Modellzugehoerigkeit
	  // Par 2: Name des Ereignisses
	  // Par 3: show in trace?
    public CarProcess(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);

        order = (Schalter_Model) owner;
        output = (Schalter_Model) owner;
    }

    
    // Beschreibung der Aktionen des Kunden vom Eintreffen bis zum Verlassen
    //   des Schalters 
    public void lifeCycle() throws SuspendExecution{
    	// TODO: Fertig machen
    	
    	CarProcess process = new CarProcess(order, "Car", true);
		order.queueOrder.insert(process);
    	
    	if(order.queueOrder.size() == 1) {
    		order.init();
    		order();
    	}
    }
    
    public void order() {
    	order.order();
    	
        sendTraceNote("Kunde hat Bestellung aufgegeben.");
        
    	if(output.queueOutput.size() <= 2) {
    		output.queueOutput.insert(new CarProcess(order, "Car", true));
    		if(output.queueOutput.size() == 1) {
    			output.init();
    			takeOrder();
    		}
    	}
    }
    
    public void takeOrder() {
    	order.takeOrder();

        sendTraceNote("Kunde hat Bestellung angenommen und fährt weg.");
    }
}
