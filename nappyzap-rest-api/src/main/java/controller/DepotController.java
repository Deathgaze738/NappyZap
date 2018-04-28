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

import model.Address;
import model.Depot;
import model.Vehicle;
import service.DepotService;

@RestController
@RequestMapping("/depots")
public class DepotController {
	
	@Autowired
	DepotService depotService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Depot addDepot(@RequestBody @Valid Address depot,
			@RequestHeader("Authorization") String authorization){
		return depotService.addDepot(authorization, depot);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Depot getDepot(@PathVariable("id") Long id,
			@RequestHeader("Authorization") String authorization){
		return depotService.getDepot(authorization, id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Depot> getDepots(@RequestHeader("Authorization") String authorization){
		return depotService.getDepots(authorization);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Depot updateDepot(@PathVariable("id") Long id,
			@RequestBody @Valid Address newAddress,
			@RequestHeader("Authorization") String authorization){
		return depotService.updateDepot(authorization, id, newAddress);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteDepot(@PathVariable("id") Long id,
			@RequestHeader("Authorization") String authorization){
		depotService.deleteDepot(authorization, id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{id}/vehicles")
	public List<Vehicle> getDepotVehicles(@RequestHeader("Authorization") String authorization,
			@PathVariable("id") Long id){
		return depotService.getDepotVehicles(authorization, id);
	}
}
