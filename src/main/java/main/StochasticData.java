package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class StochasticData {

	public static void main(String[] args) {
		List<CallData> listCallData = new ArrayList<CallData>();
		double previousCall = 0;
		for (int i = 0; i < 20000; i++) {
			double callTime =  previousCall + Math.abs(exponentialStochasticData(1.3698));
			previousCall = callTime;
			CallData data = new CallData(i + 1, callTime,
					uniformStochasticData(1, 20), exponentialStochasticData(99.8359), normalStochasticData(120.0721, 9.0191));
			listCallData.add(data);
		}
		AnalyzeInput.generateVelocityHistogram(listCallData);
		AnalyzeInput.generateCallDurationHistogram(listCallData);
		AnalyzeInput.generateInterArrivalTimeHistogram(listCallData);
		AnalyzeInput.generateBaseStationHistogram(listCallData);
		AnalyzeInput.printVelocityMean(listCallData);
		AnalyzeInput.printBaseStationMean(listCallData);
		AnalyzeInput.printCallDurationMean(listCallData);
		AnalyzeInput.printInterArrivalMean(listCallData);
	}

	/**
	 * 
	 * @param mean
	 * @param standardDeviation
	 * @return
	 */
	public static double normalStochasticData(double mean, double standardDeviation) {
		NormalDistribution distribution = new NormalDistribution(mean, standardDeviation);
		return distribution.sample();
	}

	/**
	 * 
	 * @param mean
	 * @return
	 */
	public static double exponentialStochasticData(double mean) {
		ExponentialDistribution distribution = new ExponentialDistribution(mean);
		return distribution.sample();
	}

	/**
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static int uniformStochasticData(int lowerBound, int upperBound) {
		UniformIntegerDistribution distribution = new UniformIntegerDistribution(lowerBound, upperBound);
		return distribution.sample();
	}
}
