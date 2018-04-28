package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.AddressRepository;
import dao.DepotRepository;
import dao.DepotVehicleRepository;
import exception.MappingProviderException;
import exception.NotFoundException;
import model.Address;
import model.Depot;
import model.DepotVehicle;
import model.Vehicle;
import provider.GoogleMapProvider;

@Service
public class DepotService {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	DepotRepository depotRepo;
	@Autowired
	AddressRepository addressRepo;
	@Autowired
	DepotVehicleRepository depotVehicleRepo;
	@Autowired
	GoogleMapProvider googleMapProvider;
	
	@Transactional
	public Depot addDepot(String authorization, Address depotAddress){
		employeeService.authorize(authorization);
		Depot depot = new Depot();
		depot.setAddress(depotAddress);
		//Get Lat and Lng data from Google Maps.
		if(googleMapProvider.getLatLng(depotAddress) == null){
			throw new MappingProviderException("An error was encountered with our geocoding provider. Please contact a system administrator.");
		}
		addressRepo.save(depotAddress);
		depotRepo.save(depot);
		return depot;
	}
	
	public Depot getDepot(String authorization, Long id){
		employeeService.authorize(authorization);
		Depot depot = depotRepo.findOne(id);
		if(depot == null){
			throw new NotFoundException("Depot with ID '" + id + "' was not found.");
		}
		return depot;
	}
	
	public List<Depot> getDepots(String authorization){
		employeeService.authorize(authorization);
		List<Depot> depots = depotRepo.findAll();
		if(depots.isEmpty()){
			throw new NotFoundException("No Depots found.");
		}
		return depots;
	}
	
	public Depot updateDepot(String authorization, Long id, Address newAddress){
		employeeService.authorize(authorization);
		Depot depot = depotRepo.findOne(id);
		if(depot == null){
			throw new NotFoundException("Depot with ID '" + id + "' was not found.");
		}
		//Get Lat and Lng data from Google Maps.
		if(googleMapProvider.getLatLng(newAddress) == null){
			throw new MappingProviderException("An error was encountered with our geocoding provider. Please contact a system administrator.");
		}
		Address oldAddress = depot.getAddress();
		addressRepo.save(newAddress);
		depot.setAddress(newAddress);
		depotRepo.save(depot);
		addressRepo.delete(oldAddress);
		return depot;
	}
	
	@Transactional
	public void deleteDepot(String authorization, Long id){
		employeeService.authorize(authorization);
		Depot depot = depotRepo.findOne(id);
		if(depot == null){
			throw new NotFoundException("Depot with ID '" + id + "' was not found.");
		}
		Address address = depot.getAddress();
		depotRepo.delete(id);
		addressRepo.delete(address);
	}
	
	public List<Vehicle> getDepotVehicles(String authorization, Long depotId){
		employeeService.authorize(authorization);
		List<DepotVehicle> depotVehicles = depotVehicleRepo.findAllByIdDepotId(depotId);
		if(depotVehicles.isEmpty()){
			throw new NotFoundException("No vehicles found for depot '" + depotId + "'.");
		}
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for(DepotVehicle depotVehicle : depotVehicles){
			vehicles.add(depotVehicle.getId().getVehicle());
		}
		return vehicles;
	}
}
