package service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dao.AddressRepository;
import dao.EmployeeRepository;
import dao.EmployeeTypeRepository;
import dao.SexRepository;
import dto.EmployeeDetailsDTO;
import jar.Login;
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
	public boolean authorize(String encoded, Long employeeId){
		Login login = new Login(encoded);
		Employee emp = employeeRepo.findOneByEmailAddress(login.getEmail_address());
		if(emp == null){
			return false;
		}
		if((!emp.getEmailAddress().equals(login.getEmail_address()) || !emp.isActive() || !emp.getJobType().getRoleName().equals("Administrator")) && emp.getEmployeeId() != employeeId){
			return false;
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(login.getPassword(), emp.getPassword());
	}
	
	@Transactional
	public Object addEmployee(Employee employee){
		EmployeeType type = employeeTypeRepo.findOne(employee.getJobType().getTypeId());
		Sex sex = sexRepo.findOne(employee.getSex().getId());
		if(sex == null || type == null){
			return null;
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		employee.setPassword(encoder.encode(employee.getPassword()));
		addressRepo.save(employee.getHomeAddress());
		employeeRepo.save(employee);
		return employee;
	}
	
	public Employee getEmployee(Long id){
		return employeeRepo.findOne(id);
	}
	
	public Object updateEmployee(Long id, EmployeeDetailsDTO employeeDetails){
		Employee emp = employeeRepo.findOne(id);
		if(sexRepo.exists(employeeDetails.getSex().getId())){
			emp.setEmailAddress(employeeDetails.getEmailAddress());
			emp.setFirstName(employeeDetails.getFirstName());
			emp.setMiddleName(employeeDetails.getMiddleName());
			emp.setLastName(employeeDetails.getLastName());
			emp.setSex(employeeDetails.getSex());
			employeeRepo.save(emp);
		}
		else{
			return null;
		}
		return emp;
	}
}
