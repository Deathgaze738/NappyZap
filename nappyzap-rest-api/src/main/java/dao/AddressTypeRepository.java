package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.AddressType;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, Long>{
	
}
