package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.CountryCode;

@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode, String>{

}
