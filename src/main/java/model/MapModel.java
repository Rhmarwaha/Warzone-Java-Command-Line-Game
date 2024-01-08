package model;

import Constants.ProjectConfig;
import GamePhase.MapPhaseState;
import logger.LogEntryBuffer;

import java.io.*;
import java.util.*;


/**
 * class that defines all entities in the map (continents, countries, borders) and their operations
 * @author Rohit
 * @version build 2
 *
 */
public class MapModel implements Serializable {
	/**
	 * string map name
	 */
	private String mapName;
	/**
	 * list of continents in map
	 */
	private List<Continent> continents;
	/**
	 * list of countries in map
	 */
	private List<Country> countries;

	/**
	 * map of continent and countries list
	 */

	private Map<Continent,List<Country>> continentCountries;
	/**
	 * map of country and borders list
	 */
	private Map<Country,List<Country>> borders;
	/**
	 * Object of MapModel class
	 */
	private static MapModel d_MapModel;
	/**
	 * LogEntryBuffer
	 */
	private LogEntryBuffer d_logger;

	/**
	 * Hashmap for countries
	 */
	HashMap<String, Country> countryHashMap = new HashMap<>();

	private FileReader d_mapFileReader;
	private BufferedReader d_bufferReader;

	
	
	/**
	 * default constructor
	 */
	public MapModel() {
	}

	/**
	 * If an instance for MapModel has not been created, create it and return it
	 * @return d_MapModel
	 */
	public static MapModel getInstance() {
		if (Objects.isNull(d_MapModel)) {
			d_MapModel = new MapModel();
		}
		return d_MapModel;
	}
	
	/**
	 * getter method that returns the list of all countries on the map
	 * @return countries
	 */
	public List<Country> getCountries() {
		return countries;
	}
	
	/**
	 * setter method to modify the countries on the map
	 * @param countries - list of countries
	 */
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	
	/**
	 * getter method for the name of the map
	 * @return mapName
	 */
	public String getMapName() {
		return mapName;
	}
	
	/**
	 * setter method to modify the map's name
	 * @param mapName - updated map name
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	/**
	 * getter method that returns the list of all continents
	 * @return continents
	 */
	public List<Continent> getContinents() {
		return continents;
	}
	
	/**
	 * setter method that modifies the list of continents on the map
	 * @param continents - updated list of continents
	 */
	public void setContinents(List<Continent> continents) {
		this.continents = continents;
	}
	
	/**
	 * getter method that returns a map of all continents and their its associated countries
	 * @return continentCountries
	 */
	public Map<Continent, List<Country>> getContinentCountries() {
		return continentCountries;
	}
	
	/**
	 * setter method to modify the map's continents and their associated countries
	 * @param continentCountries - map of continents and their list of associated countries
	 */
	public void setContinentCountries(Map<Continent, List<Country>> continentCountries) {
		this.continentCountries = continentCountries;
	}
	
	/**
	 * getter method that returns the map of each country  a list of countries that borders them
	 * @return borders
	 */
	public Map<Country, List<Country>> getBorders() {
		return borders;
	}
	
	/**
	 * setter method to modify the countries' borders
	 * @param borders - map of countries and their list of associated countries they share a border
	 */
	public void setBorders(Map<Country, List<Country>> borders) {
		this.borders = borders;
	}
	
	/**
	 * method for adding a new continent on the map.
	 * If the map does not contain any continents, initialize map continents with an array list
	 * If the map does not contain a mapping of continents with associated countries, initialize the continent countries with a HashMap
	 * @param continent - continent to be added to the map
	 */
	public void addContinent(Continent continent) {
		
		if(this.continents == null) {
			this.continents = new ArrayList<Continent>();
		}
		if(this.continentCountries == null) {
			this.continentCountries = new HashMap<Continent, List<Country>>();
		}
		this.continents.add(continent);
		this.continentCountries.put(continent, new ArrayList<Country>());
	}
	
	/**
	 * method to input a new country into its associated continent
	 * If that particular continent does not exist in the mapping of continentCountries, nothing happens.
	 * If it does, add that country into the map (continentCountries)
	 * If the map does not have any borders set, initialize the border with a linked hash-map and add that country
	 * 
	 * @param continent - continent
	 * @param country - country
	 */
	public void addContinentCountries(Continent continent, Country country) {
		if(continent != null) {
			country.setContinent(continent);
			if (this.continentCountries.containsKey(continent)) {
//				System.out.println(country.getCountryId() + "inside...............");
				this.continentCountries.get(continent).add(country);	
			}
			continent.addToCountryList(country);
		}
				
		if(this.borders == null) {
			this.borders = new LinkedHashMap<Country, List<Country>>();
		}
		this.borders.put(country, new ArrayList<Country>());
		if(this.countries == null) {
			this.countries = new ArrayList<Country>();
		}
		this.countries.add(country);
		this.borders.put(country, new ArrayList<Country>());
		
		
		
//		System.out.println(country.getCountryId() + "outside...............");
		
	}
	
