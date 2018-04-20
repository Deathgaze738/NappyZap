package controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dto.EmployeeDetailsDTO;
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
	
	private static final String EMPLOYEE_ADD_SUCCESS = "Employee added successfully!";
	private static final String EMPLOYEE_ADD_FAILED = "Employee add failed. Please ensure that the employee, sex, employee type are valid and address is well formed.";
	private static final String ADMIN_ONLY = "Unauthorized: Only an Administrator can add new employees.";
	private static final String EMPLOYEE_GET_FAILED = "Employee doesn't exist.";
	private static final String EMPLOYEE_UPDATE_FAILED = "Invalid Sex.";
	private static final String EMPLOYEE_UPDATE_SUCCESS = "Employee Details successfully updated!";
	private static final String UNAUTHORIZED = "Authorization failed.";
	
	
	/**
	 * Adds an employee and address if the authorization header is valid for either an employee with the administrator type, 
	 * or someone using the administrator secret key. 
	 * 
	 * @param employee								Validated employee object created from the request body.
	 * @param authorization							Authorization header.
	 * @param response								HTTPServlet Response code.
	 * @return										Return Employee and success status if successful, returns error states if unsuccessful.
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public Object addEmployee(@Valid @RequestBody Employee employee,
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response){
		Object returnVal = null;
		if(adminService.authorize(authorization) || employeeService.authorize(authorization, 0l)){
			returnVal = employeeService.addEmployee(employee);
			if(returnVal != null){
				response.setStatus(HttpServletResponse.SC_CREATED);
				returnVal = new CustomerResponseBody(EMPLOYEE_ADD_SUCCESS, null);
			}
			else{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				returnVal = new CustomerResponseBody(EMPLOYEE_ADD_FAILED, null);
			}
		}
		else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(ADMIN_ONLY, "Please contact the server administrator");
		}
		return returnVal;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Object getEmployee(@PathVariable("id") Long id, 
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response){
		Object returnVal = null;
		if(adminService.authorize(authorization) || employeeService.authorize(authorization, id)){
			response.setStatus(HttpServletResponse.SC_OK);
			returnVal = employeeService.getEmployee(id);
			if(returnVal == null){
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				returnVal = new CustomerResponseBody(EMPLOYEE_GET_FAILED, id);
			}
		}
		else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(ADMIN_ONLY, "Please contact the server administrator.");
		}
		return returnVal;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Object updateEmployee(@PathVariable("id") Long id,
			@RequestBody @Valid EmployeeDetailsDTO employeeDetails,
			@RequestHeader("Authorization") String authorization,
			HttpServletResponse response){
		Object returnVal = null;
		if(adminService.authorize(authorization) || employeeService.authorize(authorization, id)){
			response.setStatus(HttpServletResponse.SC_OK);
			returnVal = employeeService.updateEmployee(id, employeeDetails);
			if(returnVal == null){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				returnVal = new CustomerResponseBody(EMPLOYEE_UPDATE_FAILED, employeeDetails);
			}
			else{
				returnVal = new CustomerResponseBody(EMPLOYEE_UPDATE_SUCCESS, returnVal);
			}
		}
		else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			returnVal = new CustomerResponseBody(UNAUTHORIZED, null);
		}
		return returnVal;
	}
}
