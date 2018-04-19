package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.CustomerTelephoneNumber;
import model.CustomerTelephoneNumberId;

@Repository
public interface CustomerTelephoneNumberRepository extends JpaRepository<CustomerTelephoneNumber, CustomerTelephoneNumberId>{
	public List<CustomerTelephoneNumber> findAllByCustomerTelephoneNumberIdOwnerCustomerId(Long customerId);
	public CustomerTelephoneNumber findOneByCustomerTelephoneNumberIdOwnerCustomerIdAndNumberTelephoneNoId(Long customerId, Long telephoneNoId);
}
