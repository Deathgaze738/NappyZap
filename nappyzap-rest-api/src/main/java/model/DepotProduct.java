package model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "depotproduct")
public class DepotProduct {
	@EmbeddedId
	private DepotProductId id;
	
	@NotNull
	@Column(name = "quantity")
	private int quantity;

	public DepotProductId getId() {
		return id;
	}

	public void setId(DepotProductId id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
