package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>{

}
