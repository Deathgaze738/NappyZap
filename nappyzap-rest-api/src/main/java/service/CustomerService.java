package service;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import controller.CustomerResponseBody;
import dao.AddressRepository;
import dao.AddressTypeRepository;
import dao.CountryCodeRepository;
import dao.CustomerAddressRepository;
import dao.CustomerRepository;
import dao.CustomerTelephoneNumberRepository;
import dao.ResidenceTypeRepository;
import dao.SexRepository;
import dao.TelephoneNumberRepository;
import dao.TelephoneNumberTypeRepository;
import dto.CustomerAddressDTO;
import dto.CustomerTelephoneNumberDTO;
import dto.CustomerUpdateDTO;
import jar.Login;
import model.Address;
import model.AddressType;
import model.CountryCode;
import model.Customer;
import model.CustomerAddress;
import model.CustomerAddressID;
import model.CustomerTelephoneNumber;
import model.CustomerTelephoneNumberId;
import model.ResidenceType;
import model.TelephoneNumber;
import model.TelephoneNumberType;
import provider.GoogleMapProvider;

@Service
public class CustomerService {
	
	static final String SEX_NOT_VALID = "Sex is not valid";
	
	@Autowired
	CustomerRepository customerRepo;
	@Autowired
	SexRepository sexRepo;
	@Autowired
	CustomerAddressRepository customerAddressRepo;
	@Autowired
	AddressRepository addressRepo;
	@Autowired
	ResidenceTypeRepository residenceTypeRepo;
	@Autowired
	AddressTypeRepository addressTypeRepo;
	@Autowired
	CountryCodeRepository countryCodeRepo;
	@Autowired
	TelephoneNumberTypeRepository telephoneNumberTypeRepo;
	@Autowired
	TelephoneNumberRepository telephoneNumberRepo;
	@Autowired
	CustomerTelephoneNumberRepository customerTelephoneNumberRepo;
	@Autowired
	GoogleMapProvider googleMapProvider;
	
	/**
	 * Adds a new phone number to the customer, or replaces the phone number if the user
	 * already has a phone number of that type registered. 
	 * @param custId							ID of the customer to which the telephone number will be linked.
	 * @param custTeleDTO						DTO of the customer telephone update data
	 * @return									Returns null if the request is malformed, returns customer telephone number if the request succeeded.
	 */
	@Transactional(rollbackFor = Exception.class)
	public Object addPhoneNumber(Long custId, CustomerTelephoneNumberDTO custTeleDTO){
		Object returnVal = null;
		try{
			Customer cust = customerRepo.findOne(custId);
			TelephoneNumber number = custTeleDTO.getTelephoneNumber();
			telephoneNumberRepo.save(number);
			TelephoneNumberType type = telephoneNumberTypeRepo.findOne(custTeleDTO.getTypeId());
			//If Foreign key exists.
			if(type != null){
				CustomerTelephoneNumberId custTeleId = new CustomerTelephoneNumberId();
				custTeleId.setOwner(cust);
				custTeleId.setType(type);
				CustomerTelephoneNumber custTeleNo = customerTelephoneNumberRepo.findOne(custTeleId);
				//If customer already has a number of that type.
				if(custTeleNo == null){
					custTeleNo = new CustomerTelephoneNumber();
				}
				custTeleNo.setCustomerTelephoneNumberId(custTeleId);
				custTeleNo.setNumber(number);
				customerTelephoneNumberRepo.save(custTeleNo);
				returnVal = custTeleNo;
			}
		}catch(Exception e){

		}
		return returnVal;
	}
	
