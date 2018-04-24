package controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.ResidenceType;
import service.AdminService;
import service.EmployeeService;
import service.ResidenceTypeService;

@RestController
@RequestMapping("/residencetypes")
public class ResidenceTypeController {
	@Autowired
	ResidenceTypeService residenceTypeService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	AdminService adminService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResidenceType getResidenceType(@PathVariable("id") Long id){
		return residenceTypeService.getResidenceType(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<ResidenceType> getAllResidenceTypes(){
		return residenceTypeService.getResidenceTypes();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResidenceType addResidenceType(@RequestBody @Valid ResidenceType type, 
			@RequestHeader("Authorization") String authorization){
		return residenceTypeService.addResidenceType(authorization, type);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResidenceType updateResidenceType(@RequestBody @Valid ResidenceType type,
			@PathVariable("id") Long id,
			@RequestHeader("Authorization") String authorization){
		return residenceTypeService.updateResidenceType(authorization, id, type);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteResidenceType(@PathVariable("id") Long id,
			@RequestHeader("Authorization") String authorization){
		residenceTypeService.deleteResidenceType(authorization, id);
	}
}
