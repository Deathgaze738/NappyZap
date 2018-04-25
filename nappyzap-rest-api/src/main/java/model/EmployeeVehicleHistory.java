package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "employeevehiclehistory")
public class EmployeeVehicleHistory {
	@EmbeddedId
	private EmployeeVehicleId id;
	
	@NotNull
	@Column(name = "start_datetime")
	private Timestamp startTimestamp;
	
	@NotNull
	@Column(name = "end_datetime")
	private Timestamp endTimestamp;

	public EmployeeVehicleId getId() {
		return id;
	}

	public void setId(EmployeeVehicleId id) {
		this.id = id;
	}

	public Timestamp getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Timestamp startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
}
