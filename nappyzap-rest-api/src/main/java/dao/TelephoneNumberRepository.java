package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.TelephoneNumber;

@Repository
public interface TelephoneNumberRepository extends JpaRepository<TelephoneNumber, Long>{

}
