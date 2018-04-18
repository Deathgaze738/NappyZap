package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.ResidenceType;

@Repository
public interface ResidenceTypeRepository extends JpaRepository<ResidenceType, Long>{

}
