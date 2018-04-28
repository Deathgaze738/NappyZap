package dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
	public List<Route> findAllByShiftIdAndDate(Long shiftId, Date date);
	public Route findOneByShiftIdAndDate(Long shiftId, Date date);
	public List<Route> findAllByDate(Date date);
}
