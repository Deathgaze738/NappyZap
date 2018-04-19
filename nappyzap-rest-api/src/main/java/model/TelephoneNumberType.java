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
@Table(name = "telephonenumbertype")
public class TelephoneNumberType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "telephone_number_type_id")
	private Long telephoneNumberType;
	
	@Column(name = "type_name")
	@Size(max = 45)
	@NotNull
	private String typeName;

	public Long getTelephoneNumberType() {
		return telephoneNumberType;
	}

	public void setTelephoneNumberType(Long telephoneNumberType) {
		this.telephoneNumberType = telephoneNumberType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
