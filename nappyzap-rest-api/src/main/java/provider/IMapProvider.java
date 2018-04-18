package provider;

import dto.RoadRouteDTO;
import model.Address;

public interface IMapProvider {
	public Address getLatLng(Address address);
	public RoadRouteDTO getRoadRoute(Address address1, Address address2);
}
