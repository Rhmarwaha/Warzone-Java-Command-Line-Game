package persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import Constants.ProjectConfig;
import GamePhase.MapPhaseState;
import logger.ConsoleWriter;
import logger.LogEntryBuffer;
import logger.LogGenerator;
import model.Continent;
import model.Country;
import model.GameModel;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;

/**
 * class that defines creating, saving, reading and loading map files into the game
 * @author Rohit, Raghav
 * @version build 2
 */
public class MapFileAlteration {
	/**
	 * filereader
	 */
	private FileReader d_mapFileReader;
	/**
	 * buffered reader
	 */
	private BufferedReader d_bufferReader;
	/**
	 * file writer
	 */
	private FileWriter d_mapFileWriter;
	/**
	 * buffered writer
	 */
	private BufferedWriter d_bufferWriter;
	/**
	 * object of map model
	 */
	private MapModel d_mapModel;
	/**
	 * object of game model
	 */
	private GameModel gameModel;

	/**
	 * table pattern string
	 */
	String l_tablePattern = "- %-25s- %-20s- %-70s%n";
	/**
	 * LogEntryBuffer
	 */
	private LogEntryBuffer d_logger;
	/**
	 * Console Writer
	 */
	private ConsoleWriter d_consoleWriter;
	/**
	 * Log Generator
	 */
	private LogGenerator d_logGenrator;
	/**
	 * String Builder object
	 */

	StringBuilder stringBuilder = new StringBuilder();
	
	/**
	 * method for creating a new MapModel object
	 */
	public MapFileAlteration() {
		d_mapModel = MapModel.getInstance();
		gameModel  = GameModel.getInstance();
		d_logger = new LogEntryBuffer();
		d_consoleWriter = new ConsoleWriter();
		d_logGenrator = LogGenerator.getInstance();
		d_logger.addObserver(d_consoleWriter);
		d_logger.addObserver(d_logGenrator);
	}
	
