import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeSpan;

public class Restaurant_Model extends Model {
	
	public static final int MAX_COUNTER_QUEUE_SIZE = 6;
	
	public static final double CAR_DRIVE_AWAY_TIME = 0.4;
	
	// random number generator for random car arrivals
	private ContDistExponential carArrivalTime;

    // returns a random number for a car arrival
    public double getCarArrivalTime() {
	   return carArrivalTime.sample();
    }
    
   // random number generator for the time it takes to order
 	private ContDistUniform orderTime;
 	
 	// returns a random number for the time it takes to order
     public double getOrderTime() {
 	   return orderTime.sample();
     }
	
    // random number generator for the time it takes to make the order
	private ContDistUniform makingTime;

    // returns a random number for the time it takes to male the order
    public double getMakingTime() {
        return makingTime.sample();
    }
    
	// Waiting queues
    public ProcessQueue<CarProcess> queueOrder;
    public ProcessQueue<CarProcess> queueCounter;
    
    // Order and Counter queues
    public OrderProcess orderProcess;
    public ProcessQueue<CounterProcess> queueFreeCounters;
    public int numCounters;
	

    // constructor
    public Restaurant_Model(Model owner, String name, boolean showInReport, boolean showIntrace) {
    	super(owner, name, showInReport, showIntrace);
    }

     // description of the model TODO: EDIT
    public String description() {
    	return "Schalter2_p Model (Prozess orientiert)l:" +
               "simuliert einen Bankschalter, wo ankommende Kunden zuerst in einer"+
               "Warteschlange eingereiht werden. Wenn der Schalter frei ist,"+
               "werden sie bedient.";
    }	


    /**
     * first events at initialization
     */
    public void doInitialSchedules() {
        // create process to create new cars
        NewCarProcess newCars = new NewCarProcess(this, "Autokreation", true);
        // start process
        newCars.activate(new TimeSpan(0.0));
        
        // initialize Orders and Counters
        orderProcess = new OrderProcess(this, "Schalter", true);
        CounterProcess counterProcess = new CounterProcess(this, "Ausgabe", true, false);
        queueFreeCounters.insert(counterProcess);
        numCounters = 1;
        counterProcess = new CounterProcess(this, "Ausgabe", true, true); //last parameter: take fastest orders first
        queueFreeCounters.insert(counterProcess);
        numCounters = 2;
    }


    /**
     * initialization of DESMO-J infrastructure
     */
    public void init() {
		
    	// times for car arrivals
    	carArrivalTime =  new ContDistExponential(this, "Ankunftszeitintervall", 1.0, true, true);	 // 1.5
    	carArrivalTime.setNonNegative(true);	// deactivate negative times

    	// times for ordering and making the order
    	orderTime = new ContDistUniform(this, "Bestellzeit", 0.33, 1.5, true, true); // 0:20 - 1:30 min
    	makingTime = new ContDistUniform(this, "Zubereitungszeiten", 0.75, 3.0, true, true); // 0:45 - 3:00 min
    	
    	/*
    	 * http://time.com/money/3478752/drive-thru-fast-food-fast-casual/
    	 * average drive-thru wait time hit 181 second
    	 */

    	// queues for cars
       	queueOrder = new ProcessQueue<CarProcess>(this, "Kunden Schalter WS", true, true);
       	queueCounter = new ProcessQueue<CarProcess>(this, "Kunden Ausgabe WS", true, true);
       	
       	// queues Counters
        queueFreeCounters = new ProcessQueue<CounterProcess>(this, "Freie Ausgaben WS", true, true);
	
    }
}