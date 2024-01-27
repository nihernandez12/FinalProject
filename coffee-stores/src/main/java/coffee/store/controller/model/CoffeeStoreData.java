package coffee.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import coffee.store.entity.CoffeeStore;
import coffee.store.entity.Customer;
import coffee.store.entity.Drink;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoffeeStoreData {
	private Long coffeeStoreId;
	private String coffeeStoreName;
	private String coffeeStoreAddress;
	private String coffeeStoreCity;
	private String coffeeStoreState;
	private String coffeeStoreZip;
	private String coffeeStorePhone;
	private Set<CoffeeStoreDrink> drinks = new HashSet<>();
	private Set<CoffeeStoreCustomer> customers = new HashSet<>();
	
	public CoffeeStoreData(CoffeeStore coffeeStore) {
		coffeeStoreId = coffeeStore.getCoffeeStoreId();
		coffeeStoreName = coffeeStore.getCoffeeStoreName();
		coffeeStoreAddress = coffeeStore.getCoffeeStoreAddress();
		coffeeStoreCity = coffeeStore.getCoffeeStoreCity();
		coffeeStoreState = coffeeStore.getCoffeeStoreState();
		coffeeStoreZip = coffeeStore.getCoffeeStoreZip();
		coffeeStorePhone = coffeeStore.getCoffeeStorePhone();
		
		for(Drink drink : coffeeStore.getDrinks()) {
			drinks.add(new CoffeeStoreDrink(drink));
		}
		
		for(Customer customer : coffeeStore.getCustomers()) {
			customers.add(new CoffeeStoreCustomer(customer));
		}
	}
@Data
@NoArgsConstructor
public static class CoffeeStoreDrink {
	private Long drinkId;
	private String drinkName;
	private String drinkDescription;
	private String drinkIngredients;
	
	public CoffeeStoreDrink(Drink drink) {
		drinkId = drink.getDrinkId();
		drinkName = drink.getDrinkName();
		drinkDescription = drink.getDrinkDescription();
		drinkIngredients = drink.getDrinkIngredients();
	}
 }

@Data
@NoArgsConstructor
public static class CoffeeStoreCustomer {
	private Long customerId;
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
	private String customerDrinkSuggestion;
	
	public CoffeeStoreCustomer(Customer customer) {
		customerId = customer.getCustomerId();
		customerFirstName = customer.getCustomerFirstName();
		customerLastName = customer.getCustomerLastName();
		customerEmail = customer.getCustomerEmail();
		customerDrinkSuggestion = customer.getCustomerDrinkSuggestion();
	}
}


}