	/**
	 * Reads a map file from the map package.
	 * Data is extracted in the following manner:
	 * 1. When it reads the line "MAP": extracts name of the map
	 * 2. When it reads the line "CONTINENTS_TABLE": data below are the continents and their associated id
	 * 3. When it reads the line "COUNTRIES_TABLE": data below are the countries, their associated id and which continent
	 * they belong to using continent id.
	 * 4. When it reads the line "BORDERS_TABLE" data below are: first number is the country's id and every number after it
	 * are the countries that border it using their id.
	 *
	 */
	public void readMapFile() {
		d_logger.setLogMessage("Domination Map is loaded");
		this.d_mapModel.clearMap();

		try {

			d_mapFileReader = new FileReader(ProjectConfig.D_MAP_FILES_PATH+MapPhaseState.D_CURRENT_MAP);
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
					this.d_mapModel.setMapName(l_mapFileLine);
				}else if(l_isContinentTableContent) {
					String[] l_continentRow = l_mapFileLine.trim().split("\\s+");
					try {
						this.d_mapModel.addContinent(new Continent(Integer.parseInt(l_continentRow[0]),l_continentRow[1],l_continentRow[2]));
					}catch(IndexOutOfBoundsException ex) {

					}

				}else if(l_isCountriesTableContent) {
					String[] l_countryRow = l_mapFileLine.trim().split("\\s+");
					try {
						Country l_country = new Country(Integer.parseInt(l_countryRow[0]), l_countryRow[1]);

						if(this.d_mapModel.getContinents().size() <= Integer.parseInt(l_countryRow[2])) {
							this.d_mapModel.addContinentCountries(null, l_country);
						}else {
							this.d_mapModel.addContinentCountries(this.d_mapModel.getContinents().get(Integer.parseInt(l_countryRow[2])), l_country);
						}

					}catch(IndexOutOfBoundsException ex) {
						System.out.println(ex);
					}


				}else if(l_isBorderTableContent) {
					String[] l_borderRow = l_mapFileLine.trim().split("\\s+");
					Country l_mainCountry = null ;
					try {
						if(this.d_mapModel.getCountries().size() > Integer.parseInt(l_borderRow[0])){
							l_mainCountry = this.d_mapModel.getCountries().get(Integer.parseInt(l_borderRow[0]));
						}else {

						}

					}catch(IndexOutOfBoundsException ex) {
						System.out.println(ex+"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
					}

					for(int counter = 1; counter<l_borderRow.length; counter++) {
						try {
							if(this.d_mapModel.getCountries().size() <= Integer.parseInt(l_borderRow[counter])) {
								this.d_mapModel.addBorders(l_mainCountry,null);
							}else {
								this.d_mapModel.addBorders(l_mainCountry, this.d_mapModel.getCountries().get(Integer.parseInt(l_borderRow[counter])));
							}

						}catch(IndexOutOfBoundsException | NullPointerException ex) {

						}

					}
				}

			}
		} catch (IOException p_e) {

			p_e.printStackTrace();
		}
	}

	/**
	 * With an already create map by the player.  The map can be saved in the following format.
	 * Data is extracted from the list of continents, countries, map of continent countries and map of borders
	 * with the following tags: MAP, CONTINENTS_TABLE, COUNTRIES_TABLE, BORDERS_TABLE respectively.
	 * Use buffer variable to efficiently transfer data
	 * @param p_mapFileName - name of map file
	 */
	private void writeMapFile(String p_mapFileName, Boolean saveAsConquest) {
		try {
			if(! MapPhaseState.D_CURRENT_MAP.equals(p_mapFileName)) {
				try{
					if(new File(ProjectConfig.D_MAP_FILES_PATH + p_mapFileName).createNewFile()) {
						MapPhaseState.D_CURRENT_MAP = p_mapFileName;
					}
				}catch(IOException p_exception) {

				}

			}

			d_mapFileWriter = new FileWriter(ProjectConfig.D_MAP_FILES_PATH+MapPhaseState.D_CURRENT_MAP);
			d_bufferWriter = new BufferedWriter(d_mapFileWriter);

			stringBuilder.append("MAP\n" + MapPhaseState.D_CURRENT_MAP+"\nCONTINENTS_TABLE\n");

			for(Continent continent : this.d_mapModel.getContinents()) {
				stringBuilder.append(continent.getUniqueContinetId())
						.append(" ")
						.append(continent.getContinentId())
						.append(" ")
						.append(continent.getContientValue())
						.append("\n");
			}

			if(this.d_mapModel.getCountries() != null){
				stringBuilder.append("COUNTRIES_TABLE\n");
				for(Country country : this.d_mapModel.getCountries()) {
					stringBuilder.append(country.getUniqueCountryId())
							.append(" ")
							.append(country.getCountryId())
							.append(" ")
							.append(country.getContinent().getUniqueContinetId())
							.append("\n");
				}
			}
			if(this.d_mapModel.getBorders() != null) {
				stringBuilder.append("BORDERS_TABLE");
				for(Map.Entry<Country, List<Country>> border: this.d_mapModel.getBorders().entrySet()) {
					stringBuilder.append("\n")
							.append(border.getKey().getUniqueCountryId());
					for(Country country : border.getValue()) {
						stringBuilder.append(" ")
								.append(country.getUniqueCountryId());

					}
				}
			}
			d_bufferWriter.write(stringBuilder.toString());
			d_bufferWriter.flush();
		} catch (  IOException p_e) {
			// Log
			p_e.printStackTrace();
		}


	}

	/**
	 * Method to add a continent in the map and returns alert of successful operation
	 * @param p_continent - a continent object to be added to the map
	 * @return ReponseWrapper temp object to alert player of the status after adding the continent
	 */
	public ResponseWrapper addContinent(Continent p_continent) {

		if(this.d_mapModel.getContinents() == null) {
			p_continent.setUniqueContinetId(0);
		}else {
			p_continent.setUniqueContinetId(this.d_mapModel.getContinents().size());
		}

		this.d_mapModel.addContinent(p_continent);

		return new ResponseWrapper(200, "Add continent successfully ");
	}

	/**
	 * Method to add a country in the map and returns alert of successful operation
	 * @param p_country - a country object to be added to the map
	 * @return ReponseWrapper temp object to alert player of the status after adding the country
	 */
	public ResponseWrapper addCountry(Country p_country) {

		for(Continent continent : this.d_mapModel.getContinents()) {
			if(continent.getContinentId().equals(p_country.getContinent().getContinentId())) {
				p_country.setContinent(continent);
				break;
			}
		}
		if(this.d_mapModel.getCountries() == null) {
			p_country.setUniqueCountryId(0);
		}else {
			p_country.setUniqueCountryId(this.d_mapModel.getCountries().size());
		}

		this.d_mapModel.addContinentCountries(p_country.getContinent(), p_country);

		return new ResponseWrapper(200, "add country successfully ");
	}

	/**
	 * Method for adding a specific country as a neighbor of another country
	 * @param p_mainCountry - the key country
	 * @param p_neighbourCountry - the neighboring country to the key country
	 * @return ReponseWrapper temp object to alert player of the status after adding a neighboring country
	 */
	public ResponseWrapper addNeighbour(Country p_mainCountry, Country p_neighbourCountry) {

		for(Country country : this.d_mapModel.getCountries()) {
			if(country.getCountryId().equals(p_mainCountry.getCountryId())) {
				p_mainCountry = country;
			}
			if(country.getCountryId().equals(p_neighbourCountry.getCountryId())) {
				p_neighbourCountry = country;
			}
		}
		this.d_mapModel.addBorders(p_mainCountry, p_neighbourCountry);
		this.d_mapModel.addBorders(p_neighbourCountry, p_mainCountry);
		return new ResponseWrapper(200, "add neighbour successfully ");
	}

	/**
	 * Method for saving a map in the event of creation or editing
	 * @param p_mapFileName - name of map file
	 * @return alert to show map has been successfully saved
	 */
	public ResponseWrapper saveMap(String p_mapFileName, Boolean saveAsConquest) {
		this.writeMapFile(p_mapFileName,saveAsConquest);
		return new ResponseWrapper(200, "Save Map successfully ");
	}

	/**
	 * Method for outputting in table format, the complete details of the current map in play
	 * Information about the map include:
	 * 1. Country Name
	 * 2. Continent Name
	 * 3. Bordering Countries
	 * 4. Player information
	 * 5. Continents controlled by player
	 * @return alert for successful showing of map
	 */
	public ResponseWrapper showmap() {

		/*ResponseWrapper l_resp=this.validateMap();
		if(l_resp.getStatusValue()==404)
		{
			System.out.format("\nMap cannot be displayed as Validation Failed \n");
			return l_resp;
		}*/

		d_logger.setLogMessage(" Continents: ");
		d_logger.setLogMessage("********************");
		d_logger.setLogMessage("  Continent's Name  ");
		d_logger.setLogMessage("********************");

		this.d_mapModel.getContinents().stream().forEach((continent) -> {
			String l_table = "- %-20s %n";
			System.out.format(l_table, continent.getContinentId());
		});

		d_logger.setLogMessage("********************");


		// Showing Countries in the Continent and their details
		System.out.format("\n Countries in this Map and their details are : \n");

		String country_title = "Country";
		String continent_title = "Continent";
		String neighbors_title = "Country";
		System.out.format("****************************************************************************************************%n");
		System.out.format(l_tablePattern, country_title, continent_title, neighbors_title);
		System.out.format("****************************************************************************************************%n");
		for (Map.Entry<Country, List<Country>> entry : this.d_mapModel.getBorders().entrySet()) {

			System.out.format(l_tablePattern, entry.getKey().getCountryId(), entry.getKey().getContinent().getContinentId(),
					this.getCountriesList(entry.getValue()));
		}

		System.out.format("****************************************************************************************************%n");

		System.out.format("\nPlayers in this game are : ");

		System.out.format("\nCountries allotted to players: ");

		System.out.format("\n*****************************************************************%n");
		System.out.format("     Players     !     Strategy     !     Allotted Countries       %n");
		System.out.format("*******************************************************************%n");

		if(this.gameModel.getPlayers() != null ) {
			for(Player player : gameModel.getPlayers()) {
				System.out.format("     " + player.getPlayerName() + "     !     " +player.getStrategy().getStrategyName() + "     !     ");
				if(player.getCountriesHold() != null) {
					for(Country country : player.getCountriesHold())
						System.out.format(" -" + country.getCountryId());
				}
				System.out.println();
			}
		}

		System.out.format("*******************************************************************%n");

		return new ResponseWrapper(200," Show Map Done Successfully");

	}

	/**
	 * method to get countries list in the form of a string
	 * @param countriesList - list of countries
	 * @return list of countries as a string if the string length is greater than 0, else return empty
	 */
	public String getCountriesList(List<Country> countriesList) {
		String l_countList = "";
		for (Country country : countriesList) {
			l_countList += country.getCountryId() + "-";
		}
		return l_countList.length() > 0 ? l_countList.substring(0, l_countList.length() - 1) : "";
	}

	/**
	 * Method to remove continent from the map, it should be removed from the following:
	 * 1. continents list
	 * 2. countries list (all countries that belong to the deleted continent must also be deleted)
	 * 3. continentsCountries map: a mapping key/value pair of continent and list of countries that belong to that continent
	 * 4. borders map: a mapping key/value pair of country and list of countries that borders the key
	 *
	 * When deleting a continent we need to ensure that all countries in that continent is deleted
	 * and ensure that the remaining countries do not share a border with the deleted countries.
	 *
	 * @param p_continent - continent to be removed
	 * @return alert to player to show successful deletion of continent
	 */
	public ResponseWrapper removeContinent(Continent p_continent) {

		// Deleting Continents
		Boolean l_continentFound = false;

		for (int contIndex = 0; contIndex < d_mapModel.getContinents().size(); contIndex++) {
			if (d_mapModel.getContinents().get(contIndex).getContinentId().equals(p_continent.getContinentId())) {
				d_mapModel.getContinents().remove(contIndex);
				l_continentFound=true;
				break;
			}
		}

		if(Boolean.FALSE.equals(l_continentFound))
		{
			return new ResponseWrapper(404, "Continent Doesn't Exist in the Map");
		}

		// Deleting Countries
		List<Country> l_deletedCountriesList = d_mapModel.getCountries();
		d_mapModel.setCountries(d_mapModel.getCountries().stream().filter((conti)-> ! conti.getContinent().getContinentId().equals(p_continent.getContinentId())).collect(Collectors.toList()));
		l_deletedCountriesList.removeIf((cont)-> !cont.getContinent().getContinentId().equals(p_continent.getContinentId()));

		// Removing Continents from Map

		d_mapModel.getContinentCountries().entrySet().removeIf((map)-> map.getKey().getContinentId().equals(p_continent.getContinentId()));

		// Removing countries from Borders
		Map<Country, List<Country>> l_tempMap = new HashMap<Country, List<Country>>(d_mapModel.getBorders());

		for (Map.Entry<Country, List<Country>> mapEntry : l_tempMap.entrySet()) {
			Boolean l_countExists= l_deletedCountriesList.stream().anyMatch((cont)-> cont.getCountryId().equals(mapEntry.getKey().getCountryId()));
			if (Boolean.TRUE.equals(l_countExists)) {
				for (Country country : l_deletedCountriesList) {
					d_mapModel.getBorders().entrySet().removeIf((map)-> map.getKey().getCountryId().equals(country.getCountryId()));
				}
			}
		}

		// Removing neighboring countries from Borders
		List<Country> l_countriesUpdatedBorderList;
		for (Map.Entry<Country, List<Country>> mapEntry : d_mapModel.getBorders().entrySet()) {
			l_countriesUpdatedBorderList = mapEntry.getValue();
			for (Country country : l_deletedCountriesList) {
				l_countriesUpdatedBorderList = l_countriesUpdatedBorderList.stream()
						.filter((cont) -> !cont.getCountryId().equals(country.getCountryId()))
						.collect(Collectors.toList());
			}
			d_mapModel.getBorders().put(mapEntry.getKey(), l_countriesUpdatedBorderList);
		}

		return new ResponseWrapper(200,"Removed Continent Successfully");

	}

	/**
	 * Method to remove country from the map, it should be removed from the following:
	 * 1. countries list
	 * 2. continentsCountries map: a mapping key/value pair of continent and list of countries that belong to that continent
	 * 3. borders map: a mapping key/value pair of country and list of countries that borders the key
	 *
	 * When deleting a country, we should ensure that other countries that used to border that country reflect
	 * that change and also delete that country from their borders list.
	 *
	 * @param country - country to be removed
	 * @return alert to player to show successful deletion of country
	 */
	public ResponseWrapper removeCountry(Country country) {
		// Deleting Countries
		List<Country> l_deletedCountriesList = d_mapModel.getCountries();

		d_mapModel.setCountries(d_mapModel.getCountries().stream().filter((conti)-> !conti.getCountryId().equals(country.getCountryId())).collect(Collectors.toList()));
		l_deletedCountriesList.removeIf((conti)-> ! conti.getCountryId().equals(country.getCountryId()));

		if(l_deletedCountriesList.isEmpty())
		{
			return new ResponseWrapper(404, "Country Doesn't Exist in the Map");
		}
		// Removing countries from ContinentCountries map

		List<Country> l_continentCountriesList;
		for (Map.Entry<Continent, List<Country>> mapEntry : d_mapModel.getContinentCountries().entrySet()) {
			l_continentCountriesList = mapEntry.getValue();
			for (Country conti : l_deletedCountriesList) {
				l_continentCountriesList = l_continentCountriesList.stream()
						.filter((cont) -> !cont.getCountryId().equals(conti.getCountryId()))
						.collect(Collectors.toList());
			}
			d_mapModel.getContinentCountries().put(mapEntry.getKey(), l_continentCountriesList);
		}

		// Removing countries from Borders
		Map<Country, List<Country>> l_tempMap = new HashMap<Country, List<Country>>(d_mapModel.getBorders());

		for (Map.Entry<Country, List<Country>> mapEntry : l_tempMap.entrySet()) {
			Boolean l_countExists= l_deletedCountriesList.stream().anyMatch((cont)-> cont.getCountryId().equals(mapEntry.getKey().getCountryId()));
			if (Boolean.TRUE.equals(l_countExists)) {
				for (Country cont : l_deletedCountriesList) {
					d_mapModel.getBorders().entrySet().removeIf((map)-> map.getKey().getCountryId().equals(cont.getCountryId()));
				}
			}
		}

		// Removing neighboring countries from borders

		List<Country> l_countriesUpdatedBorderList;
		for (Map.Entry<Country, List<Country>> mapEntry : d_mapModel.getBorders().entrySet()) {
			l_countriesUpdatedBorderList = mapEntry.getValue();
			for (Country conti : l_deletedCountriesList) {
				l_countriesUpdatedBorderList = l_countriesUpdatedBorderList.stream()
						.filter((cont) -> !cont.getCountryId().equals(conti.getCountryId()))
						.collect(Collectors.toList());
			}
			d_mapModel.getBorders().put(mapEntry.getKey(), l_countriesUpdatedBorderList);
		}

		return new ResponseWrapper(200, "Removed Country Successfully");
	}

	/**
	 * Method for removing a country from another country's list of neighbors.
	 * That change should be reflected in borders map.
	 *
	 * The borders map is searched by the country key, its neighbor list is access and search
	 * until param: neighbourCountry is found in the list and deleted
	 *
	 * @param mainCountry - key value in the borders map
	 * @param neighbourCountry - country to be removed in the list of countries in borders map
	 * @return alert that neighboring country has been successfully removed
	 */
	public ResponseWrapper removeNeighbour(Country mainCountry,Country neighbourCountry) {

		List<Country> l_neighbouringCountriesList;
		Boolean l_countryFound=false;
		Boolean l_neighbourFound=false;

		for (Map.Entry<Country, List<Country>> mapEntry : d_mapModel.getBorders().entrySet()) {

			if(!mapEntry.getKey().getCountryId().equals(mainCountry.getCountryId()))
			{
				continue;
			}
			l_neighbouringCountriesList = mapEntry.getValue();
			l_countryFound=true;
			l_neighbouringCountriesList = l_neighbouringCountriesList.stream()
					.filter((cont) -> !cont.getCountryId().equals(neighbourCountry.getCountryId()))
					.collect(Collectors.toList());
			if(mapEntry.getValue().size() != l_neighbouringCountriesList.size())
			{
				l_neighbourFound=true;
			}
			d_mapModel.getBorders().put(mapEntry.getKey(), l_neighbouringCountriesList);
		}

		if(Boolean.FALSE.equals(l_countryFound))
		{
			return new ResponseWrapper(404, "Country Not Found In Border Map List");
		}
		else if(Boolean.FALSE.equals(l_neighbourFound))
		{
			return new ResponseWrapper(404, "Bordering Country Not Found In Border List");
		}
		return new ResponseWrapper(200, "Removed Neighbouring Country Successfully");

	}

	/**
	 * Method to validate the map once a change has been made.
	 * For example we want to add a country into a map with a continent that does not exist, then it's
	 * an error that should not occur.
	 *
	 * @return alert either the map is validated or alert a specific error message
	 */
	public ResponseWrapper validateMap() {

		if(this.d_mapModel.getContinents() == null  ) {
			return new ResponseWrapper(404, "In map there are no continents");
		}
		if(this.d_mapModel.getCountries() == null) {
			return new ResponseWrapper(404, "In map there are no countries");
		}
		Set<Continent> l_continents = new HashSet<Continent>(this.d_mapModel.getContinents());
		Set<Country> l_countries = new HashSet<Country>(this.d_mapModel.getCountries());

		for(Continent cont : l_continents)
		{
			try {
				Integer.parseInt(cont.getContientValue());

			} catch (Exception exc) {
				return new ResponseWrapper(404, " COntinent Value is not good");
			}

		}

		if ("".equals(this.d_mapModel.getMapName()) || this.d_mapModel.getMapName() == null || this.d_mapModel.getContinents().isEmpty()
				|| this.d_mapModel.getCountries().isEmpty() || this.d_mapModel.getContinentCountries().isEmpty()
				|| this.d_mapModel.getBorders().isEmpty()) {

			return new ResponseWrapper(404, "Map is not created Properly");

		} else if (l_continents.size() != this.d_mapModel.getContinents().size()
				|| l_countries.size() != this.d_mapModel.getCountries().size()) {
			return new ResponseWrapper(404, "Duplicate Continent or Country Found in map");

		} else if (l_countries.size() < 2) {
			return new ResponseWrapper(404, " Countries Should be Atleast 2 in map ");

		}



		Boolean l_countryContinentNotExists = this.d_mapModel.getCountries().stream()
				.anyMatch((country) -> country.getContinent() == null
						|| country.getContinent().getContientValue() == null
						|| "".equals(country.getContinent().getContinentId())
						|| "".equals(country.getContinent().getContientValue()));

		if (Boolean.TRUE.equals(l_countryContinentNotExists)) {
			return new ResponseWrapper(404, "Country's Continent Data is missing");
		}

		Boolean l_countryContinentExistsInContinentsList=false;
		for(Country count : this.d_mapModel.getCountries())
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
		for(Map.Entry<Country, List<Country>> mapEntry : d_mapModel.getBorders().entrySet()) {
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




		Boolean l_countryBorderNotExists = this.d_mapModel.getBorders().values().stream()
				.anyMatch(borderList -> borderList.isEmpty()) ? true : false;

		if (Boolean.TRUE.equals(l_countryBorderNotExists)) {
			return new ResponseWrapper(404, " Countries Border Missing ");
		}

		return new ResponseWrapper(200, "VALIDATION SUCCESSFUL ");
	}

	/**
	 * getter method for MapModel
	 * @return d_mapModel - map model
	 */
	public MapModel getMapModel() {
		return d_mapModel;
	}

	/**
	 * getter method for MapModel
	 * @return gameModel - game model
	 */
	public GameModel getGameModel() {
		return gameModel;
	}
}
