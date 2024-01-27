package coffee.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coffee.store.controller.model.CoffeeStoreData;
import coffee.store.controller.model.CoffeeStoreData.CoffeeStoreCustomer;
import coffee.store.controller.model.CoffeeStoreData.CoffeeStoreDrink;
import coffee.store.dao.CoffeeStoreDao;
import coffee.store.dao.CustomerDao;
import coffee.store.dao.DrinkDao;
import coffee.store.entity.CoffeeStore;
import coffee.store.entity.Customer;
import coffee.store.entity.Drink;

@Service
public class CoffeeStoreService {
	@Autowired
	private CoffeeStoreDao coffeeStoreDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private DrinkDao drinkDao;
	
	@Transactional(readOnly = false)
	public CoffeeStoreData saveCoffeeStore(CoffeeStoreData coffeeStoreData) {
		Long coffeeStoreId = coffeeStoreData.getCoffeeStoreId();
		CoffeeStore coffeeStore = findOrCreateCoffeeStore(coffeeStoreId);
		
		copyCoffeeStoreFields(coffeeStore, coffeeStoreData);
		return new CoffeeStoreData(coffeeStoreDao.save(coffeeStore));
	}
	private void copyCoffeeStoreFields(CoffeeStore coffeeStore, 
			CoffeeStoreData coffeeStoreData) {
		coffeeStore.setCoffeeStoreAddress(coffeeStoreData.getCoffeeStoreAddress());
		coffeeStore.setCoffeeStoreCity(coffeeStoreData.getCoffeeStoreCity());
		coffeeStore.setCoffeeStoreId(coffeeStoreData.getCoffeeStoreId());
		coffeeStore.setCoffeeStoreName(coffeeStoreData.getCoffeeStoreName());
		coffeeStore.setCoffeeStorePhone(coffeeStoreData.getCoffeeStorePhone());
		coffeeStore.setCoffeeStoreState(coffeeStoreData.getCoffeeStoreState());
		coffeeStore.setCoffeeStoreZip(coffeeStoreData.getCoffeeStoreZip());
	}
	private CoffeeStore findOrCreateCoffeeStore(Long coffeeStoreId) {
		if(Objects.isNull(coffeeStoreId)) {
			return new CoffeeStore();
		}
		else {
			return findCoffeeStoreById(coffeeStoreId);
		}
	}
	
	private CoffeeStore findCoffeeStoreById(Long coffeeStoreId) {
		return coffeeStoreDao.findById(coffeeStoreId)
				.orElseThrow(() -> new NoSuchElementException(
						"Coffee store with ID=" + coffeeStoreId + " was not found."));
	}
	
	private void copyCustomerFields(Customer customer,
			CoffeeStoreCustomer coffeeStoreCustomer) {
		customer.setCustomerDrinkSuggestion(coffeeStoreCustomer.getCustomerDrinkSuggestion());
		customer.setCustomerEmail(coffeeStoreCustomer.getCustomerEmail());
		customer.setCustomerFirstName(coffeeStoreCustomer.getCustomerFirstName());
		customer.setCustomerId(coffeeStoreCustomer.getCustomerId());
		customer.setCustomerLastName(coffeeStoreCustomer.getCustomerLastName());
	}
	
	private void copyDrinkFields(Drink drink, CoffeeStoreDrink coffeeStoreDrink) {
		drink.setDrinkDescription(coffeeStoreDrink.getDrinkDescription());
		drink.setDrinkId(coffeeStoreDrink.getDrinkId());
		drink.setDrinkIngredients(coffeeStoreDrink.getDrinkIngredients());
		drink.setDrinkName(coffeeStoreDrink.getDrinkName());
		
}
	
	private Customer findOrCreateCustomer(Long coffeeStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
			return new Customer();
		}
		return findCustomerById(coffeeStoreId, customerId);
	}
	
	private Drink findOrCreateDrink(Long coffeeStoreId, Long drinkId) {
		if(Objects.isNull(drinkId)) {
			return new Drink();
		}
		return findDrinkById(coffeeStoreId, drinkId);
		}
	
	private Customer findCustomerById(Long coffeeStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(
				() -> new NoSuchElementException(
						"Customer with ID=" + customerId + " was not found."));
		
		if(customer.getCoffeeStore().getCoffeeStoreId() != coffeeStoreId) {
			throw new IllegalArgumentException("The customer with ID=" + customerId
					+ " is not a saved customer at the coffee store with ID=" + coffeeStoreId + ".");
		}
		return customer;
		
	}
	
	private Drink findDrinkById(Long coffeeStoreId, Long drinkId) {
		Drink drink = drinkDao.findById(drinkId)
				.orElseThrow(() -> new NoSuchElementException(
						"Drink with ID=" + drinkId + " was not found."));
		
		boolean found = false;
		
		for(CoffeeStore coffeeStore : drink.getCoffeeStores()) {
			if(coffeeStore.getCoffeeStoreId() == coffeeStoreId) {
				found = true;
				break;
			}
		}
		
		if(!found) {
			throw new IllegalArgumentException("The drink with ID=" + drinkId 
					+ " is not a drink at the coffee store with ID=" + coffeeStoreId);
		}
		return drink;
	}
	
	@Transactional(readOnly = false)
	public CoffeeStoreCustomer saveCustomer(Long coffeeStoreId,
			CoffeeStoreCustomer coffeeStoreCustomer) {
		CoffeeStore coffeeStore = findCoffeeStoreById(coffeeStoreId);
		Long customerId = coffeeStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(coffeeStoreId, customerId);
		
		copyCustomerFields(customer, coffeeStoreCustomer);
		
		customer.setCoffeeStore(coffeeStore);
		coffeeStore.getCustomers().add(customer);
		
		Customer dbCustomer = customerDao.save(customer);
		
		return new CoffeeStoreCustomer(dbCustomer);
	}
	
	@Transactional
	public CoffeeStoreDrink saveDrink(Long coffeeStoreId, CoffeeStoreDrink coffeeStoreDrink) {
		CoffeeStore coffeeStore = findCoffeeStoreById(coffeeStoreId); 
		Long drinkId = coffeeStoreDrink.getDrinkId();
		Drink drink = findOrCreateDrink(coffeeStoreId, drinkId);
		
		copyDrinkFields(drink, coffeeStoreDrink);
		
		drink.getCoffeeStores().add(coffeeStore);
		coffeeStore.getDrinks().add(drink);
		
		Drink dbDrink = drinkDao.save(drink);
		
		return new CoffeeStoreDrink(dbDrink);
		
	}
	
	@Transactional(readOnly = true)
	public List<CoffeeStoreData> retrieveAllCoffeeStores() {
		List<CoffeeStore> coffeeStores = coffeeStoreDao.findAll();
		
		List<CoffeeStoreData> result = new LinkedList<>();
		
		for(CoffeeStore coffeeStore : coffeeStores) {
			CoffeeStoreData csd = new CoffeeStoreData(coffeeStore);
			
			csd.getDrinks().clear();
			csd.getCustomers().clear();
			
			result.add(csd);
		}
		return result;
	}
	
	@Transactional(readOnly = true)
	public CoffeeStoreData retrieveCoffeeStoreById(Long coffeeStoreId) {
		return new CoffeeStoreData(findCoffeeStoreById(coffeeStoreId));
		
	}
	
	@Transactional(readOnly = false)
	public void deleteCoffeeStoreById(Long coffeeStoreId) {
		CoffeeStore coffeeStore = findCoffeeStoreById(coffeeStoreId);
		coffeeStoreDao.delete(coffeeStore);
	}
	
}
