package business.Order;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Card;
import model.Card.CardType;
import model.Continent;
import model.Country;
import model.MapModel;
import model.Player;

class BlockadeOrderTest {
	
	private BlockadeOrder blockade_order;
	
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
		
		Card c = new Card(CardType.BLOCKADE);
		
		P1.addSpecificCard(c);
		
		blockade_order = new BlockadeOrder(P1, d_Mexico);
	}

	@Test
	void testValid() {
		System.out.println("Before Validation Stage");
		P1.printCardList();
		
		assertTrue(blockade_order.valid());
		
		System.out.println("After Validation Stage");
		P1.printCardList();
	}
	
	@Test
	void testExecute() {
		blockade_order.execute();
		
		assertEquals(45, d_Mexico.getArmies());
		assertEquals(null, d_Mexico.getCountryOwner());
		
	}

}
