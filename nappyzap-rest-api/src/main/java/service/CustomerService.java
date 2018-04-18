package service;

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
import dao.ResidenceTypeRepository;
import dao.SexRepository;
import dto.CustomerAddressDTO;
import jar.Login;
import model.Address;
import model.AddressType;
import model.CountryCode;
import model.Customer;
import model.CustomerAddress;
import model.CustomerAddressID;
import model.ResidenceType;
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
	GoogleMapProvider googleMapProvider;
	
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
	 * @return							True is deleted successfully, false if the user isn't the owner of the address, or the address doesn't exist.
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
	 * Handles the act of verifying the foreign keys, and inserting the new address and the address customer relation.
	 * If the user already has a delivery address associated with it, it is replaced with the new delivery address
	 * and recorded in the CustomerAddressHistory table.
	 * @param customerAddressDTO			DTO of CustomerAddress relationship
	 * @param cust							ID of the customer to which this address will be associated.
	 * @return								Returns CustomerAddressDTO of the new address if all conditions hold, else returns null.
	 */
	@Transactional
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
	public Object getAddresses(Long customerId){
		return customerAddressRepo.findAllByCustomerAddressIdOwnerIdCustomerId(customerId);
	}
}
