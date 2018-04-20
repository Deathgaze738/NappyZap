package dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import model.Sex;

public class EmployeeDetailsDTO {
	@NotNull
	@Email
	@Size(max = 254, message = "Email Address must be less than 254 characters.")
	private String emailAddress;
	
	@NotNull
	@Size(max = 50, message = "First name must be less than 50 characters.")
	private String firstName;

	@Size(max = 50, message = "Middle name must be less than 50 characters.")
	private String middleName;
	
	@NotNull
	@Size(max = 50, message = "Last name must be less than 50 characters.")
	private String lastName;
	
	@NotNull
	@Valid
	private Sex sex;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
	
}
