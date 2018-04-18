package jar;

import org.apache.tomcat.util.codec.binary.Base64;

public class Login {
	private String email_address;
	private String password;
	
	public Login(String encoded){
		byte[] decoded = Base64.decodeBase64(encoded);
		String decodedString  = new String(decoded);
		String[] parts = decodedString.split(":");
		if(parts.length == 2){
			this.email_address = parts[0];
			this.password = parts[1];
		}
		else{
			this.email_address = "";
			this.password = "";
		}
	}
	
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
