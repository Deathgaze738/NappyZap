package service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dao.AddressRepository;
import dao.EmployeeRepository;
import dao.EmployeeTypeRepository;
import dao.SexRepository;
import dto.EmployeeDetailsDTO;
import exception.NotFoundException;
import exception.UnauthorizedException;
import jar.Login;
import model.Address;
import model.Customer;
import model.Employee;
import model.EmployeeType;
import model.Sex;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	AddressRepository addressRepo;
	
	@Autowired
	EmployeeTypeRepository employeeTypeRepo;
	
	@Autowired
	SexRepository sexRepo;
	
	
	/**
	 * Checks if the user is authorized. Authorized actors are administrators, using the admin secret key, and users who own the account.
	 * @param encoded						Base64 encoding of the employee email address and password.
	 * @return								true is authorized, false if not.	
	 */
	public Employee authorize(String encoded, Long employeeId){
		Login login = new Login(encoded);
		Employee emp = employeeRepo.findOneByEmailAddress(login.getEmail_address());
		if(emp == null){
			throw new UnauthorizedException("Authorization header " + encoded + " is not valid.");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(((!emp.isActive() || !emp.getJobType().getRoleName().equals("Administrator")) && emp.getEmployeeId() != employeeId) || !encoder.matches(login.getPassword(), emp.getPassword())){
			throw new UnauthorizedException("Employee with email " + login.getEmail_address() + " is unauthorized.");
		}
		return emp;
	}
	
	public Employee authorize(String encoded){
		return authorize(encoded, 0L);
	}
	
	@Transactional
	public Employee addEmployee(Employee employee, String authorization){
		if(employeeRepo.findAll().size() != 0){
			authorize(authorization, 0L);
		}
		else{
			employee.setJobType(employeeTypeRepo.findOne(1L));
			employee.setActive(true);
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		employee.setPassword(encoder.encode(employee.getPassword()));
		addressRepo.save(employee.getHomeAddress());
		employeeRepo.save(employee);
		return employee;
	}
	
	public Employee getEmployee(String authorization, Long id){
		authorize(authorization, id);
		Employee emp = employeeRepo.findOne(id);
		if(emp == null){
			throw new NotFoundException("Employee with id '" + id + "' not found.");
		}
		return emp;
	}
	
	public Employee updateEmployee(Long id, EmployeeDetailsDTO employeeDetails, String authorization){
		authorize(authorization, id);
		Employee emp = employeeRepo.findOne(id);
		if(emp == null){
			throw new NotFoundException("Employee with id '" + id + "' not found.");
		}
		emp.setEmailAddress(employeeDetails.getEmailAddress());
		emp.setFirstName(employeeDetails.getFirstName());
		emp.setMiddleName(employeeDetails.getMiddleName());
		emp.setLastName(employeeDetails.getLastName());
		emp.setSex(employeeDetails.getSex());
		employeeRepo.save(emp);
		return emp;
	}
	
	@Transactional
	public Employee updateAddress(String authorization, Long empId, Address newAddress){
		authorize(authorization, empId);
		Employee emp = employeeRepo.findOne(empId);
		if(emp == null){
			throw new NotFoundException("Employee with id '" + empId + "' not found.");
		}
		Address oldAddress = emp.getHomeAddress();
		emp.setHomeAddress(newAddress);
		addressRepo.save(newAddress);
		employeeRepo.save(emp);
		addressRepo.delete(oldAddress);
		return emp;
	}
}
