package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "customerstripe")
public class CustomerStripe {
	@Id
	private Long customerId;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Customer customer;
	
	@Column(name = "customer_stripe_code")
	private String stripeCode;

	public String getStripeCode() {
		return stripeCode;
	}

	public void setStripeCode(String stripeCode) {
		this.stripeCode = stripeCode;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
