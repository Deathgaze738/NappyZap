package model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customeraddress")
public class CustomerAddress {
	@EmbeddedId
	private CustomerAddressID customerAddressId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "address_type_id")
	private AddressType addressType;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "residence_type_id")
	private ResidenceType residenceType;
	
	@Size(max = 255, message = "Please keep notes to 255 characters or less.")
	private String notes;

	public CustomerAddressID getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(CustomerAddressID customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	public ResidenceType getResidenceType() {
		return residenceType;
	}

	public void setResidenceType(ResidenceType residenceType) {
		this.residenceType = residenceType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
