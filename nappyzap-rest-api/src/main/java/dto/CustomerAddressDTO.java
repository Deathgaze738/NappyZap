package dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.Address;
import model.AddressType;
import model.ResidenceType;

public class CustomerAddressDTO {
	@NotNull
	@Valid
	private Address address;
	@NotNull
	@Valid
	private AddressType addressType;
	@NotNull
	@Valid
	private ResidenceType residenceType;
	private String notes;
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
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
