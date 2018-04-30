package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "telephonenumber")
public class TelephoneNumber {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "telephone_no_id")
	private Long telephoneNoId;
	
	@NotNull
	@Column(name = "country_code")
	@Size(max = 10, message = "Country Code is limited to 10 characters.")
	private String countryCode;
	
	@NotNull
	@Column(name = "number")
	@Size(max = 25, message = "Number is limited to 25 characters.")
	private String number;

	public Long getTelephoneNoId() {
		return telephoneNoId;
	}

	public void setTelephoneNoId(Long telephoneNoId) {
		this.telephoneNoId = telephoneNoId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
