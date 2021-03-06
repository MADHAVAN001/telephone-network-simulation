package main;

import java.util.List;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class AnalyzeInput {
	private static String directory = "src/main/resources/";

	public static void main(String args[]) {

		String inputFile = directory + "data_file.csv";
		CsvFileReader fileReader = new CsvFileReader();
		List<CallData> listCallData = fileReader.readCSV(inputFile);
		generateBaseStationHistogram(listCallData);
		generateCallDurationHistogram(listCallData);
		generateInterArrivalTimeHistogram(listCallData);
		generateVelocityHistogram(listCallData);
		printVelocityMean(listCallData);
		printBaseStationMean(listCallData);
		printCallDurationMean(listCallData);
		printInterArrivalMean(listCallData);
	}

	/**
	 * 
	 * @param callData
	 */
	public static void generateBaseStationHistogram(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double[] values = new double[callData.size()];
		int i = 0;
		for (CallData data : callData) {
			values[i++] = data.getStation();
		}
		GenerateGraphs generator = new GenerateGraphs();
		generator.generateHistogram(values, 20, directory + "BaseStation.png", "Frequency vs. Base Station",
				"Base Station", "Frequency");
	}

	/**
	 * 
	 * @param callData
	 */
	public static void generateCallDurationHistogram(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double[] values = new double[callData.size()];
		int i = 0;
		for (CallData data : callData) {
			values[i++] = data.getCallDuration();
		}
		GenerateGraphs generator = new GenerateGraphs();
		generator.generateHistogram(values, 40, directory + "CallDuration.png", "Frequency vs. Call Duration",
				"Call Duration", "Frequency");
	}

	/**
	 * 
	 * @param callData
	 */
	public static void generateInterArrivalTimeHistogram(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double[] values = new double[callData.size()];
		int i = 0;
		double prevArrival = 0;
		for (CallData data : callData) {
			values[i++] = data.getTime() - prevArrival;
			prevArrival = data.getTime();
		}
		GenerateGraphs generator = new GenerateGraphs();
		generator.generateHistogram(values, 100, directory + "InterArrivalTime.png", "Frequency vs. Inter-Arrival Time",
				"Inter-Arrival Time", "Frequency");
	}

	/**
	 * 
	 * @param callData
	 */
	public static void generateVelocityHistogram(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double[] values = new double[callData.size()];
		int i = 0;
		for (CallData data : callData) {
			values[i++] = data.getVelocity();
		}
		GenerateGraphs generator = new GenerateGraphs();
		generator.generateHistogram(values, 40, directory + "Velocity.png", "Frequency vs. Velocity", "Velocity",
				"Frequency");
	}

	/**
	 * 
	 * @param callData
	 */
	public static void printVelocityMean(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double sumVelocity = 0;
		for (CallData data : callData) {
			sumVelocity += data.getVelocity();
		}
		System.out.println("Velocity Mean: " + ((double) sumVelocity / callData.size()));
	}

	/**
	 * 
	 * @param callData
	 */
	public static void printInterArrivalMean(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double sumArrival = 0;
		double prevArrival = 0;
		for (CallData data : callData) {
			sumArrival += data.getTime() - prevArrival;
			prevArrival = data.getTime();
		}
		System.out.println("Inter-Arrival Mean: " + ((double) sumArrival / callData.size()));
	}

	/**
	 * 
	 * @param callData
	 */
	public static void printCallDurationMean(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double callDuration = 0;
		for (CallData data : callData) {
			callDuration += data.getCallDuration();
		}
		System.out.println("Call Duration Mean: " + ((double) callDuration / callData.size()));
	}

	/**
	 * 
	 * @param callData
	 */
	public static void printBaseStationMean(List<CallData> callData) {
		if (callData == null || callData.size() == 0)
			return;
		double baseStation = 0;
		for (CallData data : callData) {
			baseStation += data.getStation();
		}
		System.out.println("Base Station Mean: " + ((double) baseStation / callData.size()));
	}

}
