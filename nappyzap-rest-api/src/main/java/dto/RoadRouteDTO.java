package dto;

import java.sql.Timestamp;

public class RoadRouteDTO {
	
	private double roadDistance;
	private double timeTaken;
	private Timestamp timeRetrieved;
	
	public RoadRouteDTO(double roadDistance, double timeTaken, Timestamp timeRetrieved){
		this.roadDistance = roadDistance;
		this.timeTaken = timeTaken;
		this.timeRetrieved = timeRetrieved;
	}

	public double getRoadDistance() {
		return roadDistance;
	}

	public void setRoadDistance(double roadDistance) {
		this.roadDistance = roadDistance;
	}

	public double getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(double timeTaken) {
		this.timeTaken = timeTaken;
	}

	public Timestamp getTimeRetrieved() {
		return timeRetrieved;
	}

	public void setTimeRetrieved(Timestamp timeRetrieved) {
		this.timeRetrieved = timeRetrieved;
	}
}
