package main;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class CallData {

	private int id;
	private double time;
	private int station;
	private double callDuration;
	private double velocity;

	public CallData(int id, double time, int station, double callDuration, double velocity) {
		this.id = id;
		this.time = time;
		this.station = station;
		this.callDuration = callDuration;
		this.velocity = velocity;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(double time) {
		this.time = time;
	}

	/**
	 * @return the station
	 */
	public int getStation() {
		return station;
	}

	/**
	 * @param station
	 *            the station to set
	 */
	public void setStation(int station) {
		this.station = station;
	}

	/**
	 * @return the callDuration
	 */
	public double getCallDuration() {
		return callDuration;
	}

	/**
	 * @param callDuration
	 *            the callDuration to set
	 */
	public void setCallDuration(double callDuration) {
		this.callDuration = callDuration;
	}

	/**
	 * @return the velocity
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity
	 *            the velocity to set
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

}
