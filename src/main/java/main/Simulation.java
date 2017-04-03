package main;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class Simulation {
	private static String directory = "src/main/resources/";

	public static void main(String args[]) {
		String inputFile = directory + "data_file.csv";
		PriorityQueue<Event> futureEventList = new PriorityQueue<Event>();
		int channelsAvailable[][] = new int[21][2];

		/*
		 * Properties for plotting
		 */
		XYSeriesCollection dataset = new XYSeriesCollection();
	    XYSeries series1 = new XYSeries("Call Dropped Percentage");
	    XYSeries series2 = new XYSeries("Call Blocked Percentage");
		
	    
	    List<Double> dropped = new ArrayList<Double>();
	    List<Double> blocked = new ArrayList<Double>();
	    
		for(int iterations = 0;iterations<1000;iterations++)
		{
		for (int i = 0; i < 21; i++) {
			channelsAvailable[i][0] = 10;
			channelsAvailable[i][1] = 0;
		}
		int callDropped = 0;
		int callCompleted = 0;
		int callHandover = 0;
		int callBlocked = 0;

		Random rand = new Random();

		List<Event> startEvents = generateCallStartEvents(inputFile);
		for (int i = 0; i < 10000; i++)
			futureEventList.add(startEvents.get(i));
		
		int numCalls = 0;
		while (futureEventList.size() > 0) {
			Event currentEvent = futureEventList.poll();
			if (currentEvent.getType() == EventType.CALL_START_EVENT) {
				
				try{
					dropped.set(numCalls, dropped.get(numCalls)+(double)callDropped*100);
					blocked.set(numCalls, blocked.get(numCalls)+(double)callBlocked*100);
					}
					catch (java.lang.IndexOutOfBoundsException e){
						dropped.add(numCalls, (double)callDropped*100);
						blocked.add(numCalls, (double)callBlocked*100);
					}
					numCalls++;
					
				
				if (channelsAvailable[currentEvent.getCallData().getStation()][0] > 0) {
					channelsAvailable[currentEvent.getCallData().getStation()][0]--;
					double position = 2 * rand.nextDouble();
					double timeRemaining = ((2 - position) / currentEvent.getCallData().getVelocity()) * 3600;

					if (timeRemaining >= currentEvent.getCallData().getCallDuration()) {
						CallData data = currentEvent.getCallData();
						Event nextEvent = new Event(
								currentEvent.getTime() + currentEvent.getCallData().getCallDuration(),
								EventType.CALL_TERMINATION_EVENT, data);
						futureEventList.add(nextEvent);
					} else if (currentEvent.getCallData().getStation() == 20) {
						CallData data = currentEvent.getCallData();
						Event nextEvent = new Event(currentEvent.getTime() + timeRemaining,
								EventType.CALL_TERMINATION_EVENT, data);
						futureEventList.add(nextEvent);
					} else {
						CallData data = currentEvent.getCallData();
						data.setCallDuration(currentEvent.getCallData().getCallDuration() - timeRemaining);
						data.setStation(data.getStation() + 1);
						Event nextEvent = new Event(currentEvent.getTime() + timeRemaining,
								EventType.CALL_HANDOVER_EVENT, data);
						futureEventList.add(nextEvent);
					}
				} else {
					callBlocked++;
					continue;
				}
			} else if (currentEvent.getType() == EventType.CALL_HANDOVER_EVENT) {
				if (currentEvent.getChannelType() == ChannelType.NORMAL_CHANNEL)
					channelsAvailable[currentEvent.getCallData().getStation() - 1][0]++;
				else if (currentEvent.getChannelType() == ChannelType.HANDOVER_CHANNEL)
					channelsAvailable[currentEvent.getCallData().getStation() - 1][1]++;
				if (channelsAvailable[currentEvent.getCallData().getStation()][0] > 0) {
					callHandover++;
					channelsAvailable[currentEvent.getCallData().getStation()][0]--;
					CallData data = currentEvent.getCallData();
					double timeRemaining = (2 / data.getVelocity()) * 3600;
					if (timeRemaining >= data.getCallDuration()) {
						Event nextEvent = new Event(currentEvent.getTime() + data.getCallDuration(),
								EventType.CALL_TERMINATION_EVENT, data);
						futureEventList.add(nextEvent);
					} else if (currentEvent.getCallData().getStation() == 20) {
						Event nextEvent = new Event(currentEvent.getTime() + timeRemaining,
								EventType.CALL_TERMINATION_EVENT, data);
						futureEventList.add(nextEvent);
					} else {
						data.setCallDuration(currentEvent.getCallData().getCallDuration() - timeRemaining);
						data.setStation(data.getStation() + 1);
						Event nextEvent = new Event(currentEvent.getTime() + timeRemaining,
								EventType.CALL_HANDOVER_EVENT, data);
						futureEventList.add(nextEvent);
					}
				} else if (channelsAvailable[currentEvent.getCallData().getStation()][1] > 0) {
					callHandover++;
					channelsAvailable[currentEvent.getCallData().getStation()][1]--;
					CallData data = currentEvent.getCallData();
					double timeRemaining = (2 / data.getVelocity()) * 3600;
					if (timeRemaining >= data.getCallDuration()) {
						Event nextEvent = new Event(currentEvent.getTime() + data.getCallDuration(),
								EventType.CALL_TERMINATION_EVENT, data);
						nextEvent.setChannelType(ChannelType.HANDOVER_CHANNEL);
						futureEventList.add(nextEvent);
					} else if (currentEvent.getCallData().getStation() == 20) {
						Event nextEvent = new Event(currentEvent.getTime() + timeRemaining,
								EventType.CALL_TERMINATION_EVENT, data);
						nextEvent.setChannelType(ChannelType.HANDOVER_CHANNEL);
						futureEventList.add(nextEvent);
					} else {
						data.setCallDuration(currentEvent.getCallData().getCallDuration() - timeRemaining);
						data.setStation(data.getStation() + 1);
						Event nextEvent = new Event(currentEvent.getTime() + timeRemaining,
								EventType.CALL_HANDOVER_EVENT, data);
						nextEvent.setChannelType(ChannelType.HANDOVER_CHANNEL);
						futureEventList.add(nextEvent);
					}
				} else {
					callDropped++;
					continue;
				}
			} else if (currentEvent.getType() == EventType.CALL_TERMINATION_EVENT) {
				if (currentEvent.getChannelType() == ChannelType.NORMAL_CHANNEL)
					channelsAvailable[currentEvent.getCallData().getStation()][0]++;
				else if (currentEvent.getChannelType() == ChannelType.HANDOVER_CHANNEL)
					channelsAvailable[currentEvent.getCallData().getStation()][1]++;
				callCompleted++;
			}
		}
		System.out.println("Number of calls dropped: " + callDropped);
		/*System.out.println("Number of calls blocked: " + callBlocked);
		System.out.println("Number of calls completed: " + callCompleted);
		System.out.println("Call Blocked Percentage: " + ((double) (callBlocked / 100.0)));
		System.out.println("Call Dropped Percentage: " + ((double) (callDropped / 100.0)));
		System.out.println("Call Completed Percentage: " + ((double) (callCompleted / 100.0)));
		System.out.println("Successful Handover Percentage: " + ((double) (callHandover / 100.0)));
		*/}
		
		for(int i = 0;i<9000;i++)
		{
			series1.add(i+1, dropped.get(i)/(1000*(i+1)));
			series2.add(i+1, blocked.get(i)/(1000*(i+1)));
		}
		
		dataset.addSeries(series1);
	    dataset.addSeries(series2);
	    System.out.println(series1.getItemCount());
	    System.out.println(series2.getItemCount());
	    GenerateGraphs xyPlot = new GenerateGraphs();
	    xyPlot.generateLineChart(dataset, directory+"Warm-up.png", "Steady State - No Channel Reservation", "Total Number of Calls", "Percentage of Calls");
		
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<Event> generateCallStartEvents(String inputFile) {
		if (inputFile == null || inputFile.length() == 0)
			return null;
		List<Event> events = new ArrayList<Event>();
		CsvFileReader fileReader = new CsvFileReader();
		List<CallData> listCallData = fileReader.readCSV(inputFile);
		for (CallData data : listCallData) {
			events.add(new Event(data.getTime(), EventType.CALL_START_EVENT, data));
		}
		return events;
	}
}
