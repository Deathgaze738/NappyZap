package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "currencycode")
public class CurrencyCode {
	@Id
	@NotNull
	@Column(name = "iso_currency_code")
	@Size(max = 3, min = 3, message = "ISO Currency codes must be 3 characters.")
	private String isoCurrencyCode;
	
	@Column(name = "name")
	@Size(max = 100, message = "Name must be 100 characters maximum.")
	private String name;
	
	@Column(name = "decimal_point")
	private int decimalPoint;
	
	@Column(name = "currency_symbol")
	@NotNull
	@Size(max = 5, message = "Currency Symbol can be 5 characters maximum.")
	private String currencySymbol;

	public String getIsoCurrencyCode() {
		return isoCurrencyCode;
	}

	public void setIsoCurrencyCode(String isoCurrencyCode) {
		this.isoCurrencyCode = isoCurrencyCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDecimalPoint() {
		return decimalPoint;
	}

	public void setDecimalPoint(int decimalPoint) {
		this.decimalPoint = decimalPoint;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
}
