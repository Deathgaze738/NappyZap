package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private Long id;
	
	@ElementCollection(targetClass = OrderStatus.class)
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	@NotNull
	private OrderStatus orderStatus;
	
	@Column(name = "pickup_time")
	private Timestamp pickupTime;
	
	@NotNull
	@Column(name = "requested_time")
	private Timestamp requestTime;
	
	@NotNull
	@Column(name = "time_created")
	private Timestamp timeCreated;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Employee driver;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "delivery_address")
	private Address deliveryAddress;
	
	@Column(name = "notes")
	private String notes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Timestamp getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(Timestamp pickupTime) {
		this.pickupTime = pickupTime;
	}

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public Timestamp getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Employee getDriver() {
		return driver;
	}

	public void setDriver(Employee driver) {
		this.driver = driver;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
