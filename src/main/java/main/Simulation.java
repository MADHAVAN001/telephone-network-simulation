package main;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

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

		for (int i = 0; i < 21; i++) {
			channelsAvailable[i][0] = 9;
			channelsAvailable[i][1] = 1;
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
		System.out.println("Number of calls blocked: " + callBlocked);
		System.out.println("Number of calls completed: " + callCompleted);
		System.out.println("Call Blocked Percentage: " + ((double)(callBlocked / 100.0)));
		System.out.println("Call Dropped Percentage: " + ((double)(callDropped / 100.0)));
		System.out.println("Call Completed Percentage: " + ((double)(callCompleted / 100.0)));
		System.out.println("Successful Handover Percentage: "+((double)(callHandover / 100.0)));
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
