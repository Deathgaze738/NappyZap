package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "countrycode")
public class CountryCode {
	@Id
	@Length(max = 2, message = "ISO Code must be 2 characters.")
	private String iso_code;
	
	@NotNull
	@Length(max = 100, message = "Country name must be less than 100 characters.")
	private String country_name;

	public String getIso_code() {
		return iso_code;
	}

	public void setIso_code(String iso_code) {
		this.iso_code = iso_code;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
}
