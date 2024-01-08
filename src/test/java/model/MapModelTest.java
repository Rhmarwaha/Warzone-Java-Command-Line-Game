package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logger.ConsoleWriter;
import logger.LogEntryBuffer;
import model.Continent;
import model.Country;
import model.MapModel;


/**
 * JUNIT test class to test the following:
 * 1. Prove a continent is a connected graph using DFS (Depth First Search)
 * 2. Prove that adding a country that already exists in the map does not create a duplication
 * @author Kevin
 * @version build 1
 * 
 */
class MapModelTest {
	
	private MapModel d_MM = new MapModel();
	private MapModel d_MapT = new MapModel(); //used in void ConnectGraphMap() only
	private static int d_test_number = 1;


	@BeforeEach
	void setUp() {
		
		
		System.out.println("Starting Test: " + d_test_number);
		System.out.println("-----------------------");
		
		
		Continent l_America = new Continent("America");
		
		Country l_Canada = new Country("Canada", l_America);
		Country l_USA = new Country("USA", l_America);
		Country l_Mexico = new Country("Mexico", l_America);
		Country l_Guatemala = new Country("Guatemala", l_America);
		Country l_Nicaragua = new Country("Nicaragua", l_America);
		Country l_Colombia = new Country("Colombia", l_America);
		Country l_Venezuela = new Country("Venezuela", l_America);
		Country l_Ecuador = new Country("Ecuador", l_America);
		Country l_Peru = new Country("Peru", l_America);
		Country l_Brazil = new Country("Brazil", l_America);
		
		
		d_MM.addContinent(l_America);
		
		d_MM.addContinentCountries(l_America, l_Canada);
		d_MM.addContinentCountries(l_America, l_USA);
		d_MM.addContinentCountries(l_America, l_Mexico);
		d_MM.addContinentCountries(l_America, l_Guatemala);
		d_MM.addContinentCountries(l_America, l_Nicaragua);
		d_MM.addContinentCountries(l_America, l_Colombia);
		d_MM.addContinentCountries(l_America, l_Venezuela);
		d_MM.addContinentCountries(l_America, l_Ecuador);
		d_MM.addContinentCountries(l_America, l_Peru);
		d_MM.addContinentCountries(l_America, l_Brazil);
		
		d_MM.addBorders(l_Canada, l_USA);
		
		d_MM.addBorders(l_USA, l_Canada);
		d_MM.addBorders(l_USA, l_Mexico);
		
		d_MM.addBorders(l_Mexico, l_USA);
		d_MM.addBorders(l_Mexico, l_Guatemala);
		
		d_MM.addBorders(l_Guatemala, l_Mexico);
		d_MM.addBorders(l_Guatemala, l_Nicaragua);
		
		d_MM.addBorders(l_Nicaragua, l_Guatemala);
		d_MM.addBorders(l_Nicaragua, l_Colombia);
		
		d_MM.addBorders(l_Colombia, l_Nicaragua);
		d_MM.addBorders(l_Colombia, l_Venezuela);
		d_MM.addBorders(l_Colombia, l_Ecuador);
		d_MM.addBorders(l_Colombia, l_Brazil);
		d_MM.addBorders(l_Colombia, l_Peru);
		
		d_MM.addBorders(l_Venezuela, l_Colombia);
		d_MM.addBorders(l_Venezuela, l_Brazil);
		
		d_MM.addBorders(l_Ecuador, l_Colombia);
		d_MM.addBorders(l_Ecuador, l_Peru);
		
		d_MM.addBorders(l_Peru, l_Ecuador);
		d_MM.addBorders(l_Peru, l_Colombia);
		d_MM.addBorders(l_Peru, l_Brazil);
		
		d_MM.addBorders(l_Brazil, l_Peru);
		d_MM.addBorders(l_Brazil, l_Colombia);
		d_MM.addBorders(l_Brazil, l_Venezuela);
		
		
	}

	@AfterEach
	void tearDown() {
		++d_test_number;
		d_MM.clearMap();
		System.out.println("\n\n");
	}
	
	
	/**
	 * Method to assert that the continent is a connected graph
	 */
	@Test
	void ConnectedGraphContinent() {
		
		assertTrue(ConnectGraphTraversal(d_MM));
		
	}
	
