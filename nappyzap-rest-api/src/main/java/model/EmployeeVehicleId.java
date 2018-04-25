package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

@Embeddable
public class EmployeeVehicleId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2801966404819135248L;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@Valid
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_registration")
	@Valid
	private Vehicle vehicle;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