	/**
	 * method to add a neighboring country to the map of borders using mainCountry as search key 
	 * @param mainCountry - country we want to add a border with
	 * @param neighbourCountry - country that will share border and be inserted in the list
	 */
	public void addBorders(Country mainCountry,Country neighbourCountry) {
		
		
		if(this.borders.containsKey(mainCountry)) {
			this.borders.get(mainCountry).add(neighbourCountry);
			mainCountry.getNeighbors().add(neighbourCountry);
		}else {
			this.borders.put(mainCountry,  new ArrayList<Country>());
//			System.out.println("ROHIT");
		}
	}

	
	/**
	 * Method for validating the map once created. It can produce the following message:
	 * 1. In map there are no continents
	 * 2. Continent Value is not good
	 * 3. Map is not created Properly
	 * 4. Duplicate Continent or Country Found in map
	 * 5. Countries Should be Atleast 2 in map
	 * 6. Country's Continent Data is missing
	 * 7. Country's Continent not available in the list
	 * 8. Border Data for Countries is not consistent with Countries that are added
	 * 9. Countries Border Missing
	 * 10. VALIDATION SUCCESSFUL
	 * @return alert for the specific message, either successful if not, what is the error message
	 */
	public ResponseWrapper validateMap() {

		if(getContinents() == null  ) {
			return new ResponseWrapper(404, "In map there are no continents");
		}
		if(getCountries() == null) {
			return new ResponseWrapper(404, "In map there are no countries");
		}
		Set<Continent> l_continents = new HashSet<Continent>(getContinents());
		Set<Country> l_countries = new HashSet<Country>(getCountries());

		for(Continent cont : l_continents)
		{
			try {
				Integer.parseInt(cont.getContientValue());

			} catch (Exception exc) {
				return new ResponseWrapper(404, " COntinent Value is not good");
			}

		}

		if ("".equals(getMapName()) || getMapName() == null || getContinents().isEmpty()
				|| getCountries().isEmpty() || getContinentCountries().isEmpty()
				|| getBorders().isEmpty()) {

			return new ResponseWrapper(404, "Map is not created Properly");

		} else if (l_continents.size() != getContinents().size()
				|| l_countries.size() != getCountries().size()) {
			return new ResponseWrapper(404, "Duplicate Continent or Country Found in map");

		} else if (l_countries.size() < 2) {
			return new ResponseWrapper(404, " Countries Should be Atleast 2 in map ");

		}



		Boolean l_countryContinentNotExists = getCountries().stream()
				.anyMatch((country) -> country.getContinent() == null
						|| country.getContinent().getContientValue() == null
						|| "".equals(country.getContinent().getContinentId())
						|| "".equals(country.getContinent().getContientValue()));

		if (Boolean.TRUE.equals(l_countryContinentNotExists)) {
			return new ResponseWrapper(404, "Country's Continent Data is missing");
		}

		Boolean l_countryContinentExistsInContinentsList=false;
		for(Country count : getCountries())
		{
			for(Continent conti : l_continents)
			{
				if(count.getContinent().getContinentId().contains(conti.getContinentId()))
				{
					l_countryContinentExistsInContinentsList=true;
					break;
				}
			}
			if(Boolean.FALSE.equals(l_countryContinentExistsInContinentsList))
			{
				break;
			}

		}

		if (Boolean.FALSE.equals(l_countryContinentExistsInContinentsList)) {
			return new ResponseWrapper(404, " Country's Continent not available in the list ");

		}



		Boolean l_countryBorderRelevantData=false;
		for(Map.Entry<Country, List<Country>> mapEntry : getBorders().entrySet()) {
			boolean isCountryExitInList = false;
			if(mapEntry.getKey() != null) {
				for(Country country : l_countries) {
					if(country.getCountryId().equals(mapEntry.getKey().getCountryId())) {
						isCountryExitInList = true;
					}
				}
			}

			if (! isCountryExitInList) {
				l_countryBorderRelevantData = true;
				break ;
			}


			for(Country neighbourCountries : mapEntry.getValue()) {
				if(neighbourCountries != null) {
					for(Country country : l_countries) {
						if(country.getCountryId().equals(neighbourCountries.getCountryId())) {
							isCountryExitInList = true;
						}
					}

				}else {
					isCountryExitInList = true;

				}

				if (! isCountryExitInList) {
					l_countryBorderRelevantData = true;
					break ;
				}


			}
		}


		if (Boolean.TRUE.equals(l_countryBorderRelevantData)) {
			return new ResponseWrapper(404,
					" Border Data for Countries is not consistent with Countries that are added ");

		}




		Boolean l_countryBorderNotExists = getBorders().entrySet().stream()
				.anyMatch(borderMap -> borderMap.getValue().isEmpty()) ? true : false;

		if (Boolean.TRUE.equals(l_countryBorderNotExists)) {
			return new ResponseWrapper(404, " Countries Border Missing ");
		}

		return new ResponseWrapper(200, " VALIDATION SUCCESSFUL ");
	}


	

	
	/**
	 * Method to return the specific country
	 * @param p_Id - country's ID
	 * @return country that was searched by ID
	 */
	public Country getCountry(String p_Id) {

		for (Country country : countries) {
			String uniqueId = country.getCountryId();
			countryHashMap.put(uniqueId, country);
		}
		return countryHashMap.get(p_Id);
	}
	
