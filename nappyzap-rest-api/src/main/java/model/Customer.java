package model;

public class Customer {
	private final long id;
	private final String email_address;
	private final String password;
	
	public Customer(long id, String email_address, String password){
		this.id = id;
		this.email_address = email_address;
		this.password = password;
	}
	
	public long getId(){
		return id;
	}
	
	public String getEmailAddress(){
		return email_address;
	}
	
	public String getPassword(){
		return password;
	}
}
