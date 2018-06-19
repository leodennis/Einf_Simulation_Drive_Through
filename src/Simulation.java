import java.awt.Window;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

public class Simulation {

	public static void main(String[] args) throws IOException {
		
		int numberExperiments = 1000;
		Long[] seeds = new Long[numberExperiments];
		Random random = new Random(); 
		for (int i = 0; i < seeds.length; i++) {
			seeds[i] = random.nextLong();
			// seeds[i] = (long) (Math.random()*Long.MAX_VALUE);
		}
		
		List<String> normalResults = new ArrayList<>();
		List<String> ticketResults = new ArrayList<>();
		
		int i = 0;
		Restaurant_Model.USE_TICKET_SYSTEM = false;
		while (i < numberExperiments) {
			CarProcess.missedCars = 0;
			CarProcess.servedCars = 0;
			long seed = seeds[i];
			// make new experiment
	    	Experiment driveThroughExperiment = 
	            new Experiment("DriveThrough-Prozess");
	    	
	    	driveThroughExperiment.setSeedGenerator(seed);
	    	
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
	        
	        if (checkRetry()) {
	        	System.out.println("Error, retrying...");
	        } else {
	        	if (CarProcess.servedCars > 0)
		        	normalResults.add(printResults() + "\t\t" + seed);
	        	System.out.println(i);
	        	i++;
	        }
	        
	        Window[] windows = JFrame.getWindows();
	        for (Window window : windows)
	        	window.dispose();
		}
		
		i = 0;
		Restaurant_Model.USE_TICKET_SYSTEM = true;
		while (i < numberExperiments) {
			CarProcess.missedCars = 0;
			CarProcess.servedCars = 0;
			long seed = seeds[i];
			// make new experiment
	    	Experiment driveThroughExperiment = 
	            new Experiment("DriveThrough-Prozess");
	    	
	    	driveThroughExperiment.setSeedGenerator(seed);
	    	
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
	        
	        if (checkRetry()) {
	        	System.out.println("Error, retrying...");
	        } else {
	        	if (CarProcess.servedCars > 0)
		        	ticketResults.add(printResults() + "\t\t" + seed);
	        	System.out.println(i);
	        	i++;
	        }
	        
	        Window[] windows = JFrame.getWindows();
	        for (Window window : windows)
	        	window.dispose();
		}
		
		FileWriter writer = new FileWriter("ticket.txt"); 
		for (String str: ticketResults) {
		  writer.write(str);
		  writer.write("\n");
		}
		writer.close();
		
		writer = new FileWriter("normal.txt"); 
		for (String str: normalResults) {
		  writer.write(str);
		  writer.write("\n");
		}
		writer.close();
		
		System.out.println("\nTicket System = false");
		System.out.println("Copy this into Excel:");
		for (String string : normalResults) {
			System.out.println(string);
		}
		
		System.out.println("\nTicket System = true");
		System.out.println("Copy this into Excel:");
		for (String string : ticketResults) {
			System.out.println(string);
		}
		

	}
	
	private static boolean checkRetry() {
		List<String> list = new ArrayList<>();

		// read all lines into list
		try (BufferedReader br = Files.newBufferedReader(Paths.get("DriveThrough-Prozess_error.html"))) {
			list = br.lines().collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String correct = "<HTML><HEAD>\n"
				+ "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">\n"
				+ "<META NAME=\"Author\" CONTENT=\"Tim Lechler\">\n"
				+ "<META NAME=\"GENERATOR\" CONTENT=\"DESMO-J 2.5.1e\">\n"
				+ "<TITLE>./DriveThrough-Prozess_error.html</TITLE></HEAD>\n"
				+ "<BODY TEXT=\"#000000\" BGCOLOR=\"#FFFFFF\" LINK=\"#0000EE\" VLINK=\"#551A8B\" ALINK=\"#FF0000\">\n"
				+ "<A NAME=\"top\"></A><br>\n"
				+ "<DIV align=center><H3>DriveThrough-Prozess - errors & warnings</H3></DIV>\n"
				+ "<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=3 WIDTH=\"100%\" >\n"
				+ "<TR VALIGN=TOP><TD><B><DIV align=left>model</DIV></B></TD><TD><B><DIV align=left>time</DIV></B></TD><TD><B><DIV align=left>error</DIV></B></TD><TD><B><DIV align=left>content</DIV></B></TD></TR>\n"
				+ "</TABLE><P>\n"
				+ "<FONT SIZE=-1><A HREF=#top>top</A></FONT><P>";
		
		return !String.join("\n", list.subList(0, list.size()-2)).equals(correct);
	}
	
	private static String printResults() {
		List<String> list = new ArrayList<>();

		// read all lines into list
		try (BufferedReader br = Files.newBufferedReader(Paths.get("DriveThrough-Prozess_report.html"))) {
			list = br.lines().collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Pattern p = Pattern.compile("<tr valign=\"top\"><td>.*</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td><td>(.*)</td></tr>");

		
		String missedCars = String.valueOf(CarProcess.missedCars);
		String servedCars = String.valueOf(CarProcess.servedCars);
		String qAvg_wsSchalter = "";
		String qAvg_wsAusgabe = "";
		String maxWait_wsSchalter = "";
		String maxWait_wsAusgabe = "";
		String avgWait_wsSchalter = "";
		String avgWait_wsAusgabe = "";
		
		for (String line : list) {
			if (line.trim().startsWith("<tr valign=\"top\"><td>Kunden Schalter WS")) { // WS_Schalter
				Matcher m = p.matcher(line);
				if (m.find()) {
					qAvg_wsSchalter = m.group(7).replace('.', ',');
					maxWait_wsSchalter = m.group(9).replace('.', ',');
					avgWait_wsSchalter = m.group(10).replace('.', ',');
				}
			} else if (line.trim().startsWith("<tr valign=\"top\"><td>Kunden Ausgabe WS")) { // WS_Ausgabe
				Matcher m = p.matcher(line);
				if (m.find()) {
					qAvg_wsAusgabe = m.group(7).replace('.', ',');
					maxWait_wsAusgabe = m.group(9).replace('.', ',');
					avgWait_wsAusgabe = m.group(10).replace('.', ',');
				}
				break;
			}
		}
		
		StringBuilder result =  new StringBuilder();
		result.append(missedCars).append("\t");
		result.append(servedCars).append("\t");
		result.append(qAvg_wsSchalter).append("\t");
		result.append(qAvg_wsAusgabe).append("\t").append("\t");
		result.append(maxWait_wsSchalter).append("\t");
		result.append(maxWait_wsAusgabe).append("\t").append("\t");
		result.append(avgWait_wsSchalter).append("\t");
		result.append(avgWait_wsAusgabe);
		return result.toString();
	}

}
