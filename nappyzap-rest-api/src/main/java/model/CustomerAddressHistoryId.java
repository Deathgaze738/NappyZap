package model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Embeddable
public class CustomerAddressHistoryId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1576994628360991241L;
	
	@NotNull
	@JoinColumn(name = "owner_id")
	@ManyToOne
	private Customer owner;
	
	@NotNull
	@JoinColumn(name = "address_id")
	@ManyToOne
	private Address address;
	
	@CreationTimestamp
	private Date dateChanged;

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
