package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.TelephoneNumberType;

@Repository
public interface TelephoneNumberTypeRepository extends JpaRepository<TelephoneNumberType, Long>{

}
