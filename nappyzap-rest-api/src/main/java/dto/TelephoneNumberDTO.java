package dto;

import model.TelephoneNumber;
import model.TelephoneNumberType;

public class TelephoneNumberDTO {
	private TelephoneNumber telephoneNumber;
	private TelephoneNumberType type;
	public TelephoneNumber getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(TelephoneNumber telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public TelephoneNumberType getType() {
		return type;
	}
	public void setType(TelephoneNumberType type) {
		this.type = type;
	}
}
