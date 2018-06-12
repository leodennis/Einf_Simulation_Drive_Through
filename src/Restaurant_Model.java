import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeSpan;

public class Restaurant_Model extends Model {
	
	public static final int MAX_COUNTER_QUEUE_SIZE = 3;
	
	public static final double CAR_DRIVE_AWAY_TIME = 0.5;
	
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
    public ProcessQueue<OrderProcess> queueFreeOrders;
    public ProcessQueue<CounterProcess> queueFreeCounters;
	

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
        OrderProcess orderProcess = new OrderProcess(this, "Schalter", true);
        queueFreeOrders.insert(orderProcess);
        CounterProcess counterProcess = new CounterProcess(this, "Ausgabe", true);
        queueFreeCounters.insert(counterProcess);
    }


    /**
     * initialization of DESMO-J infrastructure
     */
    public void init() {
		
    	// times for car arrivals
    	carArrivalTime =  new ContDistExponential(this, "Ankunftszeitintervall", 1.0, true, true);	
    	carArrivalTime.setNonNegative(true);	// deactivate negative times

    	// times for ordering and making the order
    	orderTime = new ContDistUniform(this, "Bestellzeit", 2.0, 8.0, true, true);	
    	makingTime = new ContDistUniform(this, "Zubereitungszeiten", 5.0, 15.0, true, true);

    	// queues for cars
       	queueOrder = new ProcessQueue<CarProcess>(this, "Kunden Schalter WS", true, true);
       	queueCounter = new ProcessQueue<CarProcess>(this, "Kunden Ausgabe WS", true, true);
       	
       	// queues for the Orders and Counters
       	queueFreeOrders = new ProcessQueue<OrderProcess>(this, "Freie Schalter WS", true, true);
        queueFreeCounters = new ProcessQueue<CounterProcess>(this, "Freie Ausgaben WS", true, true);
	
    }
}