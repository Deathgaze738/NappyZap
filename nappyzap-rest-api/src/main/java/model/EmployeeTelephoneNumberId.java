package model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class EmployeeTelephoneNumberId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8222513333397505574L;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "telephone_number_id")
	private TelephoneNumber telephoneNumber;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public TelephoneNumber getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(TelephoneNumber telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
}
