import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

public class Simulation {

	public static void main(String[] args) {
		
		// make new experiment
    	Experiment driveThroughExperiment = 
            new Experiment("DriveThrough-Prozess");
    	
    	driveThroughExperiment.setSeedGenerator((long) (Math.random()*Long.MAX_VALUE));
 
    	
        // create new model
        // Par 1: null markiert main model, sonst Mastermodell angeben
        Restaurant_Model model = new Restaurant_Model(null, "Ausgabe Modell", true, true);  

        // connect model with experiment
        model.connectToExperiment(driveThroughExperiment);

        // interval for trace/debug
        driveThroughExperiment.tracePeriod(new TimeInstant(0.0), new TimeInstant(100));
        driveThroughExperiment.debugPeriod(new TimeInstant(0.0), new TimeInstant(100));

        // set end of simulation
        // -> here: 18h (= 1080 min)
        driveThroughExperiment.stop(new TimeInstant(1080));

        // start experiment at time 0.0
        driveThroughExperiment.start(); 

        // generate report
        driveThroughExperiment.report();

        // finish
        driveThroughExperiment.finish();
        
        System.out.println("Misses cars: " + CarProcess.missedCars);
        System.out.println("Served cars: " + CarProcess.servedCars);

	}

}
