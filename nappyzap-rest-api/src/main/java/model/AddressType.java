package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "addresstype")
public class AddressType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long addressTypeId;
	
	@NotNull
	@Length(max = 45, message = "AddressType name should be 45 character maximum.")
	private String name;
	
	@NotNull
	@Length(max = 100, message = "AddressType description should be 100 characters maximum.")
	private String description;

	public Long getAddressTypeId() {
		return addressTypeId;
	}

	public void setAddressTypeId(Long addressTypeId) {
		this.addressTypeId = addressTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
