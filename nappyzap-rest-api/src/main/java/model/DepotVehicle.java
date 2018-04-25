package model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "depotvehicle")
public class DepotVehicle {
	@EmbeddedId
	private DepotVehicleId id;

	public DepotVehicleId getId() {
		return id;
	}

	public void setId(DepotVehicleId id) {
		this.id = id;
	}
}
