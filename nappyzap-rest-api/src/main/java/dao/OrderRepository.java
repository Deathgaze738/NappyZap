package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Visit;

@Repository
public interface OrderRepository extends JpaRepository<Visit, Long>{

}
