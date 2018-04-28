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

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "visit")
public class Visit {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "visit_id")
	private Long id;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	@NotNull
	private OrderStatus orderStatus;
	
	@Column(name = "pickup_time")
	private Timestamp pickupTime;
	
	@Column(name = "time_created")
	@CreationTimestamp
	private Timestamp timeCreated;
	
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
