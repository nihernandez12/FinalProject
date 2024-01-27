package coffee.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import coffee.store.entity.Drink;

public interface DrinkDao extends JpaRepository<Drink, Long> {

}
