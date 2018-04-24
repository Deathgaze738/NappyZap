package exception;

public class NotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7053834581688918244L;

	public NotFoundException(String message){
		super(message);
	}
}
