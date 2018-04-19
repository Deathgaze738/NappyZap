package dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.TelephoneNumber;

public class CustomerTelephoneNumberDTO {
	@NotNull
	@Valid
	private TelephoneNumber telephoneNumber;
	
	@NotNull
	private Long typeId;

	public TelephoneNumber getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(TelephoneNumber telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	
}
