
import desmoj.core.exception.DelayedInterruptException;
import desmoj.core.exception.InterruptException;
import desmoj.core.simulator.*;
import co.paralleluniverse.fibers.SuspendExecution;

// stellt die Kundenaktivitaeten als Prozess dar
public class CarProcess extends SimProcess {
	
	private final int CAR_INSERT_MAX_QUEUE = 5;
	private final TimeSpan ORDER_TIME = new TimeSpan(1);

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
    	
    	// Wahrscheinlich muessen wir schalter und ausgabe model trennen
    	
    	if (order.queueOrder.size() > CAR_INSERT_MAX_QUEUE) {
    		sendTraceNote("Schlange zu lang, Kunde faehrt weg.");
    		return;
    	}
    	
    	if (order.queueOrder.size() > 1) {
    		order.queueOrder.insert(this);
    		passivate();
    	} else {
    		order();
    	}
    }
    
    public void order() throws DelayedInterruptException, InterruptException, SuspendExecution {
    	order.order();
    	
    	hold(ORDER_TIME);
    	
        sendTraceNote("Kunde hat Bestellung aufgegeben.");
        
    	if (output.queueOutput.size() <= 2) {
    		order.queueOrder.remove(this);
    		output.queueOutput.insert(this);
    		if (output.queueOutput.size() > 1) {
    			passivate();
    		} else {
    			output.readyToTakeOrder();
    			passivate();
    		}
    	} else {
    		passivate();
    	}
    }
    
    public void takeOrder() {
        sendTraceNote("Kunde hat Bestellung angenommen und faehrt weg.");
    }
}
