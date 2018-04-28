package service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.AddressDistanceRepository;
import dao.CustomerAddressRepository;
import dao.CustomerRepository;
import dao.DepotRepository;
import dao.DepotVehicleRepository;
import dao.OrderRepository;
import dao.RouteRepository;
import dao.RouteStopRepository;
import dao.ShiftRepository;
import dto.OrderDTO;
import dto.RoadRouteDTO;
import dto.RouteStopsDTO;
import dto.ShiftAvailabilityDTO;
import exception.MappingProviderException;
import exception.NotFoundException;
import exception.RoutingException;
import jar.App;
import model.Address;
import model.AddressDistance;
import model.AddressDistanceId;
import model.Customer;
import model.CustomerAddress;
import model.Depot;
import model.Visit;
import model.OrderStatus;
import model.Route;
import model.RouteStop;
import model.RouteStopId;
import model.Shift;
import provider.GoogleMapProvider;

@Service
public class RoutingService {
	
	static Logger log = Logger.getLogger(App.class.getName());
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	CustomerAddressRepository customerAddressRepo;
	
	@Autowired
	AddressDistanceRepository distanceRepo;
	
	@Autowired
	DepotRepository depotRepo;
	
	@Autowired
	DepotVehicleRepository depotVehicleRepo;
	
	@Autowired
	ShiftRepository shiftRepo;
	
	@Autowired
	RouteRepository routeRepo;
	
	@Autowired
	RouteStopRepository routeStopRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	GoogleMapProvider googleMapProvider;
	
	@Transactional
	public Visit addOrder(String authorization, Long customerId, OrderDTO orderDto){
		Customer customer = customerService.authorize(customerId, authorization);
		Address deliveryAddress = (customerAddressRepo.findOneByAddressTypeAddressTypeIdAndCustomerAddressIdOwnerIdCustomerId(2L, customerId)).getCustomerAddressId().getAddressId();
		if(deliveryAddress == null){
			throw new NotFoundException("No delivery address registered to this account.");
		}
		Visit order = orderDto.getOrder();
		Shift shift = shiftRepo.findOne(orderDto.getShift().getId());
		if(shift == null){
			throw new NotFoundException("Shift with Id '" + orderDto.getShift().getId() + "' not found.");
		}
		order.setDeliveryAddress(deliveryAddress);
		order.setOrderStatus(OrderStatus.Pending);
		orderRepo.save(order);
		Route route = routeRepo.findOneByShiftIdAndDate(shift.getId(), orderDto.getDate());
		List<RouteStop> currentRoute = routeStopRepo.findAllByIdRouteId(route.getId());
		routeStopRepo.delete(currentRoute);
		RouteStopsDTO newRoute = computeRoute(deliveryAddress.getDepot(), route, currentRoute, shift.getEndTime().getTime() - shift.getStartTime().getTime(), deliveryAddress, order, customer);
		if(newRoute == null){
			throw new RoutingException("Unfortunately, this route is not available anymore.");
		}
		routeStopRepo.save(newRoute.getStops());
		return order;
	}
	
	public List<ShiftAvailabilityDTO> checkAvailability(String authorization, Long customerId, Date date){
		Customer cust = customerService.authorize(customerId, authorization);
		//If no date is passed or the date is before today, check availability for today.
		Calendar cal = Calendar.getInstance();
		Date curDate = new Date(cal.getTimeInMillis());
		cal.add(Calendar.MONTH, 1);
		Date maxDate = new Date(cal.getTimeInMillis());
		
		if(date == null || date.before(curDate) || date.after(maxDate)){
			date = curDate;
		}
		Address deliveryAddress = (customerAddressRepo.findOneByAddressTypeAddressTypeIdAndCustomerAddressIdOwnerIdCustomerId(2L, customerId).getCustomerAddressId().getAddressId());
		Depot closest = deliveryAddress.getDepot();
		List<ShiftAvailabilityDTO> availabilities = new ArrayList<ShiftAvailabilityDTO>();
		List<Shift> shifts = shiftRepo.findAll();
		for(Shift shift : shifts){
			boolean valid = false;
			for(Route route : routeRepo.findAllByShiftIdAndDate(shift.getId(), date)){
				Depot depot = (depotVehicleRepo.findOneByIdVehicleId(route.getVehicle().getId())).getId().getDepot();
				if(depot.getId() == closest.getId()){
					RouteStopsDTO potentialRoute = computeRoute(closest, route, routeStopRepo.findAllByIdRouteId(route.getId()), shift.getEndTime().getTime() - shift.getStartTime().getTime(), deliveryAddress, cust);
					if(potentialRoute != null){
						ShiftAvailabilityDTO availability = new ShiftAvailabilityDTO();
						availability.setAvailable(true);
						availability.setShift(shift);
						availabilities.add(availability);
						valid = true;
						break;
					}
				}
			}
			if(!valid){
				ShiftAvailabilityDTO availability = new ShiftAvailabilityDTO();
				availability.setAvailable(false);
				availability.setShift(shift);
				availabilities.add(availability);
			}
		}
		return availabilities;
	}
	
