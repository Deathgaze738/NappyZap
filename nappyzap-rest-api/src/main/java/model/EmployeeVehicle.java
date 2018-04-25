package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "employeevehicle")
public class EmployeeVehicle {
	@EmbeddedId
	private EmployeeVehicleId id;
	
	@CreationTimestamp
	@Column(name = "start_timestamp")
	private Timestamp startTimestamp;

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
}
