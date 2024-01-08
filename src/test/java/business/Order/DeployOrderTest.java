package business.Order;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Continent;
import model.Country;
import model.MapModel;
import model.Player;

class DeployOrderTest {
	
	private DeployOrder deploy_order;
	
	private Player P1;
	
	private MapModel d_MM = new MapModel();
	
	private Continent d_America;
	
	private Country d_Canada, d_USA, d_Mexico;

	@BeforeEach
	void setUp() throws Exception {
		
		d_America = new Continent("North America");
		
		d_Canada = new Country("Canada", d_America);
		d_USA = new Country("USA", d_America);
		d_Mexico = new Country("Mexico", d_America);
		
		P1 = new Player("Kevin");
		
		d_MM.addContinent(d_America);
		
		d_MM.addContinentCountries(d_America, d_Canada);
		d_MM.addContinentCountries(d_America, d_USA);
		d_MM.addContinentCountries(d_America, d_Mexico);
		
		d_MM.addBorders(d_Canada, d_USA);
		
		d_MM.addBorders(d_USA, d_Canada);
		d_MM.addBorders(d_USA, d_Mexico);
		
		d_MM.addBorders(d_Mexico, d_USA);
		
		P1.addCountryHold(d_Canada);
		P1.addCountryHold(d_USA);
		P1.addCountryHold(d_Mexico);
		
		P1.getCountry(d_Canada).setArmy(5);
		P1.getCountry(d_USA).setArmy(10);
		P1.getCountry(d_Mexico).setArmy(15);
		
		P1.setArmiesToIssue(5);
		
		int allArmies = P1.getArmiesToIssue();
		
		deploy_order = new DeployOrder(d_Canada, allArmies, P1);
	}

	@Test
	void valid() {
		
		assertTrue(deploy_order.valid());

	}
	
	@Test
	void execute() {
		
		deploy_order.execute();
		
		assertEquals(10, d_Canada.getArmies());

	}

}
