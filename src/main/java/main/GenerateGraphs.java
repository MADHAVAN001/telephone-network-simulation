package main;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.plot.PlotOrientation;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class GenerateGraphs {
	/**
	 * 
	 * @param values
	 * @param numBins
	 * @param fileName
	 * @param plotTitle
	 * @param xaxis
	 * @param yaxis
	 */
	public void generateHistogram(double[] values, int numBins, String fileName, String plotTitle, String xaxis,
			String yaxis) {
		if (values == null || values.length == 0 || numBins <= 0 || fileName == null || fileName.length() == 0
				|| plotTitle == null || plotTitle.length() == 0 || xaxis == null || xaxis.length() == 0 || yaxis == null
				|| yaxis.length() == 0)
			return;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Histogram", values, numBins);
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips,
				urls);
		int width = 1000;
		int height = 600;
		try {
			ChartUtilities.saveChartAsPNG(new File(fileName), chart, width, height);
		} catch (IOException e) {
			System.out.println("Error in creating Histogram");
		}
	}

	public void generateLineChart(XYDataset dataset, String fileName, String plotTitle, String xaxis, String yaxis) {

		JFreeChart chart = ChartFactory.createXYLineChart(plotTitle, xaxis, yaxis, dataset);
		int width = 1000;
		int height = 600;
		try {
			ChartUtilities.saveChartAsPNG(new File(fileName), chart, width, height);
		} catch (IOException e) {
			System.out.println("Error in creating XY Line Plot");
		}
	}
}