	/**
	 * method to return list of countries
	 * @param countriesList - list of countries in the map
	 * @return a string of the list of all countries
	 */
	public String getCountriesList(List<Country> countriesList) {
		String l_countList = "";
		for (Country country : countriesList) {
			l_countList += country.getCountryId() + "-";
		}
		return l_countList.length() > 0 ? l_countList.substring(0, l_countList.length() - 1) : "";
	}
	

	public ResponseWrapper MapModelBuilder(MapModel p_MapModel) {
		this.clearMap();
		try {

			d_mapFileReader = new FileReader(ProjectConfig.D_MAP_FILES_PATH+ MapPhaseState.D_CURRENT_MAP);
			d_bufferReader = new BufferedReader(d_mapFileReader);

		} catch (FileNotFoundException p_e) {
			// Log
			p_e.printStackTrace();
		}

		String l_mapFileLine ;
		boolean l_isMapContent , l_isCountriesTableContent,l_isContinentTableContent,l_isBorderTableContent;
		l_isMapContent = l_isCountriesTableContent = l_isContinentTableContent = l_isBorderTableContent = false;

		try {
			while ((l_mapFileLine = d_bufferReader.readLine()) != null) {
				if(l_mapFileLine.equals("MAP")) {
					l_isMapContent = true;
					l_isCountriesTableContent = l_isContinentTableContent = l_isBorderTableContent = false;
					continue;
				}else if(l_mapFileLine.equals("CONTINENTS_TABLE")) {
					l_isContinentTableContent = true;
					l_isMapContent = l_isCountriesTableContent = l_isBorderTableContent = false;
					continue;
				}else if(l_mapFileLine.equals("COUNTRIES_TABLE")) {
					l_isCountriesTableContent = true;
					l_isMapContent = l_isContinentTableContent = l_isBorderTableContent = false;
					continue;
				}else if(l_mapFileLine.equals("BORDERS_TABLE")) {
					l_isBorderTableContent = true;
					l_isMapContent = l_isCountriesTableContent = l_isContinentTableContent = false;
					continue;
				}

				if(l_isMapContent) {
					d_MapModel.setMapName(l_mapFileLine);
				}else if(l_isContinentTableContent) {
					String[] l_continentRow = l_mapFileLine.trim().split("\\s+");
					try {
						d_MapModel.addContinent(new Continent(Integer.parseInt(l_continentRow[0]),l_continentRow[1],l_continentRow[2]));
					}catch(IndexOutOfBoundsException ex) {

					}

				}else if(l_isCountriesTableContent) {
					String[] l_countryRow = l_mapFileLine.trim().split("\\s+");
					try {
						Country l_country = new Country(Integer.parseInt(l_countryRow[0]), l_countryRow[1]);

						if(d_MapModel.getContinents().size() <= Integer.parseInt(l_countryRow[2])) {
							d_MapModel.addContinentCountries(null, l_country);
						}else {
							d_MapModel.addContinentCountries(d_MapModel.getContinents().get(Integer.parseInt(l_countryRow[2])), l_country);
						}

					}catch(IndexOutOfBoundsException ex) {
						System.out.println(ex);
					}


				}else if(l_isBorderTableContent) {
					String[] l_borderRow = l_mapFileLine.trim().split("\\s+");
					Country l_mainCountry = null ;
					try {
						if(d_MapModel.getCountries().size() > Integer.parseInt(l_borderRow[0])){
							l_mainCountry = d_MapModel.getCountries().get(Integer.parseInt(l_borderRow[0]));
						}else {

						}

					}catch(IndexOutOfBoundsException ex) {
						System.out.println(ex+"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
					}

					for(int counter = 1; counter<l_borderRow.length; counter++) {
						try {
							if(d_MapModel.getCountries().size() <= Integer.parseInt(l_borderRow[counter])) {
								d_MapModel.addBorders(l_mainCountry,null);
							}else {
								d_MapModel.addBorders(l_mainCountry, d_MapModel.getCountries().get(Integer.parseInt(l_borderRow[counter])));
							}

						}catch(IndexOutOfBoundsException | NullPointerException ex) {

						}

					}
				}

			}
		} catch (IOException p_e) {

			p_e.printStackTrace();
		}
		return new ResponseWrapper(200, "Successfully loaded Map");
	}
	
	
	
	

	/**
	 * method for clearing the map and all its parameters
	 */
	public void clearMap() {

		
		if( MapModel.getInstance().getContinents() != null) {
			MapModel.getInstance().getContinents().clear();
		}
		if( MapModel.getInstance().getCountries() != null) {
			MapModel.getInstance().getCountries().clear();
		}
		
		if( MapModel.getInstance().getContinentCountries() != null) {
			MapModel.getInstance().getContinentCountries().clear();
		}
		
		if( MapModel.getInstance().getBorders() != null) {
			MapModel.getInstance().getBorders().clear();
		}
		
		
		
		
	}
}
