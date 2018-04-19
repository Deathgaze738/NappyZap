package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.CustomerAddressHistory;
import model.CustomerAddressHistoryId;

@Repository
public interface CustomerAddressHistoryRepository extends JpaRepository<CustomerAddressHistory, CustomerAddressHistoryId>{

}
