package dto;

import java.sql.Timestamp;

public class RoadRouteDTO {
	
	private Long roadDistance;
	private Long timeTaken;
	private Timestamp timeRetrieved;
	
	public RoadRouteDTO(Long roadDistance, Long timeTaken, Timestamp timeRetrieved){
		this.roadDistance = roadDistance;
		this.timeTaken = timeTaken;
		this.timeRetrieved = timeRetrieved;
	}

	public RoadRouteDTO() {
		
	}

	public Timestamp getTimeRetrieved() {
		return timeRetrieved;
	}

	public void setTimeRetrieved(Timestamp timeRetrieved) {
		this.timeRetrieved = timeRetrieved;
	}

	public Long getRoadDistance() {
		return roadDistance;
	}

	public void setRoadDistance(Long roadDistance) {
		this.roadDistance = roadDistance;
	}

	public Long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Long timeTaken) {
		this.timeTaken = timeTaken;
	}
}
