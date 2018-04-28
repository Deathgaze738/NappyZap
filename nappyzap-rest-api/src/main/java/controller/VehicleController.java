package controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dto.DepotVehicleDTO;
import model.DepotVehicle;
import model.EmployeeVehicle;
import model.Vehicle;
import service.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	@Autowired
	VehicleService vehicleService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Vehicle addVehicle(@Valid @RequestBody Vehicle vehicle,
			@RequestHeader("Authorization") String authorization){
		return vehicleService.addVehicle(authorization, vehicle);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Vehicle> getVehicles(@RequestHeader("Authorization") String authorization){
		return vehicleService.getVehicles(authorization);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Vehicle getVehicle(@RequestHeader("Authorization") String authorization,
			@PathVariable("id") Long id){
		return vehicleService.getVehicle(authorization, id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{vehicle}/depots/{depot}")
	public DepotVehicle addDepotVehicle(@PathVariable("vehicle") Long vehicleId,
			@PathVariable("depot") Long depotId,
			@RequestHeader("Authorization") String authorization){
		return vehicleService.addDepotVehicle(authorization, vehicleId, depotId);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{vehicle}/depots/{depot}")
	public void deleteDepotVehicle(@PathVariable("vehicle") Long vehicleId,
			@PathVariable("depot") Long depotId,
			@RequestHeader("Authorization") String authorization){
		vehicleService.deleteDepotVehicle(authorization, vehicleId, depotId);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{vehicle}/employees/{employee}")
	public EmployeeVehicle addEmployeeVehicle(@PathVariable("vehicle") Long vehicleId,
			@PathVariable("employee") Long employeeId,
			@RequestHeader("Authorization") String authorization){
		return vehicleService.addEmployeeVehicle(authorization, vehicleId, employeeId);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value ="/{vehicle}/employees/{employee}")
	public void deleteEmployeeVehicle(@PathVariable("vehicle") Long vehicleId,
			@PathVariable("employee") Long employeeId,
			@RequestHeader("Authorization") String authorization){
		vehicleService.deleteEmployeeVehicle(authorization, vehicleId, employeeId);
	}
}
