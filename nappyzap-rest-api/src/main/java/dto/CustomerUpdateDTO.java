package dto;

import java.sql.Date;

/**
 * DTO containing only the fields which are updateable after an account has been created.
 * @author Aaron
 *
 */
public class CustomerUpdateDTO {
	private String first_name;
	private String last_name;
	private int sex_id;
	private Date date_of_birth;
	
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
	
	public int getSex_id() {
		return sex_id;
	}
	public void setSex_id(int sex_id) {
		this.sex_id = sex_id;
	}
}
