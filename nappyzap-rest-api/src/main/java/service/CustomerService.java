package service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.AddressDistanceRepository;
import dao.AddressRepository;
import dao.AddressTypeRepository;
import dao.CountryCodeRepository;
import dao.CustomerAddressRepository;
import dao.CustomerRepository;
import dao.CustomerTelephoneNumberRepository;
import dao.DepotRepository;
import dao.ResidenceTypeRepository;
import dao.SexRepository;
import dao.TelephoneNumberRepository;
import dao.TelephoneNumberTypeRepository;
import dto.CustomerAddressDTO;
import dto.CustomerTelephoneNumberDTO;
import dto.CustomerUpdateDTO;
import dto.RoadRouteDTO;
import dto.TelephoneNumberDTO;
import exception.MappingProviderException;
import exception.NotFoundException;
import exception.UnauthorizedException;
import jar.Login;
import model.Address;
import model.AddressDistance;
import model.AddressDistanceId;
import model.AddressType;
import model.CountryCode;
import model.Customer;
import model.CustomerAddress;
import model.CustomerAddressID;
import model.CustomerTelephoneNumber;
import model.CustomerTelephoneNumberId;
import model.Depot;
import model.ResidenceType;
import model.Shift;
import model.TelephoneNumber;
import model.TelephoneNumberType;
import provider.GoogleMapProvider;

@Service
public class CustomerService {
	
	static final String SEX_NOT_VALID = "Sex is not valid";
	private static final int MAX_TIME = 1800;
	
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
	DepotRepository depotRepo;
	@Autowired
	AddressDistanceRepository addressDistanceRepo;
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
	public TelephoneNumber addPhoneNumber(String authorization, Long custId, CustomerTelephoneNumberDTO custTeleDTO){
		Customer cust = authorize(custId, authorization);
		TelephoneNumber number = custTeleDTO.getTelephoneNumber();
		telephoneNumberRepo.save(number);
		TelephoneNumberType type = telephoneNumberTypeRepo.findOne(custTeleDTO.getTypeId());
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
		return number;
	}
	
	/**
	 * Updates the relevant customer with the DTO provided.
	 * Should catch any constraint errors returned from the database.
	 * @param custId								ID of the customer to be updated.
	 * @param customerUpdateDTO						DTO of the information to update within customer.
	 * @return										True if DTO is well formed and constraints hold, false otherwise.
	 */
	public Customer editCustomer(String authorization, Long custId, CustomerUpdateDTO customerUpdateDTO){
		Customer cust = authorize(custId, authorization);
		cust.setDate_of_birth(customerUpdateDTO.getDate_of_birth());
		cust.setFirst_name(customerUpdateDTO.getFirst_name());
		cust.setLast_name(customerUpdateDTO.getLast_name());
		cust.setSex_entity(sexRepo.findOne(customerUpdateDTO.getSex_id()));
		customerRepo.save(cust);
		return cust;
	}
	
