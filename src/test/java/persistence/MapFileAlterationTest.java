package persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import GamePhase.MapPhaseState;
import model.Continent;
import model.Country;
import persistence.MapFileAlteration;

/**
 * JUNIT test class to test the following:
 * 1. Prove that the map is valid when a country is deleted
 * 2. Prove that the map is valid when a continent is deleted
 * @author Kevin
 * @version build 1
 *
 */
class MapFileAlterationTest {
	
	static int d_test_number = 1;
	
	MapFileAlteration d_MFA;
	Continent d_America;
	Country d_Canada, d_USA, d_Mexico, d_Guatemala, d_Nicaragua, d_Colombia, d_Venezuela, d_Ecuador, d_Peru, d_Brazil;
	
	Continent d_Asia;
	Country d_China, d_India, d_Japan, d_Korea;
	
	Continent d_Europe;
	Country d_France;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
		System.out.println("Starting Test: " + d_test_number);
		
		d_MFA = new MapFileAlteration();
		
		d_America = new Continent(1,"America");
		
		d_Canada = new Country("Canada", d_America);
		d_USA = new Country("USA", d_America);
		d_Mexico = new Country("Mexico", d_America);
		d_Guatemala = new Country("Guatemala", d_America);
		d_Nicaragua = new Country("Nicaragua", d_America);
		d_Colombia = new Country("Colombia", d_America);
		d_Venezuela = new Country("Venezuela", d_America);
		d_Ecuador = new Country("Ecuador", d_America);
		d_Peru = new Country("Peru", d_America);
		d_Brazil = new Country("Brazil", d_America);
		
		d_Asia = new Continent(2,"Asia");
		
		d_China = new Country("China", d_Asia);
		d_India = new Country("India", d_Asia);
		d_Japan = new Country("Japan", d_Asia);
		d_Korea = new Country("Korea", d_Asia);
		
		
		//America
		
		d_MFA.addContinent(d_America);
		
		d_MFA.addCountry(d_Canada);
		d_MFA.addCountry(d_USA);
		d_MFA.addCountry(d_Mexico);
		d_MFA.addCountry(d_Guatemala);
		d_MFA.addCountry(d_Nicaragua);
		d_MFA.addCountry(d_Colombia);
		d_MFA.addCountry(d_Venezuela);
		d_MFA.addCountry(d_Ecuador);
		d_MFA.addCountry(d_Peru);
		d_MFA.addCountry(d_Brazil);
		
		d_MFA.addNeighbour(d_Canada, d_USA);
		
		d_MFA.addNeighbour(d_USA, d_Canada);
		d_MFA.addNeighbour(d_USA, d_Mexico);
		
		d_MFA.addNeighbour(d_Mexico, d_USA);
		d_MFA.addNeighbour(d_Mexico, d_Guatemala);
		
		d_MFA.addNeighbour(d_Guatemala, d_Mexico);
		d_MFA.addNeighbour(d_Guatemala, d_Nicaragua);
		
		d_MFA.addNeighbour(d_Nicaragua, d_Guatemala);
		d_MFA.addNeighbour(d_Nicaragua, d_Colombia);
		
		d_MFA.addNeighbour(d_Colombia, d_Nicaragua);
		d_MFA.addNeighbour(d_Colombia, d_Venezuela);
		d_MFA.addNeighbour(d_Colombia, d_Ecuador);
		d_MFA.addNeighbour(d_Colombia, d_Brazil);
		d_MFA.addNeighbour(d_Colombia, d_Peru);
		
		d_MFA.addNeighbour(d_Venezuela, d_Colombia);
		d_MFA.addNeighbour(d_Venezuela, d_Brazil);
		
		d_MFA.addNeighbour(d_Ecuador, d_Colombia);
		d_MFA.addNeighbour(d_Ecuador, d_Peru);
		
		d_MFA.addNeighbour(d_Peru, d_Ecuador);
		d_MFA.addNeighbour(d_Peru, d_Colombia);
		d_MFA.addNeighbour(d_Peru, d_Brazil);
		
		d_MFA.addNeighbour(d_Brazil, d_Peru);
		d_MFA.addNeighbour(d_Brazil, d_Colombia);
		d_MFA.addNeighbour(d_Brazil, d_Venezuela);
		
		//Asia
		
