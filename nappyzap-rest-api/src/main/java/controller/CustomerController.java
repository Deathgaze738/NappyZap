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
import model.Address;
import model.Customer;
import service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private static final String AUTHORIZATION_FAILED = "Authorization denied: Username and password are incorrect.";
	private static final String ADDRESS_ADD_FAILED = "Adding Address failed: Please ensure that Address Type, Country Code, and Residence Type are valid and formatting is correct.";
	private static final String ADDRESS_ADD_SUCCESS = "Address successfully added!";
	private static final String ADDRESS_DELETE_SUCCESS = "Address successfully deleted!";
	
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	SexRepository sexRepository;
	@Autowired
	CustomerService customerService;
	
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
	
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid @RequestBody Customer cust){
		return customerService.addCustomer(cust);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> getCustomers(){
		return customerRepository.findAll();
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
		if(!customerService.authorize(customerId, authorization)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return customerRepository.findOne(customerId);
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
			returnVal = customerService.getAddresses(customerId);
		}
		else{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			returnVal = new CustomerResponseBody(AUTHORIZATION_FAILED, null);
		}
		return returnVal;
	}
}