	/**
	 * Adds a new customer to the database.
	 * Password is hashed and salted using the BCryptEncoder class.
	 * An email confirmation token is created and emailed to the user for verification.
	 * @param cust						Customer object containing new user information.
	 * @return							Returns the customer object of the new user if successful, returns error  otherwise.
	 */
	public Customer addCustomer(Customer cust){
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
	public void deleteAddress(String authorization, Long addressId, Long custId){
		authorize(custId, authorization);
		CustomerAddress custAddr = customerAddressRepo.findOneByCustomerAddressIdOwnerIdCustomerIdAndCustomerAddressIdAddressIdAddressId(custId, addressId);
		if(custAddr == null){
			throw new NotFoundException("Address with ID '" + addressId + "' does not exist, or does not belong to you.");
		}
		customerAddressRepo.delete(custAddr);
	}
	
	/**
	 * Deletes the phone number associated with the users account, and deletes the phone number from the phone number table.
	 * 
	 * @param phoneNumberId				ID of the phone number.
	 * @param custId					Id of the customer number.
	 * @return							True if deleted successfully, false if the isn't the owner of the phone number, or the number doesn't exist.
	 */
	@Transactional
	public void deletePhoneNumber(String authorization, Long phoneNumberId, Long custId){
		authorize(custId, authorization);
		CustomerTelephoneNumber custPhone = customerTelephoneNumberRepo.findOneByCustomerTelephoneNumberIdOwnerCustomerIdAndNumberTelephoneNoId(custId, phoneNumberId);
		if(custPhone == null){
			throw new NotFoundException("Telephone Number with ID '" + phoneNumberId + "' does not exist, or does not belong to you.");
		}
		customerTelephoneNumberRepo.delete(custPhone.getCustomerTelephoneNumberId());
		telephoneNumberRepo.delete(custPhone.getNumber());
	}
	
	/**
	 * Deletes the customer from the database.
	 * When a customer is deleted, the database is set to cascade and delete all identifying relationships to the user.
	 * For example, all user address information is retained for billing, order history, and tax keeping purposes, but any ties 
	 * between a user and this information are completely deleted. Retaining the information we need, but deleting any identifiable mappings.
	 * @param custId					Id of the customer to be deleted.
	 */
	public void deleteCustomer(String authorization, Long custId){
		authorize(custId, authorization);
		customerRepo.delete(custId);
	}
	
	/**
	 * Gets a single customer from database
	 * @param custId					ID of the customer to be retrieved
	 * @return							Returns customer, null if customer doesn't exist.
	 */
	public Customer getCustomer(String authorization, Long custId){
		authorize(custId, authorization);
		return customerRepo.findOne(custId);
	}
	
	public Depot getNearestDepot(Address address){
		List<Depot> depots = depotRepo.findAll();
		Depot closest = null;
		long shortestDistance = Long.MAX_VALUE;
		for(Depot depot : depots){
			RoadRouteDTO roadRoute = googleMapProvider.getRoadRoute(depot.getAddress(), address);
			if(roadRoute.getTimeTaken() < shortestDistance){
				shortestDistance = roadRoute.getTimeTaken();
				closest = depot;
			}
			if(roadRoute.getTimeTaken() < MAX_TIME){
				AddressDistanceId id = new AddressDistanceId();
				id.setAddress1(depot.getAddress());
				id.setAddress2(address);
				AddressDistance addressDistance = new AddressDistance();
				addressDistance.setId(id);
				addressDistance.setRoadDistance(roadRoute.getRoadDistance());
				addressDistance.setTimeTaken(roadRoute.getTimeTaken());
				addressDistanceRepo.save(addressDistance);
			}
		}
		return (shortestDistance < MAX_TIME) ? closest : null;
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
	public Address addAddress(String authorization, CustomerAddressDTO customerAddressDTO, Long cust){
		Customer custObj = authorize(cust, authorization);
		Address address = customerAddressDTO.getAddress();
		AddressType addressType = customerAddressDTO.getAddressType();
		ResidenceType residenceType = customerAddressDTO.getResidenceType();
		
		//Get Lat and Lng data from Google Maps.
		if(googleMapProvider.getLatLng(address) == null){
			throw new MappingProviderException("An error was encountered with our geocoding provider. Please contact a system administrator.");
		}
		//Save new address in the address table.
		addressRepo.save(address);
		//Check if the user already has a delivery address associated with this account.
		if(addressType.getAddressTypeId() == 2){
			CustomerAddress previousAddress = customerAddressRepo.findOneByAddressTypeAddressTypeIdAndCustomerAddressIdOwnerIdCustomerId(addressType.getAddressTypeId(), custObj.getId());
			if(previousAddress != null){
				customerAddressRepo.delete(previousAddress);
			}
			Depot closest = getNearestDepot(address);
			if(closest == null){
				throw new MappingProviderException("Your delivery address is not close enough to our depots, please keep up to date for a potential release in your area.");
			}
			address.setDepot(closest);
		}
			
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
		return address;
	}
	
	/**
	 * Authorizes the User using the Authorization header in the HTTP request. 
	 * @param customerId				This is the ID of the customer trying to be accessed.
	 * @param encoded					Password and Email Address encoded in Base64.
	 * @return							Returns true if the user is authorized, else false.
	 */
	public Customer authorize(Long customerId, String encoded){
		Customer cust = customerRepo.findOne(customerId);
		Login login = new Login(encoded);
		if(cust == null){
			throw new UnauthorizedException("Authorization header '" + encoded + "' is not valid.");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(!cust.getEmail_address().equals(login.getEmail_address()) || !cust.isEmail_verified() || !encoder.matches(login.getPassword(), cust.getPassword())){
			throw new UnauthorizedException("Customer with email '" + login.getEmail_address() + "' not authorized.");
		}
		return cust;
	}
	
	/**
	 * Simply return all addresses associated to the user.
	 * @param customerId					ID of the customer owning the addresses
	 * @return								Returns all address related to the customer id
	 */
	public List<CustomerAddressDTO> getAddresses(String authorization, Long customerId){
		authorize(customerId, authorization);
		List<CustomerAddress> addresses = customerAddressRepo.findAllByCustomerAddressIdOwnerIdCustomerId(customerId);
		List<CustomerAddressDTO> dtos = new ArrayList<CustomerAddressDTO>();
		for(CustomerAddress custAddr : addresses){
			CustomerAddressDTO dto = new CustomerAddressDTO();
			dto.setAddress(custAddr.getCustomerAddressId().getAddressId());
			dto.setAddressType(custAddr.getAddressType());
			dto.setResidenceType(custAddr.getResidenceType());
			dto.setNotes(custAddr.getNotes());
			dtos.add(dto);
		}
		return dtos;
	}
	
	/**
	 * Simply return all telephone numbers associated with the user.
	 * @param customerId					ID of the customer owning the telephone numbers.
	 * @return								Returns all telephone numbers related to the customer id.
	 */
	public List<TelephoneNumberDTO> getTelephoneNumbers(String authorization, Long customerId){
		authorize(customerId, authorization);
		List<CustomerTelephoneNumber> customerPhoneNumbers = customerTelephoneNumberRepo.findAllByCustomerTelephoneNumberIdOwnerCustomerId(customerId);
		List<TelephoneNumberDTO> telephoneNumbers = new ArrayList<TelephoneNumberDTO>();
		for(CustomerTelephoneNumber custTeleNum : customerPhoneNumbers){
			TelephoneNumberDTO dto = new TelephoneNumberDTO();
			dto.setTelephoneNumber(custTeleNum.getNumber());
			dto.setType(custTeleNum.getCustomerTelephoneNumberId().getType());
			telephoneNumbers.add(dto);
		}
		return telephoneNumbers;
	}
}
