package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CustomerTelephoneNumberId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7051987689628465619L;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Customer owner;
	
	@ManyToOne
	@JoinColumn(name = "type_of_number")
	private TelephoneNumberType type;

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public TelephoneNumberType getType() {
		return type;
	}

	public void setType(TelephoneNumberType type) {
		this.type = type;
	}
}
