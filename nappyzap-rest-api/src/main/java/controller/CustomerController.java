package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Customer;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	private final AtomicLong counter = new AtomicLong();
	List<Customer> customers = new ArrayList<Customer>();
	
	@RequestMapping(method = RequestMethod.POST)
	public void add(@RequestParam(value="email_address", defaultValue="hello") String email_address,
			@RequestParam(value="password", defaultValue="world") String password){
		customers.add(new Customer(counter.incrementAndGet(), email_address, password));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{customerid}")
	public Customer getCustomer(@PathVariable Long customerid){
		for(Customer c : customers){
			if(c.getId() == customerid){
				return c;
			}
		}
		return null;
		
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public Collection<Customer> getCustomers(){
		return customers;
	}
}
