package service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.DepotRepository;
import dao.DepotVehicleRepository;
import dao.EmployeeRepository;
import dao.EmployeeVehicleRepository;
import dao.VehicleRepository;
import exception.NotFoundException;
import model.Depot;
import model.DepotVehicle;
import model.DepotVehicleId;
import model.Employee;
import model.EmployeeVehicle;
import model.EmployeeVehicleId;
import model.Vehicle;

@Service
public class VehicleService {
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	VehicleRepository vehicleRepo;
	
	@Autowired
	DepotRepository depotRepo;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	DepotVehicleRepository depotVehicleRepo;
	
	@Autowired
	EmployeeVehicleRepository employeeVehicleRepo;
	
	public Vehicle addVehicle(String authorization, Vehicle vehicle){
		employeeService.authorize(authorization);
		vehicleRepo.save(vehicle);
		return vehicle;
	}
	
	public List<Vehicle> getVehicles(String authorization){
		employeeService.authorize(authorization);
		List<Vehicle> vehicles = vehicleRepo.findAll();
		if(vehicles.isEmpty()){
			throw new NotFoundException("No Vehicles found.");
		}
		return vehicles;
	}
	
	public Vehicle getVehicle(String authorization, Long id){
		employeeService.authorize(authorization);
		Vehicle vehicle = vehicleRepo.findOne(id);
		if(vehicle == null){
			throw new NotFoundException("Vehicle with Id'" + id + "' was not found.");
		}
		return vehicle;
	}
	
	@Transactional
	public DepotVehicle addDepotVehicle(String authorization, Long vehicleId, Long depotId){
		employeeService.authorize(authorization);
		Vehicle vehicle = vehicleRepo.findOne(vehicleId);
		if(vehicle == null){
			throw new NotFoundException("Vehicle with Id'" + vehicleId + "' was not found.");
		}
		Depot depot = depotRepo.findOne(depotId);
		if(depot == null){
			throw new NotFoundException("Depot with Id'" + depotId + "' was not found.");
		}
		DepotVehicle depotVehicle = depotVehicleRepo.findOneByIdVehicleId(vehicleId);
		//If vehicle is already associated with a depot, replace that association.
		if(depotVehicle != null){
			depotVehicleRepo.delete(depotVehicle);
		}
		DepotVehicleId depotVehicleId = new DepotVehicleId();
		depotVehicleId.setDepot(depot);
		depotVehicleId.setVehicle(vehicle);
		DepotVehicle newDepotVehicle = new DepotVehicle();
		newDepotVehicle.setId(depotVehicleId);
		depotVehicleRepo.save(newDepotVehicle);
		return newDepotVehicle;
	}
	
	@Transactional
	public void deleteDepotVehicle(String authorization, Long vehicleId, Long depotId){
		employeeService.authorize(authorization);
		Vehicle vehicle = vehicleRepo.findOne(vehicleId);
		//Check if vehicle and depot not found.
		if(vehicle == null){
			throw new NotFoundException("Vehicle with Id '" + vehicleId + "' was not found.");
		}
		Depot depot = depotRepo.findOne(depotId);
		if(depot == null){
			throw new NotFoundException("Depot with Id '" + depotId + "' was not found.");
		}
		DepotVehicleId depotVehicleId = new DepotVehicleId();
		depotVehicleId.setDepot(depot);
		depotVehicleId.setVehicle(vehicle);
		DepotVehicle depotVehicle = depotVehicleRepo.findOne(depotVehicleId);
		if(depotVehicle == null){
			throw new NotFoundException("There is no relationship between vehicle '" + vehicleId + "' and depot '" + depotId + "'.");
		}
		depotVehicleRepo.delete(depotVehicle);
	}
	
	@Transactional
	public EmployeeVehicle addEmployeeVehicle(String authorization, Long vehicleId, Long employeeId){
		employeeService.authorize(authorization);
		Vehicle vehicle = vehicleRepo.findOne(vehicleId);
		//Check if vehicle or employee is null
		if(vehicle == null){
			throw new NotFoundException("Vehicle with Id '" + vehicleId + "' was not found.");
		}
		Employee employee = employeeRepo.findOne(employeeId);
		if(employee == null){
			throw new NotFoundException("Employee with Id '" + employeeId + "' was not found.");
		}
		EmployeeVehicleId employeeVehicleId = new EmployeeVehicleId();
		employeeVehicleId.setEmployee(employee);
		employeeVehicleId.setVehicle(vehicle);
		EmployeeVehicle employeeVehicle = employeeVehicleRepo.findOneByIdVehicleId(vehicleId);
		if(employeeVehicle != null){
			employeeVehicleRepo.delete(employeeVehicle);
		}
		EmployeeVehicle newEmployeeVehicle = new EmployeeVehicle();
		newEmployeeVehicle.setId(employeeVehicleId);
		employeeVehicleRepo.save(newEmployeeVehicle);
		return newEmployeeVehicle;
	}
	
	@Transactional
	public void deleteEmployeeVehicle(String authorization, Long vehicleId, Long employeeId){
		employeeService.authorize(authorization);
		Vehicle vehicle = vehicleRepo.findOne(vehicleId);
		//Check if vehicle or employee is null
		if(vehicle == null){
			throw new NotFoundException("Vehicle with Id '" + vehicleId + "' was not found.");
		}
		Employee employee = employeeRepo.findOne(employeeId);
		if(employee == null){
			throw new NotFoundException("Employee with Id '" + employeeId + "' was not found.");
		}
		EmployeeVehicleId employeeVehicleId = new EmployeeVehicleId();
		employeeVehicleId.setEmployee(employee);
		employeeVehicleId.setVehicle(vehicle);
		EmployeeVehicle employeeVehicle = employeeVehicleRepo.findOne(employeeVehicleId);
		if(employeeVehicle == null){
			throw new NotFoundException("There is no relationship between vehicle '" + vehicleId + "' and vehicle '" + vehicleId + "'.");
		}
		employeeVehicleRepo.delete(employeeVehicleId);
	}
}
