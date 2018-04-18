package model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

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

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "address_id")
	private Long addressId;
	
	@NotNull
	@Size(max = 100, message = "Address lines cannot be bigger than 100 characters.")
	private String address_line_1;
	
	@Size(max = 100, message = "Address lines cannot be bigger than 100 characters.")
	private String address_line_2;
	
	@Size(max = 100, message = "Address lines cannot be bigger than 100 characters.")
	private String address_line_3;
	
	@Size(max = 100, message = "Address lines cannot be bigger than 100 characters.")
	private String address_line_4;
	
	@NotNull
	@Size(max = 100, message = "Address Locality cannot be bigger than 100 characters.")
	private String locality;
	
	@Size(max = 100, message = "Address Region cannot be bigger than 100 characters.")
	private String region;
	
	@Size(max = 50, message = "Postal Code cannot be bigger than 50 characters.")
	private String postcode;
	
	@NotNull
	@Valid
	@ManyToOne
	@JoinColumn(name = "iso_code", referencedColumnName = "iso_code")
	private CountryCode country_code;
	
	@NotNull
	private double lat;
	
	@NotNull
	private double lng;
	
	@CreationTimestamp
	private Timestamp date_created;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		String[] addressLines = {address_line_1, address_line_2, address_line_3, address_line_4};
		for(int x = 0; x < addressLines.length; x++){
			if(addressLines[x] != ""){
				sb.append(addressLines[x] + ",");
			}
			else{
				break;
			}
		}
		sb.append(locality + ",");
		sb.append(region + ",");
		sb.append(postcode + ",");
		sb.append(country_code.getIso_code());
		return sb.toString();
	}
	
	public String toGMapParam() throws IOException{
		StringBuilder sb = new StringBuilder();
		String[] addressLines = {address_line_1, address_line_2, address_line_3, address_line_4};
		for(int x = 0; x < addressLines.length; x++){
			if(addressLines[x] != ""){
				sb.append(URLEncoder.encode(addressLines[x], "UTF-8") + ",");
			}
			else{
				break;
			}
		}
		sb.append(URLEncoder.encode(locality,"UTF-8") + ",");
		sb.append(URLEncoder.encode(region, "UTF-8") + ",");
		sb.append(URLEncoder.encode(postcode, "UTF-8") + ",");
		sb.append(URLEncoder.encode(country_code.getIso_code(), "UTF-8"));
		return sb.toString();
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getAddress_line_1() {
		return address_line_1;
	}

	public void setAddress_line_1(String address_line_1) {
		this.address_line_1 = address_line_1;
	}

	public String getAddress_line_2() {
		return address_line_2;
	}

	public void setAddress_line_2(String address_line_2) {
		this.address_line_2 = address_line_2;
	}

	public String getAddress_line_3() {
		return address_line_3;
	}

	public void setAddress_line_3(String address_line_3) {
		this.address_line_3 = address_line_3;
	}

	public String getAddress_line_4() {
		return address_line_4;
	}

	public void setAddress_line_4(String address_line_4) {
		this.address_line_4 = address_line_4;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public CountryCode getCountry_code() {
		return country_code;
	}

	public void setCountry_code(CountryCode country_code) {
		this.country_code = country_code;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Timestamp getDate_created() {
		return date_created;
	}

	public void setDate_created(Timestamp date_created) {
		this.date_created = date_created;
	}
}
