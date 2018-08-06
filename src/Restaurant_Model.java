import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeSpan;

/**
 * The core model of the simulation.
 */
public class Restaurant_Model extends Model {
	
	// this determines if the counters use the ticket system
	private static final boolean USE_TICKET_SYSTEM = true;
	
	// maximum size of queue at counter
	public static final int MAX_COUNTER_QUEUE_SIZE = 5;
	
	// maximum size of queue before driving away
	public static final int CAR_INSERT_MAX_QUEUE = 3; // equals 4 cars with the one who just ordered
		
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
    
    // random number generator for the time it takes for the customer to pay
   	private ContDistUniform payingTime;

       // returns a random number for the time it takes for the customer to pay
       public double getPayingTime() {
           return payingTime.sample();
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

     // description of the model
    public String description() {
    	return "Simulation of a Drive-Through. Cars arrive and wait until they can place an order. "
    			+ "If the queue is too long they will drive away without ordering. "
    			+ "After that they have to wait until they get their order. "
    			+ "2 Different strategies implemented: Normal and ticket system.";
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
        CounterProcess counterProcess = new CounterProcess(this, "Ausgabe", true, USE_TICKET_SYSTEM);
        queueFreeCounters.insert(counterProcess);
        numCounters = 1;
    }


    /**
     * initialization of DESMO-J infrastructure
     */
    public void init() {
		
    	// times for car arrivals
    	carArrivalTime =  new ContDistExponential(this, "Ankunftszeitintervall", 1.25, true, true);	 // 1.25
    	carArrivalTime.setNonNegative(true);	// deactivate negative times

    	// times for ordering and making the order
    	orderTime = new ContDistUniform(this, "Bestellzeit", 0.167, 0.75, true, true); // 0:10 - 0:45 min
    	makingTime = new ContDistUniform(this, "Zubereitungszeiten", 0.75, 8.0, true, true); // 0:45 - 8:00 min
    	payingTime = new ContDistUniform(this, "Bezahlzeit", 0.167, 0.417, true, true); // 0:10 - 0:25 min

    	// queues for cars
       	queueOrder = new ProcessQueue<CarProcess>(this, "Kunden Schalter WS", true, true);
       	queueCounter = new ProcessQueue<CarProcess>(this, "Kunden Ausgabe WS", true, true);
       	
       	// queues Counters
        queueFreeCounters = new ProcessQueue<CounterProcess>(this, "Freie Ausgaben WS", true, true);
    }
}