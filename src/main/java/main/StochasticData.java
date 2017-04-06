package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class StochasticData {

	public static void main(String[] args) {
		List<CallData> listCallData = new ArrayList<CallData>();
		for(int i = 0;i<1000;i++){
			CallData data = new CallData(0,0,0,0,normalStochasticData(1,100));
			listCallData.add(data);
		}
		AnalyzeInput.generateVelocityHistogram(listCallData);
	}
	
	public static double normalStochasticData(double mean, double standardDeviation){
		NormalDistribution distribution = new NormalDistribution(mean,standardDeviation);
		return distribution.sample();
	}
	
}
