package coffee.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import coffee.store.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {

}
