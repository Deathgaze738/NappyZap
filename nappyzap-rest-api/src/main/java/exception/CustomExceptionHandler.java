package exception;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		String error = "Malformed JSON Request.";
		return buildResponseEntity(new DefaultError(HttpStatus.BAD_REQUEST, ex, error));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		String error = "Input validation failed.";
		return buildResponseEntity(new DefaultError(HttpStatus.BAD_REQUEST, ex, error));
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex){
		String error = "Authorization Failed.";
		return buildResponseEntity(new DefaultError(HttpStatus.UNAUTHORIZED, ex, error));
	}

	private ResponseEntity<Object> buildResponseEntity(DefaultError defaultError) {
		return new ResponseEntity<>(defaultError, defaultError.getStatus());
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class, TransactionSystemException.class, ConstraintViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolation(Exception ex){
		String error = "Validation constraint failed!";
		return buildResponseEntity(new DefaultError(HttpStatus.BAD_REQUEST, ex, error));
	}
	
	@ExceptionHandler({NotFoundException.class, EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleResourceNotFound(Exception ex){
		String error = "Resource doesn't exist.";
		return buildResponseEntity(new DefaultError(HttpStatus.NOT_FOUND, ex, error));
	}
	
	@ExceptionHandler(MappingProviderException.class)
	public ResponseEntity<Object> handleMappingProvider(MappingProviderException ex){
		String error = "Mapping provider error.";
		return buildResponseEntity(new DefaultError(HttpStatus.INTERNAL_SERVER_ERROR, ex, error));
	}
	
	@ExceptionHandler(RoutingException.class)
	public ResponseEntity<Object> handleRouting(RoutingException ex){
		String error = "Routing error.";
		return buildResponseEntity(new DefaultError(HttpStatus.BAD_REQUEST, ex, error));
	}
}
