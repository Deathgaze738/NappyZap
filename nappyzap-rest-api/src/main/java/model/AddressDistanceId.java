package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class AddressDistanceId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3493804482867395954L;

	@ManyToOne
	@JoinColumn(name = "address_1")
	private Address address1;
	
	@ManyToOne
	@JoinColumn(name = "address_2")
	private Address address2;
	
	public Address getAddress1() {
		return address1;
	}

	public void setAddress1(Address address1) {
		this.address1 = address1;
	}

	public Address getAddress2() {
		return address2;
	}

	public void setAddress2(Address address2) {
		this.address2 = address2;
	}
}
