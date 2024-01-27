package coffee.store.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class CoffeeStore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coffeeStoreId;
	private String coffeeStoreName;
	private String coffeeStoreAddress;
	private String coffeeStoreCity;
	private String coffeeStoreState;
	private String coffeeStoreZip;
	private String coffeeStorePhone;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "coffee_store_drink",
			joinColumns = @JoinColumn (name = "coffee_store_id"),
			inverseJoinColumns = @JoinColumn(name = "drink_id"))
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Drink> drinks = new HashSet<>();
	
	@OneToMany(mappedBy = "coffeeStore", cascade = CascadeType.ALL, 
			orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Customer> customers = new HashSet<>();
}

