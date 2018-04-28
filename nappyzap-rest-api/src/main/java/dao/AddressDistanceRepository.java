package dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.AddressDistance;
import model.AddressDistanceId;

@Repository
public interface AddressDistanceRepository extends JpaRepository<AddressDistance, AddressDistanceId>{
	
	@Override
	@Cacheable("distances")
	AddressDistance findOne(AddressDistanceId id);
}