	/**
	 * Updates the relevant customer with the DTO provided.
	 * Should catch any constraint errors returned from the database.
	 * @param custId								ID of the customer to be updated.
	 * @param customerUpdateDTO						DTO of the information to update within customer.
	 * @return										True if DTO is well formed and constraints hold, false otherwise.
	 */
	public boolean editCustomer(Long custId, CustomerUpdateDTO customerUpdateDTO){
		try{
			Customer cust = customerRepo.findOne(custId);
			cust.setDate_of_birth(customerUpdateDTO.getDate_of_birth());
			cust.setFirst_name(customerUpdateDTO.getFirst_name());
			cust.setLast_name(customerUpdateDTO.getLast_name());
			cust.setSex_entity(sexRepo.findOne(customerUpdateDTO.getSex_id()));
			customerRepo.save(cust);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * Adds a new customer to the database.
	 * Password is hashed and salted using the BCryptEncoder class.
	 * An email confirmation token is created and emailed to the user for verification.
	 * @param cust						Customer object containing new user information.
	 * @return							Returns the customer object of the new user if successful, returns error CustomerResponseBody otherwise.
	 */
	public Object addCustomer(Customer cust){
		if(sexRepo.findOne(cust.getSex_entity().getId()) == null){
			return new CustomerResponseBody(SEX_NOT_VALID, cust);
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		cust.setPassword(encoder.encode(cust.getPassword()));
		cust.setEmail_confirmation_token(UUID.randomUUID().toString());
		customerRepo.save(cust);
		return cust;
	}
	
	/**
	 * Finds the CustomerAddress relation and deletes it. It should be noted that actual address details are not deleted straight away, 
	 * this is in order to ensure that users can't share accounts, and so important information regarding deliveries is not deleted. 
	 * However, all data regarding to whom each address belongs is deleted.
	 * @param addressId					Address ID of the address to be deleted.
	 * @param custId					Customer ID of the owner of the address to be deleted.
	 * @return							True if deleted successfully, false if the user isn't the owner of the address, or the address doesn't exist.
	 */
	@Transactional
	public boolean deleteAddress(Long addressId, Long custId){
		CustomerAddress custAddr = customerAddressRepo.findOneByCustomerAddressIdOwnerIdCustomerIdAndCustomerAddressIdAddressIdAddressId(custId, addressId);
		if(custAddr == null){
			return false;
		}
		customerAddressRepo.delete(custAddr);
		return true;
	}
	
	/**
	 * Deletes the phone number associated with the users account, and deletes the phone number from the phone number table.
	 * 
	 * @param phoneNumberId				ID of the phone number.
	 * @param custId					Id of the customer number.
	 * @return							True if deleted successfully, false if the isn't the owner of the phone number, or the number doesn't exist.
	 */
	public boolean deletePhoneNumber(Long phoneNumberId, Long custId){
		CustomerTelephoneNumber custPhone = customerTelephoneNumberRepo.findOneByCustomerTelephoneNumberIdOwnerCustomerIdAndNumberTelephoneNoId(custId, phoneNumberId);
		if(custPhone == null){
			return false;
		}
		customerTelephoneNumberRepo.delete(custPhone.getCustomerTelephoneNumberId());
		telephoneNumberRepo.delete(custPhone.getNumber());
		return true;
	}
	
	/**
	 * Deletes the customer from the database.
	 * When a customer is deleted, the database is set to cascade and delete all identifying relationships to the user.
	 * For example, all user address information is retained for billing, order history, and tax keeping purposes, but any ties 
	 * between a user and this information are completely deleted. Retaining the information we need, but deleting any identifiable mappings.
	 * @param custId					Id of the customer to be deleted.
	 */
	public void deleteCustomer(Long custId){
		customerRepo.delete(custId);
	}
	
	/**
	 * Gets a single customer from database
	 * @param custId					ID of the customer to be retrieved
	 * @return							Returns customer, null if customer doesn't exist.
	 */
	public Customer getCustomer(Long custId){
		return customerRepo.findOne(custId);
	}
	
	/**
	 * Handles the act of verifying the foreign keys, and inserting the new address and the address customer relation.
	 * If the user already has a delivery address associated with it, it is replaced with the new delivery address
	 * and recorded in the CustomerAddressHistory table.
	 * @param customerAddressDTO			DTO of CustomerAddress relationship
	 * @param cust							ID of the customer to which this address will be associated.
	 * @return								Returns CustomerAddressDTO of the new address if all conditions hold, else returns null.
	 */
	@Transactional(rollbackFor = Exception.class)
	public CustomerAddressDTO addAddress(CustomerAddressDTO customerAddressDTO, Long cust){
		Customer custObj = customerRepo.findOne(cust);
		Address address = customerAddressDTO.getAddress();
		CountryCode countryCode = address.getCountry_code();
		AddressType addressType = customerAddressDTO.getAddressType();
		ResidenceType residenceType = customerAddressDTO.getResidenceType();
		
		try{
			countryCode = countryCodeRepo.findOne(countryCode.getIso_code());
			addressType = addressTypeRepo.findOne(addressType.getAddressTypeId());
			residenceType = residenceTypeRepo.findOne(residenceType.getResidenceTypeId());
		}catch(IllegalArgumentException e){
			return null;
		}
		
		//Verify that all of the foreign keys exist.
		if(countryCode == null || addressType == null || residenceType == null){
			return null;
		}
		
		customerAddressDTO.setAddressType(addressType);
		customerAddressDTO.setResidenceType(residenceType);
		address.setCountry_code(countryCode);
		//Get Lat and Lng data from Google Maps.
		if(googleMapProvider.getLatLng(address) == null){
			return null;
		}
		
		//Check if the user already has a delivery address associated with this account.
		if(addressType.getAddressTypeId() == 2){
			CustomerAddress previousAddress = customerAddressRepo.findOneByAddressTypeAddressTypeIdAndCustomerAddressIdOwnerIdCustomerId(addressType.getAddressTypeId(), custObj.getId());
			if(previousAddress != null){
				customerAddressRepo.delete(previousAddress);
			}
		}
	
		//Save new address in the address table.
		addressRepo.save(address);
			
		//Set up customer address relation
		CustomerAddress customerAddress = new CustomerAddress();
		CustomerAddressID customerAddressID = new CustomerAddressID();
		customerAddressID.setAddressId(address);
		customerAddressID.setOwnerId(custObj);
		customerAddress.setCustomerAddressId(customerAddressID);
		customerAddress.setAddressType(addressType);
		customerAddress.setResidenceType(residenceType);
		customerAddress.setNotes(customerAddressDTO.getNotes());
		customerAddressRepo.save(customerAddress);
		return customerAddressDTO;
	}
	
	/**
	 * Authorizes the User using the Authorization header in the HTTP request. 
	 * @param customerId				This is the ID of the customer trying to be accessed.
	 * @param encoded					Password and Email Address encoded in Base64.
	 * @return							Returns true if the user is authorized, else false.
	 */
	public boolean authorize(Long customerId, String encoded){
		Customer cust = customerRepo.findOne(customerId);
		Login login = new Login(encoded);
		if(cust == null){
			return false;
		}
		if(!cust.getEmail_address().equals(login.getEmail_address()) || !cust.isEmail_verified()){
			return false;
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(login.getPassword(), cust.getPassword());
	}
	
	/**
	 * Simply return all addresses associated to the user.
	 * @param customerId					ID of the customer owning the addresses
	 * @return								Returns all address related to the customer id
	 */
	public List<CustomerAddress> getAddresses(Long customerId){
		return customerAddressRepo.findAllByCustomerAddressIdOwnerIdCustomerId(customerId);
	}
	
	/**
	 * Simply return all telephone numbers associated with the user.
	 * @param customerId					ID of the customer owning the telephone numbers.
	 * @return								Returns all telephone numbers related to the customer id.
	 */
	public List<CustomerTelephoneNumber> getTelephoneNumbers(Long customerId){
		return customerTelephoneNumberRepo.findAllByCustomerTelephoneNumberIdOwnerCustomerId(customerId);
	}
}
