package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.EmployeeVehicle;
import model.EmployeeVehicleId;

@Repository
public interface EmployeeVehicleRepository extends JpaRepository<EmployeeVehicle, EmployeeVehicleId>{
	public EmployeeVehicle findOneByIdVehicleId(Long vehicleId);
}
