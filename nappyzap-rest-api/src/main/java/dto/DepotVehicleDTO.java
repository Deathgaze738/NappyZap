package dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.Depot;
import model.Vehicle;

public class DepotVehicleDTO {
	@NotNull
	@Valid
	private Vehicle vehicle;
	
	@NotNull
	@Valid
	private Depot depot;

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Depot getDepot() {
		return depot;
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}
}
