package controller;

import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dto.CustomerAddressDTO;
import dto.CustomerTelephoneNumberDTO;
import dto.CustomerUpdateDTO;
import dto.OrderDTO;
import dto.ShiftAvailabilityDTO;
import dto.TelephoneNumberDTO;
import model.Address;
import model.Customer;
import model.TelephoneNumber;
import model.Visit;
import service.CustomerService;
import service.RoutingService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	@Autowired
	CustomerService customerService;
	
	@Autowired
	RoutingService routingService;
	
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
	@RequestMapping(value = "/{id}/addresses/{addr}", method = RequestMethod.DELETE)
	public void deleteAddress(@PathVariable(value = "id") Long customerId, 
			@PathVariable(value = "addr") Long addr, 
			@RequestHeader("Authorization") String authorization){
		customerService.deleteAddress(authorization, addr, customerId);
	}
	
	/**
	 * Checks if the user is authorized before returning all of their phone numbers.
	 * @param customerId						ID of the customer.
	 * @param authorization						Base 64 encoded authorization string
	 * @param response							Response servlet
	 * @return									Returns Authorization failed if user authorization fails, or user doesn't exist. Returns list of phone numbers otherwise.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/phonenos")
	public List<TelephoneNumberDTO> getPhoneNumbers(@PathVariable(value = "id") Long customerId, 
			@RequestHeader("Authorization") String authorization){
		return customerService.getTelephoneNumbers(authorization, customerId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Customer add(@Valid @RequestBody Customer cust){
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
	public void deleteCustomer(@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization){
		customerService.deleteCustomer(authorization, customerId);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/phonenos/{no}")
	public void deleteTelephoneNumber(@PathVariable(value = "id") Long customerId,
			@PathVariable(value = "no") Long telephoneId,
			@RequestHeader("Authorization") String authorization){
		customerService.deletePhoneNumber(authorization, telephoneId, customerId);
	}
	
	/**
	 * Gets a customer object and returns it after verifying the User credentials.
	 * @param customerId			ID for the customer data being retrieved.
	 * @param authorization			Base64 Encoded Authorization Header.
	 * @param response				HTTP Response Header.
	 * @return						Returns a customer object for the ID, or an Authorization error should this fail.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Customer getCustomer(@PathVariable(value = "id") Long customerId, 
			@RequestHeader("Authorization") String authorization){
		return customerService.getCustomer(authorization, customerId);
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
	public Address addAddress(@Valid @RequestBody CustomerAddressDTO customerAddressDTO, 
			@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization){
		return customerService.addAddress(authorization, customerAddressDTO, customerId);
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
	public Customer editCustomer(@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization,
			@RequestBody CustomerUpdateDTO customerUpdateDTO){
		return customerService.editCustomer(authorization, customerId, customerUpdateDTO);
	}
	
	
	/**
	 * Checks the users credentials in the Authorization header and returns all of their addresses if the authorization passes.
	 * @param customerId					ID of the customer looking for addresses.
	 * @param authorization					Authorization header of the HTTP request.
	 * @param response						Response header of the HTTP response.
	 * @return								Return addresses if authorized, return an error and forbidden response else.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/addresses")
	public List<CustomerAddressDTO> getAddresses(@PathVariable(value = "id") Long customerId, 
			@RequestHeader("Authorization") String authorization){
		return customerService.getAddresses(authorization, customerId);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/phonenos")
	public TelephoneNumber addPhoneNumber(@PathVariable(value = "id") Long customerId,
			@RequestHeader("Authorization") String authorization,
			@Valid @RequestBody CustomerTelephoneNumberDTO customerTelephoneNumberDTO){
		return customerService.addPhoneNumber(authorization, customerId, customerTelephoneNumberDTO);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/available")
	public List<ShiftAvailabilityDTO> getAvailability(@PathVariable(value = "id") Long customerId,
			@RequestParam("date") Date date,
			@RequestHeader("Authorization") String authorization){
		return routingService.checkAvailability(authorization, customerId, date);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/orders")
	public Visit addOrder(@PathVariable("id") Long customerId,
			@RequestBody @Valid OrderDTO orderDto,
			@RequestHeader("Authorization") String authorization){
		return routingService.addOrder(authorization, customerId, orderDto);
	}
}