	public RouteStopsDTO computeRoute(Depot origin, Route route, List<RouteStop> stops, Long timeAvailable, Address newStop, Customer cust){
		return computeRoute(origin, route, stops, timeAvailable, newStop, null, cust);
	}
	
	public RouteStopsDTO computeRoute(Depot origin, Route route, List<RouteStop> stops, Long timeAvailable, Address newStop, Visit order, Customer cust){
		//Sort stops by order
		Long stopAllowance = 300L;
		Collections.sort(stops, (RouteStop s1, RouteStop s2) -> s1.getId().getStopNum() - s2.getId().getStopNum());
		List<Address> currentRoute = stops.stream().map(stop -> stop.getAddress()).collect(Collectors.toList());
		currentRoute.add(0, origin.getAddress());
		currentRoute.add(origin.getAddress());
		Long shortestAddition = Long.MAX_VALUE;
		
		int insertIndex = 0;
		//Uses nearest insertion order.
		for(int i = 0; i < currentRoute.size() - 1; i++){
			AddressDistance aToB = getRoadDistance(currentRoute.get(i), newStop);
			AddressDistance bToC = getRoadDistance(newStop, currentRoute.get(i+1));
			Long distance = aToB.getTimeTaken() + bToC.getTimeTaken();
			if(distance < shortestAddition){
				shortestAddition = distance;
				insertIndex = i;
			}
		}
		currentRoute.add(insertIndex + 1, newStop);
		//Get total route length and make sure it fits into the shift slot, with a small allowance for pickup and deliveries.
		Long currentTime = 0L;
		for(int i = 0; i < currentRoute.size() - 1; i++){
			currentTime += (getRoadDistance(currentRoute.get(i), currentRoute.get(i + 1))).getTimeTaken();
			currentTime += stopAllowance;
		}
		log.info(currentTime + ":" + timeAvailable);
		if(currentTime > timeAvailable){
			return null;
		}
		//Return route stops dtos signifying a route was valid.
		currentRoute.remove(0);
		currentRoute.remove(currentRoute.size() - 1);
		RouteStop insertStop = new RouteStop();
		insertStop.setAddress(newStop);
		insertStop.setOrder(order);
		insertStop.setCustomer(cust);
		RouteStopId insertStopId = new RouteStopId();
		insertStopId.setRoute(route);
		insertStopId.setStopNum(insertIndex);
		insertStop.setId(insertStopId);
		stops.add(insertIndex, insertStop);
		//Change the stop number of the remainder of the stops.
		List<RouteStop> newRouteStops = new ArrayList<RouteStop>();
		for(int i = 0; i < stops.size(); i++){
			RouteStop oldRouteStop = stops.get(i);
			RouteStopId id = new RouteStopId();
			id.setRoute(route);
			id.setStopNum(i);
			stops.get(i).setId(id);
			RouteStop newRouteStop = new RouteStop();
			newRouteStop.setAddress(oldRouteStop.getAddress());
			newRouteStop.setCustomer(oldRouteStop.getCustomer());
			newRouteStop.setOrder(oldRouteStop.getOrder());
			newRouteStop.setId(id);
			newRouteStops.add(newRouteStop);
		}
		for(RouteStop stop : stops){
			log.info(stop.getId().toString());
			log.info(stop.getAddress().getAddress_line_1());
		}
		return new RouteStopsDTO(route, newRouteStops);
	}
	
	public AddressDistance getRoadDistance(Address address1, Address address2){
		AddressDistanceId id = new AddressDistanceId();
		id.setAddress1(address1);
		id.setAddress2(address2);
		AddressDistance distance = distanceRepo.findOne(id);
		if(distance == null){
			RoadRouteDTO routeDto = googleMapProvider.getRoadRoute(address1, address2);
			if(routeDto == null){
				throw new MappingProviderException("Our mapping provider is having difficulties retrieving road road. Please try again later. If the problem persists, please contact our customer service email.");
			}
			else{
				distance = new AddressDistance();
				distance.setId(id);
				distance.setRoadDistance(routeDto.getRoadDistance());
				distance.setTimeTaken(routeDto.getTimeTaken());
				distance.setTimestamp(routeDto.getTimeRetrieved());
				distanceRepo.save(distance);
			}
		}
		return distance;
	}
	
	public Route routeTest(){
		return routeRepo.findOne(1L);
	}
}
