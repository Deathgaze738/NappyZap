package exception;

public class UnauthorizedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2904291328915528139L;

	
	public UnauthorizedException(String message){
		super(message);
	}
}
