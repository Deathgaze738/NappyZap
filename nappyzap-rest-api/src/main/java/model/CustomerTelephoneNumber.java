package model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "customertelephonenumber")
public class CustomerTelephoneNumber {
	@EmbeddedId
	CustomerTelephoneNumberId customerTelephoneNumberId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "telephone_no_id")
	TelephoneNumber number;

	public CustomerTelephoneNumberId getCustomerTelephoneNumberId() {
		return customerTelephoneNumberId;
	}

	public void setCustomerTelephoneNumberId(CustomerTelephoneNumberId customerTelephoneNumberId) {
		this.customerTelephoneNumberId = customerTelephoneNumberId;
	}

	public TelephoneNumber getNumber() {
		return number;
	}

	public void setNumber(TelephoneNumber number) {
		this.number = number;
	}
}
