import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;

public class Schalter_Model extends Model {
	
	// random number generator for random car arrivals
	private ContDistExponential carArrivalTime;

    // returns a random number for a car arrival
    public double getCarArrivalTime() {
	   return carArrivalTime.sample();
    }
	
    // random number generator for the time it takes to produce the order
	private ContDistUniform orderTime;

    // returns a random number for the order time
    public double getOrderTime() {
        return orderTime.sample();
    }
    
	// Waiting queues
    public ProcessQueue<CarProcess> queueOrder;
    public ProcessQueue<CarProcess> queueOutput;
	

    // constructor
    public Schalter_Model(Model owner, String name, boolean showInReport, boolean showIntrace) {
    	super(owner, name, showInReport, showIntrace);
    }

     // description of the model TODO: EDIT
    public String description() {
    	return "Schalter2_p Model (Prozess orientiert)l:" +
               "simuliert einen Bankschalter, wo ankommende Kunden zuerst in einer"+
               "Warteschlange eingereiht werden. Wenn der Schalter frei ist,"+
               "werden sie bedient.";
    }	


    // first events at initialization
    public void doInitialSchedules() {

    	/*
        // Prozess zur Erzeugung von Kunden einrichten
        NeuerKundeProcess neuerKunde = 
            new NeuerKundeProcess(this, "Kundenkreation", true);
        // Prozess starten
        neuerKunde.activate(new TimeSpan(0.0));
                 
        // Schalter einrichten
        SchalterProcess schalter = new SchalterProcess(this, "Schalter", true);
        // Schalterprozess starten (= "Schalter wird eroeffnet")
        schalter.activate(new TimeSpan(0.0));
        */
    }


    // initialization of DESMO-J infrastructure
    public void init() {
		
    	// Generator fuer Ankunftszeiten initialisieren
    	// Par 1: Modellzugehoerigkeit
    	// Par 2: Name des Generators
    	// Par 3: mittlere Zeitdauer in Minuten zwischen Kundenankuenften
    	// Par 4: show in report?
    	// Par 5: show in trace?
    	carArrivalTime =  new ContDistExponential(this, "Ankunftszeitintervall", 3.0, true, true);	

    	// negative Ankunftszeitintervalle sind nicht moeglich, 
    	// jedoch liefert Exponentialverteilung auch negative Werte, daher
    	carArrivalTime.setNonNegative(true);

    	// Generator fuer Bedienzeiten initialisieren
    	// Par 1: Modellzugehoerigkeit
    	// Par 2: Name des Generators
    	// Par 3: minimale Bedienzeit in Minuten
    	// Par 4: maximale Bedienzeit in Minuten
    	// Par 5: show in report?
    	// Par 6: show in trace?
    	orderTime = new ContDistUniform(this, "Bedienzeiten", 0.5, 10.0, true, true);	

    	// Warteschlange fuer Kunden initialisieren
    	// Par 1: Modellzugehoerigkeit
    	// Par 2: Name der Warteschlange
    	// Par 3: show in report?
    	// Par 4: show in trace?
       	queueOrder = new ProcessQueue<CarProcess>(this, "Kunden-Warteschlange",true, true);	
       	queueOutput = new ProcessQueue<CarProcess>(this, "Kunden-Warteschlange",true, true);	
	
    }

	public void order() {
		// TODO: Implement
	}

	public void readyToTakeOrder() {
		// TODO: Implement
	}
}