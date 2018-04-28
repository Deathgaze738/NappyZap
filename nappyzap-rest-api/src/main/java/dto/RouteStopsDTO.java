package dto;

import java.util.List;

import model.Route;
import model.RouteStop;

public class RouteStopsDTO {
	private Route route;
	private List<RouteStop> stops;
	
	public RouteStopsDTO(Route route, List<RouteStop> stops){
		this.route = route;
		this.stops = stops;
	}
	
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public List<RouteStop> getStops() {
		return stops;
	}
	public void setStops(List<RouteStop> stops) {
		this.stops = stops;
	}
}
