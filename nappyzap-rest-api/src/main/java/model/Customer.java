package model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	private long customerId;
	@Email
	@NotNull
	@Column(unique = true)
	private String email_address;
	private boolean email_verified;
	@NotNull
	@Size(min = 8, max = 255, message = "Password should be greater than 8 characters.")
	private String password;
	@NotNull
	@Size(max = 50, message = "First Name should be at 50 characters maximum.")
	private String first_name;
	@NotNull
	@Size(max = 50, message = "Last Name should be at 50 characters maximum.")
	private String last_name;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "sex", referencedColumnName = "sex_id", foreignKey=@ForeignKey(name = "customer_sex_fk"))
	private Sex sex_entity;
	@NotNull
	@DateTimeFormat(pattern = "YYYY-MM-DD")
	private Date date_of_birth;
	@UpdateTimestamp
	private Timestamp last_update;
	@CreationTimestamp
	private Timestamp datetime_created;
	private boolean two_factor_enabled;
	private String email_confirmation_token;
	private String password_reset_token;
	private Timestamp password_reset_expiration;
	
	@Override
	public String toString(){
		return String.format("%s %s : %s", first_name, last_name, sex_entity.getName());
	}
	
	public Long getId(){
		return customerId;
	}
	public void setId(Long customerId){
		this.customerId = customerId;
	}
	
	public Sex getSex_entity(){
		return sex_entity;
	}
	public void setSex_entity(Sex sex_entity){
		this.sex_entity = sex_entity;
	}
	
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	
	public boolean isEmail_verified() {
		return email_verified;
	}
	public void setEmail_verified(boolean email_verified) {
		this.email_verified = email_verified;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public Date getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	
	public Timestamp getDatetime_created() {
		return datetime_created;
	}
	public void setDatetime_created(Timestamp datetime_created) {
		this.datetime_created = datetime_created;
	}
	
	public boolean isTwo_factor_enabled() {
		return two_factor_enabled;
	}
	public void setTwo_factor_enabled(boolean two_factor_enabled) {
		this.two_factor_enabled = two_factor_enabled;
	}
	
	public String getEmail_confirmation_token() {
		return email_confirmation_token;
	}
	public void setEmail_confirmation_token(String email_confirmation_token) {
		this.email_confirmation_token = email_confirmation_token;
	}
	
	public String getPassword_reset_token() {
		return password_reset_token;
	}
	public void setPassword_reset_token(String password_reset_token) {
		this.password_reset_token = password_reset_token;
	}
	
	public Timestamp getPassword_reset_expiration() {
		return password_reset_expiration;
	}
	public void setPassword_reset_expiration(Timestamp password_reset_expiration) {
		this.password_reset_expiration = password_reset_expiration;
	}

	public Timestamp getLast_update() {
		return last_update;
	}

	public void setLast_update(Timestamp last_update) {
		this.last_update = last_update;
	}
}
