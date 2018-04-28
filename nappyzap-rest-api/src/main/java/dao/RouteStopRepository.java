package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.RouteStop;
import model.RouteStopId;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, RouteStopId>{
	public List<RouteStop> findAllByIdRouteId(Long routeId);
	public void deleteAllByIdRouteId(Long routeId);
}
