package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "addressdistance")
public class AddressDistance {
	
	@EmbeddedId
	private AddressDistanceId id;
	
	@NotNull
	@Column(name = "road_distance")
	private Long roadDistance;
	
	@NotNull
	@Column(name = "time_taken")
	private Long timeTaken;
	
	@Column(name = "timestamp")
	private Timestamp timestamp;

	public AddressDistanceId getId() {
		return id;
	}

	public void setId(AddressDistanceId id) {
		this.id = id;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
