import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

public class Simulation {

	public static void main(String[] args) {
		
		// make new experiment
    	Experiment driveThroughExperiment = 
            new Experiment("DriveThrough-Prozess");
 
    	
        // create new model
        // Par 1: null markiert main model, sonst Mastermodell angeben
        Restaurant_Model model = new Restaurant_Model(null, "Ausgabe Modell", true, true);  

        // connect model with experiment
        model.connectToExperiment(driveThroughExperiment);
        

        // interval for trace/debug
        driveThroughExperiment.tracePeriod(new TimeInstant(0.0), new TimeInstant(100));
        driveThroughExperiment.debugPeriod(new TimeInstant(0.0), new TimeInstant(100));

        // set end of simulation
        // -> here: 4h (= 240 min)
        driveThroughExperiment.stop(new TimeInstant(240));

        // start experiment at time 0.0
        driveThroughExperiment.start(); 


        // generate report
        driveThroughExperiment.report();

        // finish
        driveThroughExperiment.finish();

	}

}
