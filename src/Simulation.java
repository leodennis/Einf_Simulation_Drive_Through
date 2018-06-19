import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

public class Simulation {

	public static void main(String[] args) {
		
		// make new experiment
    	Experiment driveThroughExperiment = 
            new Experiment("DriveThrough-Prozess");
    	
    	driveThroughExperiment.setSeedGenerator(3880357535495099392L);
    	
    	/*
    	 * Seeds:
    	 * 3880357535495099392L
    	 * 6741471850034513920L
    	 * 3555580573866427392L
    	 * 8918028939976887296L
    	 * 8632441937680918528L
    	 * 8677389338550630400L
    	 * 3662174629164061696L
    	 * 2346572093450352640L
    	 * 4494830338938596352L
    	 * 5209892568371836928L
    	 * 1135130464736532480L
    	 * 4825576600361176064L
    	 * 6902637080331287552L
    	 * 3359010864524672000L
    	 * 8894889103895458816L
    	 * 5355814256263986176L
    	 * 8217049754619106304L
    	 * 7524957960017851392L
    	 * 1432210239003411456L
    	 * 6032465730557075456L
    	 * 6730498136540893184L
    	 * 5073050717860599808L
    	 * 7925075745730707456L
    	 * 2438869374603970560L
    	 * 8144562481300359168L
    	 */
    	
    	for (int i = 0; i < 25; i++) {
    		System.out.println((long) (Math.random()*Long.MAX_VALUE));
		}
    	
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
