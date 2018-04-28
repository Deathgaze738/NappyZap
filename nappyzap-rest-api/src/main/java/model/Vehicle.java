package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "vehicle")
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vehicle_id")
	private Long id;
	
	@NotNull
	@Column(name = "vehicle_registration", unique = true)
	@Size(max = 20, message = "Vehicle Registration should be 20 characters maximum.")
	private String vehicleRegistration;
	
	@NotNull
	@Column(name = "make")
	@Size(max = 50, message = "Make of car should be 50 characters maximum.")
	private String make;
	
	@NotNull
	@Column(name = "model")
	@Size(max = 50, message = "Model of the car should be 50 characters maximum.")
	private String model;
	
	@NotNull
	@Column(name = "initial_mileage")
	private int initialMileage;
	
	@Column(name = "capacity")
	private int capacity;

	public String getVehicleRegistration() {
		return vehicleRegistration;
	}

	public void setVehicleRegistration(String vehicleRegistration) {
		this.vehicleRegistration = vehicleRegistration;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getInitialMileage() {
		return initialMileage;
	}

	public void setInitialMileage(int initialMileage) {
		this.initialMileage = initialMileage;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
