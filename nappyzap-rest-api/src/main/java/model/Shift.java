package model;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "shift")
public class Shift {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "shift_id")
	private Long id;
	
	@NotNull
	@Column(name = "start_time")
	private Time startTime;
	
	@NotNull
	@Column(name = "end_time")
	private Time endTime;
	
	@NotNull
	@Column(name = "name")
	@Size(max = 45, message = "Please ensure name is 45 characters maximum.")
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
