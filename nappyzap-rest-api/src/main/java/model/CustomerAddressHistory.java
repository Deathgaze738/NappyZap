package model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customeraddresshistory")
public class CustomerAddressHistory {
	
	@EmbeddedId
	private CustomerAddressHistoryId customerAddressHistoryId;
	
	@ManyToOne
	@JoinColumn(name = "addressType_id")
	private AddressType addressType;
	
	@ManyToOne
	@JoinColumn(name = "residenceType_id")
	private ResidenceType ResidenceType;

	public CustomerAddressHistoryId getCustomerAddressHistoryId() {
		return customerAddressHistoryId;
	}

	public void setCustomerAddressHistoryId(CustomerAddressHistoryId customerAddressHistoryId) {
		this.customerAddressHistoryId = customerAddressHistoryId;
	}

	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	public ResidenceType getResidenceType() {
		return ResidenceType;
	}

	public void setResidenceType(ResidenceType residenceType) {
		ResidenceType = residenceType;
	}
}
