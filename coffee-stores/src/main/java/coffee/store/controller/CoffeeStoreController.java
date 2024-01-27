package coffee.store.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import coffee.store.controller.model.CoffeeStoreData;
import coffee.store.controller.model.CoffeeStoreData.CoffeeStoreCustomer;
import coffee.store.controller.model.CoffeeStoreData.CoffeeStoreDrink;
import coffee.store.service.CoffeeStoreService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/coffee_store")
@Slf4j
public class CoffeeStoreController {
	@Autowired
	private CoffeeStoreService coffeeStoreService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CoffeeStoreData insertCoffeeStore(@RequestBody CoffeeStoreData coffeeStoreData) {
		log.info("Creating coffee store {}", coffeeStoreData);
		return coffeeStoreService.saveCoffeeStore(coffeeStoreData);
	}
	
	@PutMapping("/{coffeeStoreId}")
	public CoffeeStoreData updateCoffeeStore(@PathVariable Long coffeeStoreId,
			@RequestBody CoffeeStoreData coffeeStoreData) {
		coffeeStoreData.setCoffeeStoreId(coffeeStoreId);
		log.info("Updating coffee store []", coffeeStoreData);
		return coffeeStoreService.saveCoffeeStore(coffeeStoreData);
	}

	@PostMapping("/{coffeeStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CoffeeStoreCustomer addCustomerToCoffeeStore(@PathVariable Long coffeeStoreId,
			@RequestBody CoffeeStoreCustomer coffeeStoreCustomer) {
		log.info("Adding customer {} to coffee store with ID={}", coffeeStoreCustomer,
				coffeeStoreId);
		
		return coffeeStoreService.saveCustomer(coffeeStoreId, coffeeStoreCustomer);
	}
	
	@PostMapping("/{coffeeStoreId}/drink")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CoffeeStoreDrink addDrinkToCoffeeStore(@PathVariable Long coffeeStoreId,
			@RequestBody CoffeeStoreDrink coffeeStoreDrink) {
		log.info("Adding drink {} to coffee store with ID={}", coffeeStoreDrink,
				coffeeStoreId);
		
		return coffeeStoreService.saveDrink(coffeeStoreId, coffeeStoreDrink);
		
	}
	
	@GetMapping
	public List<CoffeeStoreData> retrieveAllCoffeeStores() {
		log.info("Retrieving all coffee stores");
		return coffeeStoreService.retrieveAllCoffeeStores();
	}
	
	@GetMapping("/{coffeeStoreId}")
	public CoffeeStoreData retrieveCoffeeStoreById(@PathVariable Long coffeeStoreId) {
		log.info("Retrieving coffee store with ID={}", coffeeStoreId);
		return coffeeStoreService.retrieveCoffeeStoreById(coffeeStoreId);
	}
	
	
	@DeleteMapping("/{coffeeStoreId}")
	public Map<String, String> deleteCoffeeStoreById(@PathVariable Long coffeeStoreId) {
	log.info("Deleting coffee store with ID={}", coffeeStoreId);
	
	coffeeStoreService.deleteCoffeeStoreById(coffeeStoreId);
	
	return Map.of("message", "Coffee store with ID-" + coffeeStoreId + " deleted.");
}
}
