package controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dto.EmployeeDetailsDTO;
import model.Address;
import model.Employee;
import service.AdminService;
import service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	EmployeeService employeeService;
	
	
	/**
	 * Adds an employee and address if the authorization header is valid for either an employee with the administrator type, 
	 * or someone using the administrator secret key. 
	 * 
	 * @param employee								Validated employee object created from the request body.
	 * @param authorization							Authorization header.
	 * @param response								HTTPServlet Response code.
	 * @return										Return Employee and success status if successful, returns error states if unsuccessful.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Employee addEmployee(@Valid @RequestBody Employee employee,
			@RequestHeader("Authorization") String authorization){
		return employeeService.addEmployee(employee, authorization);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Employee getEmployee(@PathVariable("id") Long id, 
			@RequestHeader("Authorization") String authorization){
		return employeeService.getEmployee(authorization, id);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Employee updateEmployee(@PathVariable("id") Long id,
			@RequestBody @Valid EmployeeDetailsDTO employeeDetails,
			@RequestHeader("Authorization") String authorization){
		return employeeService.updateEmployee(id, employeeDetails, authorization);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{id}/addresses")
	public Object updateHomeAddress(@PathVariable("id") Long id,
			@RequestBody @Valid Address address,
			@RequestHeader("Authorization") String authorization){
		return employeeService.updateAddress(authorization, id, address);
	}
}
