package main;

/**
 * 
 * @author MADHAVAN001
 *
 */
public class Event implements Comparable<Event> {
	private double time;
	private int type;
	private CallData callData;
	private int channelType;

	/**
	 * 
	 * @param time
	 * @param type
	 */
	public Event(double time, int type, CallData calldata) {
		this.time = time;
		this.type = type;
		this.callData = calldata;
		this.channelType = 1;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(time);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + type;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Event))
			return false;
		Event other = (Event) obj;
		if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public int compareTo(Event event) {
		if (this.time == event.time)
			return 0;
		return this.time < event.time ? -1 : 1;
	}

	/**
	 * @return the callData
	 */
	public CallData getCallData() {
		return callData;
	}

	/**
	 * @param callData
	 *            the callData to set
	 */
	public void setCallData(CallData callData) {
		this.callData = callData;
	}

	/**
	 * @return the channelType
	 */
	public int getChannelType() {
		return channelType;
	}

	/**
	 * @param channelType
	 *            the channelType to set
	 */
	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

}