		d_MFA.addContinent(d_Asia);
		
		d_MFA.addCountry(d_China);
		d_MFA.addCountry(d_India);
		d_MFA.addCountry(d_Japan);
		d_MFA.addCountry(d_Korea);
		
		d_MFA.addNeighbour(d_India, d_China);
		
		d_MFA.addNeighbour(d_China, d_India);
		d_MFA.addNeighbour(d_China, d_Japan);
		d_MFA.addNeighbour(d_China, d_Korea);
		
		d_MFA.addNeighbour(d_Japan, d_China);
		
		d_MFA.addNeighbour(d_Korea, d_China);
		
		d_Europe = new Continent(2, "Europe");
		
		d_France = new Country("France", d_Europe);
		
	}

	@AfterEach
	void tearDown() throws Exception {
		++d_test_number;
		this.d_MFA.getMapModel().clearMap();
	}
	
	
	/**
	 * When removing a country from the map, we need to check the following to ensure the map is valid.
	 * 1. Country is no longer located in the continent
	 * 2. Country no longer located in the mapping continent and country
	 * 3. Neighboring countries in which the deleted country used to be is updated to reflect that change
	 */
	@Test
	void removeCountry() {
		
		
		d_MFA.removeCountry(d_Brazil);
		
		boolean inCountriesList = false;
		boolean inContinentCountriesList = false;
		boolean inBordersList = false;
		
		//check in countries list
		
		System.out.println("INSIDE CHECK IN COUNTRIES LIST");
		for(Country country : d_MFA.getMapModel().getCountries()) {
			System.out.println(country.getCountryId());
			if(country == d_Brazil) {
				inCountriesList = true;
			}
		}
		
		assertFalse(inCountriesList);
		
		//check in continent countries list
		
		List<Country> CountriesList = d_MFA.getMapModel().getContinentCountries().get(d_America);
		
		for(Country country : CountriesList) {
			if(country == d_Brazil) {
				inContinentCountriesList = true;
			}
		}
		
		assertFalse(inContinentCountriesList);
		
		//check in borders list
		
		for(List<Country> neighboringCountries : d_MFA.getMapModel().getBorders().values()) {
			if(neighboringCountries.contains(d_Brazil)) {
				inBordersList = true;
			}
		}
		
		assertFalse(inBordersList);
		
	}
	
	/**
	 * When removing a continent from the map, we need to check the following to ensure the map is valid
	 * 1. The continent is no longer located in the continents list
	 * 2. The continent is no longer located in the mapping of continents and countries
	 * 3. The countries borders are updated to ensure none of the existing countries share a border with a country of the deleted continent
	 */
	@Test
	void removeContinent() {
		
		//Remove Asia continent from the map
		d_MFA.removeContinent(d_Asia);
		
		boolean inContinentList = false;
		boolean inContinentCountriesList = false;
		boolean inBordersList = false;
		
		//check in continent list
		
		for(Continent continent : d_MFA.getMapModel().getContinents()) {
			if(continent == d_Asia) {
				inContinentList = true;
			}
		}
		
		assertFalse(inContinentList);
		
		//check in continent countries list
		
		if(d_MFA.getMapModel().getContinentCountries().containsKey(d_Asia)) {
			inContinentCountriesList = true;
		}
		
		assertFalse(inContinentCountriesList);
		
		//check in borders list
		
		//iterate through all keys(Country) and check if the continent attribute is Asia
		for(Country country : d_MFA.getMapModel().getBorders().keySet()) {
			if(country.getContinent() == d_Asia) {
				inBordersList = true;
			}
		}
		
		//iterate through all countries and ensure that non of those countries have borders with any country of the deleted continent
		for(List<Country> country : d_MFA.getMapModel().getBorders().values()) {
			for(Country neighboringCountry : country) {
				if(neighboringCountry.getContinent() == d_Asia) {
					inBordersList = true;
				}
			}
		}
		
		assertFalse(inBordersList);
		
		
	}
	
	
	/**
	 * Test to verify that the map cannot only contain a single country.
	 * The test is performed with the following steps described below
	 * 1. Create a single continent and a single country
	 * 2. Add those elements to the map
	 * 3. Call the validateMap() method
	 * 4. Compare the log message with the expected message: " Countries Should be Atleast 2 in map "
	 */
	@Test
	void onlyOneCountry() {
		
		MapFileAlteration l_MFA = new MapFileAlteration();
		l_MFA.getMapModel().clearMap();
		l_MFA.getMapModel().setMapName("Imaginary Map");
		
		Continent l_ImaginaryContinent = new Continent(55,"Imaginary Continent");
		l_ImaginaryContinent.setContientValue("100");
		
		Country l_ImaginaryCountry = new Country("Imaginary Country", l_ImaginaryContinent);
		
		l_MFA.addContinent(l_ImaginaryContinent);
		l_MFA.addCountry(l_ImaginaryCountry);
		
		System.out.println(l_MFA.validateMap().getDescription());
		
		String l_map_status = l_MFA.validateMap().getDescription();
		String l_error = " Countries Should be Atleast 2 in map ";
		
		assertEquals(l_error, l_map_status);
		
	}
	
	/**
	 * When removing a neighbor from the map, we need to check the following to ensure the map is valid.
	 * 1. Neighboring countries is removed from the main country 
	 */
	@Test
	void removeNeighbour() {
		Boolean l_countryexist=false;
		d_MFA.removeNeighbour(d_China, d_India);
		for (Map.Entry<Country, List<Country>> mapEntry : d_MFA.getMapModel().getBorders().entrySet()) {

			if(mapEntry.getKey().equals(d_China) && mapEntry.getValue().contains(d_India))
			{
					l_countryexist=true;
					break;
				
			}
		}
		
		assertFalse(l_countryexist);
	}
	
	
	/**
	 * Test that ensures that addContinent method properly adds the continent's data in the map
	 * and also ensures that the Continent object added is indeed the proper one.
	 * It will perform two tests:
	 * 1. Loop through the continent list and check if object: d_Europe is contained
	 * 2. Check that l_Europe unique ID is correct.  Since ID is auto increment starting from 0,
	 * l_Europe should have an ID of 2 since it is the third continent added to the map.
	 */
	@Test
	void addContinent() {
		
		
		d_MFA.addContinent(d_Europe);
		
		boolean inContinentList = false;
		
		for(Continent continent : d_MFA.getMapModel().getContinents()) {
			if(continent == d_Europe) {
				inContinentList = true;
			}
		}
		
		assertTrue(inContinentList);
		System.out.println("WATAME!!!!");
		System.out.println(d_MFA.getMapModel().getContinents().get(2).getContinentId());
		
		assertEquals(2, d_MFA.getMapModel().getContinents().get(2).getUniqueContinetId());
		
	}
	
	/**
	 * Test that ensures that addCountry method properly adds the country's data in the map
	 * and also ensures that the Country object added is indeed the proper one.
	 * It will perform two tests:
	 * 1. Loop through the countries list and check if object: d_France is contained
	 * 2. Check if d_France is contained inside the continent countries map by first
	 * searching for the key Europe and check if France is contained in the list.
	 */
	@Test
	void addCountry() {
		d_MFA.addContinent(d_Europe);
		d_MFA.addCountry(d_France);
		
		boolean inCountryList = false;
		boolean inContinentCountriesList = false;
		
		
		//check in countries list
		for(Country country : d_MFA.getMapModel().getCountries()) {
			if(country == d_France) {
				inCountryList = true;
			}
		}
		
		assertTrue(inCountryList);
		
		//check in continent countries list
		
		System.out.println(d_France.getContinent().getContinentId());
		if(d_MFA.getMapModel().getContinentCountries().get(d_Europe).contains(d_France)) {
			inContinentCountriesList = true;
		}
		
		assertTrue(inContinentCountriesList);
	}
	
	
	// 
	@Test
	void readInvalidMap() {
		
		MapPhaseState.D_CURRENT_MAP = "continentMissing.map";
		d_MFA.readMapFile();
		assertEquals("Country's Continent Data is missing",d_MFA.validateMap().getDescription());
		
		MapPhaseState.D_CURRENT_MAP = "borderMissing.map";
		d_MFA.readMapFile();
		assertEquals(" Countries Border Missing ",d_MFA.validateMap().getDescription());
		
		MapPhaseState.D_CURRENT_MAP = "countryMissing.map";
		d_MFA.readMapFile();
		assertEquals(" Border Data for Countries is not consistent with Countries that are added ",d_MFA.validateMap().getDescription());
	}

}
























































