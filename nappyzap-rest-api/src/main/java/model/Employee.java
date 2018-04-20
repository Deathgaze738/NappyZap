package model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employee_id")
	private Long employeeId;
	
	@NotNull
	@Column(name = "email_address", unique = true)
	@Size(max = 254, message = "Email Address can't be longer than 254 characters.")
	@Email
	private String emailAddress;
	
	@NotNull
	@Column(name = "password")
	@Size(max = 255, min = 8, message = "Password must be more than 8 characters.")
	private String password;
	
	@NotNull
	@Column(name = "first_name")
	@Size(max = 50, message = "First name cannot be longer than 50 characters")
	private String firstName;
	
	@Column(name = "middle_name")
	@Size(max = 50, message = "Middle name cannot be longer than 50 characters")
	private String middleName;
	
	@NotNull
	@Column(name = "last_name")
	@Size(max = 50, message = "Last name cannot be longer than 50 characters")
	private String lastName;
	
	@NotNull
	@JoinColumn(name = "sex")
	@ManyToOne
	private Sex sex;
	
	@NotNull
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@NotNull
	@Column(name = "start_date")
	private Date startDate;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "job_type")
	private EmployeeType jobType;
	
	@ManyToOne
	@NotNull
	@Valid
	@JoinColumn(name = "home_address")
	private Address homeAddress;
	
	@Column(name = "initial_training")
	private boolean initialTraining;
	
	@Column(name = "active")
	private boolean active;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isInitialTraining() {
		return initialTraining;
	}

	public void setInitialTraining(boolean initialTraining) {
		this.initialTraining = initialTraining;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public EmployeeType getJobType() {
		return jobType;
	}

	public void setJobType(EmployeeType jobType) {
		this.jobType = jobType;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
