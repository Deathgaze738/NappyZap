package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "vehiclegpsposition")
public class VehicleGPSPosition {
	@Id
	@OneToOne
	@JoinColumn(name = "vehicle_registration")
	private Vehicle vehicleRegistration;
	
	@Column(name = "lat")
	private double lat;
	
	@Column(name = "lng")
	private double lng;
	
	@UpdateTimestamp
	@Column(name = "last_update")
	private Timestamp lastUpdate;

	public Vehicle getVehicleRegistration() {
		return vehicleRegistration;
	}

	public void setVehicleRegistration(Vehicle vehicleRegistration) {
		this.vehicleRegistration = vehicleRegistration;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
