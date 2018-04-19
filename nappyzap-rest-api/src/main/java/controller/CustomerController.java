package controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.CustomerRepository;
import dao.SexRepository;
import dto.CustomerAddressDTO;
import dto.CustomerTelephoneNumberDTO;
import dto.CustomerUpdateDTO;
import model.Address;
import model.Customer;
import model.CustomerTelephoneNumber;
import service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private static final String AUTHORIZATION_FAILED = "Authorization denied: Username and password are incorrect.";
	private static final String CUSTOMER_DELETE_SUCCESS = "Customer data successfully deleted.";
	private static final String ADDRESS_ADD_FAILED = "Please ensure that Address Type, Country Code, and Residence Type are valid and formatting is correct.";
	private static final String ADDRESS_EDIT_FAILED = "Please ensure that all of the fields are valid.";
	private static final String ADDRESS_ADD_SUCCESS = "Address successfully added!";
	private static final String ADDRESS_DELETE_SUCCESS = "Address successfully deleted!";
	private static final String ADDRESS_EDIT_SUCCESS = "Address successfully edited!";
	private static final String PHONENUMBER_ADD_SUCCESS = "Telephone number successfully added!";
	private static final String PHONENUMBER_ADD_FAILED = "Please ensure that telephone number and type are valid.";
	private static final String PHONENUMBER_DELETE_SUCCESS = "Phone Number has been deleted successfully!";

	@Autowired
	CustomerService customerService;
	
	/**
	 * Checks if the user is authorized, and then checks if the user actually owns the address
	 * they're trying to delete. If the user fails either of these authorizations, Unauthorized will be returned.
	 * Deleting an address simply removes the CustomerAddress relation, the actual address is kept for delivery 
	 * history purposes.
	 * @param customerId						ID of the customer attempting to delete the address.
	 * @param addr								ID of the address the customer is trying to delete.
	 * @param authorization						Authorization header for the User.
	 * @param response							HTTP Response code
	 * @return									Returns OK if delete successful, Unauthorized if address isn't owned by user, if Address doesn't exist, or if user isn't authorized.
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}/addresses/{addr}", method = RequestMethod.DELETE)
	public Object deleteAddress(@PathVariable(value = "id") Long customerId, 
			@PathVariable(value = "addr") Long addr, 
			@RequestHeader("Authorization") String authorization, 
			HttpServletResponse response){
		Object returnVal = null;
		if(!customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		else{
			if(customerService.deleteAddress(addr, customerId)){
				response.setStatus(HttpServletResponse.SC_OK);
				returnVal = new CustomerResponseBody(ADDRESS_DELETE_SUCCESS, null);
			}
			else{
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, "Not your address.");
			}
		}
		return returnVal;
	}
	
	/**
	 * Checks if the user is authorized before returning all of their phone numbers.
	 * @param customerId						ID of the customer.
	 * @param authorization						Base 64 encoded authorization string
	 * @param response							Response servlet
	 * @return									Returns Authorization failed if user authorization fails, or user doesn't exist. Returns list of phone numbers otherwise.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/phonenos")
	@ResponseBody
	public Object getPhoneNumbers(@PathVariable(value = "id") Long customerId, 
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response){
		Object returnVal = null;
		if(!customerService.authorize(customerId,  authorization)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		else{
			response.setStatus(HttpServletResponse.SC_OK);
			returnVal = customerService.getTelephoneNumbers(customerId);
		}
		return returnVal;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid @RequestBody Customer cust){
		return customerService.addCustomer(cust);
	}
	
	/**
	 * Handles deleting a customers user information.
	 * @param customerId						ID of the customer to be deleted.
	 * @param authorization						Authorization header of the HTTP request.
	 * @param response							Response code.
	 * @return									Returns authorization failed if Authorization header is incorrect, or Deletion success else.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public Object deleteCustomer(@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response){
		Object returnVal = null;
		if(!customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		else{
			response.setStatus(HttpServletResponse.SC_OK);
			customerService.deleteCustomer(customerId);
			returnVal = new CustomerResponseBody(CUSTOMER_DELETE_SUCCESS, null);
		}
		return returnVal;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/phonenos/{no}")
	public Object deleteTelephoneNumber(@PathVariable(value = "id") Long customerId,
			@PathVariable(value = "no") Long telephoneId,
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response){
		Object returnVal = null;
		if(!customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		else{
			if(customerService.deletePhoneNumber(telephoneId, customerId)){
				response.setStatus(HttpServletResponse.SC_OK);
				returnVal = new CustomerResponseBody(PHONENUMBER_DELETE_SUCCESS, null);
			}
			else{
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
			}
		}
		return returnVal;
	}
	
	/**
	 * Gets a customer object and returns it after verifying the User credentials.
	 * @param customerId			ID for the customer data being retrieved.
	 * @param authorization			Base64 Encoded Authorization Header.
	 * @param response				HTTP Response Header.
	 * @return						Returns a customer object for the ID, or an Authorization error should this fail.
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Object getCustomer(@PathVariable(value = "id") Long customerId, 
			@RequestHeader("Authorization") String authorization, 
			HttpServletResponse response){
		Object returnVal = null;
		if(!customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		returnVal = customerService.getCustomer(customerId);
		return returnVal;
	}
	
	/**
	 * Returns a CustomerResponseBody containing a explanatory message and the result of your address addition.
	 * If the user is unauthenticated for path variable {id}, a forbidden status is return alongside an explanation.
	 * If the CustomerAddressDTO is malformed, HTTP Bad Request is returned with a message and the DTO sent.
	 * If successful, the Address and Customer relationship to that address are added.
	 * If the customer already has a delivery address on file, the original delivery address will be replaced and recorded in the CustomerAddressHistory table.
	 * This is done to prevent the same customer from using one paid account for multiple addresses.
	 * 
	 * @param customerAddressDTO	Must be in the format of a CustomerAddressDTO Object, referenced in the documentation.
	 * @param customerId			PathVariable of the customers ID
	 * @param authorization			Base64 Encoded Authorization Header
	 * @param response				HTTP Response header.
	 * @return						Returns a CustomerResponseBody containing results of request.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/addresses")
	@ResponseBody
	public Object addAddress(@Valid @RequestBody CustomerAddressDTO customerAddressDTO, 
			@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization, 
			HttpServletResponse response){
		Object returnVal = null;
		if(!customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		else{
			CustomerAddressDTO result = customerService.addAddress(customerAddressDTO, customerId);
			if(result == null){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				returnVal = new CustomerResponseBody(ADDRESS_ADD_FAILED, result);
			}
			else{
				response.setStatus(HttpServletResponse.SC_CREATED);
				returnVal = new CustomerResponseBody(ADDRESS_ADD_SUCCESS, result);
			}
		}
		return returnVal;
	}
	
	/**
	 * Updates the user data that is updateable. This data is transferred in the CustomerUpdateDTO.
	 * Update is rejected if the data is not valid according to the Model definitions.
	 * Update is rejected if the user is not valid.
	 * @param customerId						ID of the customer being updated
	 * @param authorization						Authorization header for the customer being updated.
	 * @param response							HTTPResponse to return code.
	 * @param customerUpdateDTO					DTO to format incoming update data.
	 * @return									Returns correct status code and message for outcome.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	public Object editCustomer(@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response,
			@RequestBody CustomerUpdateDTO customerUpdateDTO){
		Object returnVal = null;
		if(customerService.authorize(customerId, authorization)){
			if(customerService.editCustomer(customerId, customerUpdateDTO)){
				response.setStatus(HttpServletResponse.SC_OK);
				returnVal = new CustomerResponseBody(ADDRESS_EDIT_SUCCESS, customerUpdateDTO);
			}
			else{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				returnVal = new CustomerResponseBody(ADDRESS_EDIT_FAILED, customerUpdateDTO);
			}
		}
		else{
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return returnVal;
	}
	
	
	/**
	 * Checks the users credentials in the Authorization header and returns all of their addresses if the authorization passes.
	 * @param customerId					ID of the customer looking for addresses.
	 * @param authorization					Authorization header of the HTTP request.
	 * @param response						Response header of the HTTP response.
	 * @return								Return addresses if authorized, return an error and forbidden response else.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/addresses")
	public Object getAddresses(@PathVariable(value = "id") Long customerId, 
			@RequestHeader("Authorization") String authorization, 
			HttpServletResponse response){
		Object returnVal;
		if(customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_OK);
			returnVal = customerService.getAddresses(customerId);
		}
		else{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		return returnVal;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/phonenos")
	public Object addPhoneNumber(@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response,
			@Valid @RequestBody CustomerTelephoneNumberDTO customerTelephoneNumberDTO){
		Object returnVal = null;
		if(customerService.authorize(customerId, authorization)){
			returnVal = customerService.addPhoneNumber(customerId, customerTelephoneNumberDTO);
			if(returnVal != null){
				response.setStatus(HttpServletResponse.SC_CREATED);
				returnVal = new CustomerResponseBody(PHONENUMBER_ADD_SUCCESS, returnVal);
			}
			else{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				returnVal = new CustomerResponseBody(PHONENUMBER_ADD_FAILED, customerTelephoneNumberDTO);
			}
		}
		else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		return returnVal;
	}
}


