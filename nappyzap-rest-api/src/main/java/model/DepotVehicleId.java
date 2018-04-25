package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Embeddable
public class DepotVehicleId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 817839781002923630L;

	@ManyToOne
	@JoinColumn(name = "depot_id")
	private Depot depot;
	
	@OneToOne
	@JoinColumn(name = "vehicle_registration")
	private Vehicle vehicle;

	public Depot getDepot() {
		return depot;
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