	/**
	 * Method to assert that the map is a connected graph.
	 * This is similar to connected continent but this time the map will
	 * contain 2 continents with their countries, with 1 link between
	 * Brazil and India which allows the depth first search algorithm to traverse
	 * the whole map.
	 */
	@Test
	void ConnectGraphMap() {
		
		
		
		Continent l_America = new Continent("America");
		
		Country l_Canada = new Country("Canada", l_America);
		Country l_USA = new Country("USA", l_America);
		Country l_Mexico = new Country("Mexico", l_America);
		Country l_Guatemala = new Country("Guatemala", l_America);
		Country l_Nicaragua = new Country("Nicaragua", l_America);
		Country l_Colombia = new Country("Colombia", l_America);
		Country l_Venezuela = new Country("Venezuela", l_America);
		Country l_Ecuador = new Country("Ecuador", l_America);
		Country l_Peru = new Country("Peru", l_America);
		Country l_Brazil = new Country("Brazil", l_America);
		
		Continent l_Asia = new Continent(2,"Asia");
		
		Country l_China = new Country("China", l_Asia);
		Country l_India = new Country("India", l_Asia);
		Country l_Japan = new Country("Japan", l_Asia);
		Country l_Korea = new Country("Korea", l_Asia);
		
		d_MapT.addContinent(l_America);
		
		d_MapT.addContinentCountries(l_America, l_Canada);
		d_MapT.addContinentCountries(l_America, l_USA);
		d_MapT.addContinentCountries(l_America, l_Mexico);
		d_MapT.addContinentCountries(l_America, l_Guatemala);
		d_MapT.addContinentCountries(l_America, l_Nicaragua);
		d_MapT.addContinentCountries(l_America, l_Colombia);
		d_MapT.addContinentCountries(l_America, l_Venezuela);
		d_MapT.addContinentCountries(l_America, l_Ecuador);
		d_MapT.addContinentCountries(l_America, l_Peru);
		d_MapT.addContinentCountries(l_America, l_Brazil);
		
		d_MapT.addBorders(l_Canada, l_USA);
		
		d_MapT.addBorders(l_USA, l_Canada);
		d_MapT.addBorders(l_USA, l_Mexico);
		
		d_MapT.addBorders(l_Mexico, l_USA);
		d_MapT.addBorders(l_Mexico, l_Guatemala);
		
		d_MapT.addBorders(l_Guatemala, l_Mexico);
		d_MapT.addBorders(l_Guatemala, l_Nicaragua);
		
		d_MapT.addBorders(l_Nicaragua, l_Guatemala);
		d_MapT.addBorders(l_Nicaragua, l_Colombia);
		
		d_MapT.addBorders(l_Colombia, l_Nicaragua);
		d_MapT.addBorders(l_Colombia, l_Venezuela);
		d_MapT.addBorders(l_Colombia, l_Ecuador);
		d_MapT.addBorders(l_Colombia, l_Brazil);
		d_MapT.addBorders(l_Colombia, l_Peru);
		
		d_MapT.addBorders(l_Venezuela, l_Colombia);
		d_MapT.addBorders(l_Venezuela, l_Brazil);
		
		d_MapT.addBorders(l_Ecuador, l_Colombia);
		d_MapT.addBorders(l_Ecuador, l_Peru);
		
		d_MapT.addBorders(l_Peru, l_Ecuador);
		d_MapT.addBorders(l_Peru, l_Colombia);
		d_MapT.addBorders(l_Peru, l_Brazil);
		
		d_MapT.addBorders(l_Brazil, l_Peru);
		d_MapT.addBorders(l_Brazil, l_Colombia);
		d_MapT.addBorders(l_Brazil, l_Venezuela);
		d_MapT.addBorders(l_Brazil, l_India);
		
		//Asia
		
		d_MapT.addContinent(l_Asia);
		
		d_MapT.addContinentCountries(l_Asia, l_China);
		d_MapT.addContinentCountries(l_Asia, l_India);
		d_MapT.addContinentCountries(l_Asia, l_Japan);
		d_MapT.addContinentCountries(l_Asia, l_Korea);
		
		d_MapT.addBorders(l_India, l_China);
		d_MapT.addBorders(l_India, l_Brazil);
		
		d_MapT.addBorders(l_China, l_India);
		d_MapT.addBorders(l_China, l_Japan);
		d_MapT.addBorders(l_China, l_Korea);
		
		d_MapT.addBorders(l_Japan, l_China);
		
		d_MapT.addBorders(l_Korea, l_China);
		
		
		assertTrue(ConnectGraphTraversal(d_MapT));
	}
	
