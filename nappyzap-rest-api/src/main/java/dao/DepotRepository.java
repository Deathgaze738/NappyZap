package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Depot;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long>{

}
