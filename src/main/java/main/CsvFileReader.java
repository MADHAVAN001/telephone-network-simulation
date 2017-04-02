package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class CsvFileReader {
	public List<CallData> readCSV(String fileName) {
		if (fileName == null || fileName.length() == 0)
			return null;
		List<CallData> listCall = new ArrayList<CallData>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(fileName));
			//Empty readLine for heading
			br.readLine();
			while ((line = br.readLine()) != null) {

				String[] values = line.split(cvsSplitBy);

				int id = Integer.parseInt(values[0]);
				double time = Double.parseDouble(values[1]);
				int station = Integer.parseInt(values[2]);
				double callDuration = Double.parseDouble(values[3]);
				double velocity = Double.parseDouble(values[4]);
				CallData call = new CallData(id, time, station, callDuration, velocity);
				listCall.add(call);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return listCall;
	}
}