	/**
	 * Method for initializing the parameters and pass them to the depth first search algorithm
	 * @param d_MM
	 * @return boolean value from DFS that demonstrates all countries have been visited
	 */
	public static boolean ConnectGraphTraversal(MapModel p_MM) {
		if(p_MM.getCountries().isEmpty()) {
			return true;
		}
		
		Set<Country> visited = new HashSet<>(); 
		
		System.out.println("Starting Country: " + p_MM.getCountries().get(0).getCountryId());
		
		return DFS(p_MM, p_MM.getCountries().get(0), visited);
	}
	
	/**
	 * DFS: Depth First Search algorithm to ensure that the continent is a connected graph of countries
	 * The visited set only adds a country if that country has never been visited before.
	 * A set also prevents duplication if we want to add a country that is already contained in that set
	 * 
	 * @param d_MM Map Model
	 * @param country
	 * @param visited
	 * @return boolean value to ensure the search has visited every country in the continent
	 */
	public static boolean DFS(MapModel p_MM, Country p_country, Set<Country> p_visited) {
		
		p_visited.add(p_country);
		
		for(Country l_neighbor : p_MM.getBorders().get(p_country)) {
			if(!p_visited.contains(l_neighbor)) {
				System.out.println(l_neighbor.getCountryId());
				DFS(p_MM, l_neighbor, p_visited);
			}
		}
		
		return p_visited.size() == p_MM.getCountries().size();
		
	}
	
	/**
	 * Prove that a player that inserts a country that's already in that continent such that it does not create a duplicate
	 * The country Canada has already been added in the setUp() phase, so I created another Country variable called Canada again
	 * to add to the map.
	 * If Canada is already in the map, it should disregard that addition.
	 * To verify that Canada is only contained once in the list, iterate through the whole country list and increment the count by 1
	 * every time Canada is in the list and make an assertion that the count is less than 2.
	 */
	
	@Test
	void existingCountry() {
		
		int count = 0;
		
		Continent l_America = new Continent("America");
		
		Country l_Canada = new Country("Canada", l_America);
		
		d_MM.addContinentCountries(l_America, l_Canada);
		
		for(Country country : d_MM.getCountries()) {
			System.out.println(country.getCountryId());
			if(country == l_Canada) {
				count++;
			}
		}
		
		assertFalse(count > 1);
		
	}
	
	/**
	 * Method to assert that the same instance is returned when obtaining two instances.
	 */
	@Test
	void testMapModelSingleton() {

		MapModel l_mapModel1 = MapModel.getInstance();
		MapModel l_mapModel2 = MapModel.getInstance();

		assertSame(l_mapModel1, l_mapModel2);
	}
	
	
	/**
	 * Tescase to verify the ObserverPattern Implementation
	 */
	@Test
	public void testObserverPattern() {
		LogEntryBuffer l_logBuffer = new LogEntryBuffer();

		ConsoleWriter l_mockObserver1 = new ConsoleWriter();
		ConsoleWriter l_mockObserver2 = new ConsoleWriter();
		
		

		l_logBuffer.addObserver(l_mockObserver1);
		l_logBuffer.addObserver(l_mockObserver2);

		l_logBuffer.setLogMessage("Test Observer Pattern");

		
		assertEquals("Test Observer Pattern",l_mockObserver1.getLogMsg());
		assertEquals("Test Observer Pattern",l_mockObserver2.getLogMsg());
	}
	

}
