package model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "employeetelephonenumber")
public class EmployeeTelephoneNumber {
	@EmbeddedId
	private EmployeeTelephoneNumberId employeeTelephoneNumberId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "telephone_number_type")
	private TelephoneNumberType telephoneNumberType;

	public EmployeeTelephoneNumberId getEmployeeTelephoneNumberId() {
		return employeeTelephoneNumberId;
	}

	public void setEmployeeTelephoneNumberId(EmployeeTelephoneNumberId employeeTelephoneNumberId) {
		this.employeeTelephoneNumberId = employeeTelephoneNumberId;
	}

	public TelephoneNumberType getTelephoneNumberType() {
		return telephoneNumberType;
	}

	public void setTelephoneNumberType(TelephoneNumberType telephoneNumberType) {
		this.telephoneNumberType = telephoneNumberType;
	}
}
