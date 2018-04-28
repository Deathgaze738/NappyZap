package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.DepotVehicle;
import model.DepotVehicleId;

@Repository
public interface DepotVehicleRepository extends JpaRepository<DepotVehicle, DepotVehicleId>{
	public List<DepotVehicle> findAllByIdDepotId(Long depotId);
	public DepotVehicle findOneByIdVehicleId(Long vehicleId);
}
