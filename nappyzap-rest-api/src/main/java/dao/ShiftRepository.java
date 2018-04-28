package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>{

}
