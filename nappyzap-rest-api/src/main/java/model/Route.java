package model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "route")
public class Route {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "route_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_reg")
	@NotNull
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name = "shift")
	@NotNull
	private Shift shift;
	
	@OneToMany(mappedBy = "route")
	private Set<RouteStop> stops;
	
	@NotNull
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	public Set<RouteStop> getStops() {
		return stops;
	}

	public void setStops(Set<RouteStop> stops) {
		this.stops = stops;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
