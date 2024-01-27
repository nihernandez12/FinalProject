package coffee.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import coffee.store.entity.CoffeeStore;

public interface CoffeeStoreDao extends JpaRepository<CoffeeStore, Long> {

}
