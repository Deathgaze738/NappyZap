package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.AddressType;
import model.Customer;
import model.CustomerAddress;
import model.CustomerAddressID;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, CustomerAddressID>{
	public CustomerAddress findOneByAddressTypeAddressTypeIdAndCustomerAddressIdOwnerIdCustomerId(Long addressType, Long ownerId);
	public List<CustomerAddress> findAllByCustomerAddressIdOwnerIdCustomerId(@Param("ownerId")Long ownerId);
	public CustomerAddress findOneByCustomerAddressIdOwnerIdCustomerIdAndCustomerAddressIdAddressIdAddressId(Long customerId, Long addressId);
}
