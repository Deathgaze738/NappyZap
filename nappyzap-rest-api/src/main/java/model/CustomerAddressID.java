package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Embeddable
public class CustomerAddressID implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5353869593676565978L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "customer_id")
	private Customer ownerId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address addressId;

	public Customer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Customer ownerId) {
		this.ownerId = ownerId;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
