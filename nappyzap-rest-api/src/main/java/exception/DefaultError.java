package exception;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

public class DefaultError {
	private HttpStatus status;
	private Timestamp timestamp;
	private String message;
	private String debugMessage;
	
	private DefaultError(){
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public DefaultError(HttpStatus status){
		this();
		this.status = status;
	}
	
	public DefaultError(HttpStatus status, Throwable ex){
		this();
		this.status = status;
		this.message = "Unexpected Error.";
		this.debugMessage = ex.getLocalizedMessage();
	}
	
	public DefaultError(HttpStatus status, Throwable ex, String message){
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}
}
