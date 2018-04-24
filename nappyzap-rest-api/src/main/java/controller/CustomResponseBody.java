package controller;

public class CustomResponseBody {
	private String response;
	private Object additional;
	
	public CustomResponseBody(String response){
		this.response = response;
		this.additional = null;
	}
	
	public CustomResponseBody(String response, Object additional){
		this.response = response;
		this.additional = additional;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Object getAdditional() {
		return additional;
	}

	public void setAdditional(Object additional) {
		this.additional = additional;
	}
}
