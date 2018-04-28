package model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "routestop")
public class RouteStop {
	@EmbeddedId
	private RouteStopId id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stop_address")
	private Address address;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Visit order;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public RouteStopId getId() {
		return id;
	}

	public void setId(RouteStopId id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Visit getOrder() {
		return order;
	}

	public void setOrder(Visit order) {
		this.order = order;
	}
}
