package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DepotProductId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 712396385984585121L;

	@ManyToOne
	@JoinColumn(name = "depot_id")
	private Depot depot;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public Depot getDepot() {
		return depot;
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
