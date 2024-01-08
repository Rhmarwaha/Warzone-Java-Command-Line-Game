package business;


import Constants.ProjectConfig;
import GamePhase.MapPhaseState;
import logger.GeneralException;
import model.Continent;
import model.Country;
import model.Player;
import model.ResponseWrapper;
import persistence.Adaptee;
import persistence.Adapter;
import persistence.GameModelAlteration;
import persistence.MapFileAlteration;

import java.io.*;

/**
 * @author ishaanbajaj
 * @author rohit
 * @author kevin
 * @version build 2
 */

public class SingleGamePlayerCommands extends Phase{


	/**
	 * Object of MapFileAlteration class
	 */
	private MapFileAlteration d_mapFileAlteration;
	/**
	 * Object of GameModeAlteration class
	 */
	private GameModelAlteration gameModelAlteration;

	private Adapter d_adapter = new Adapter(new Adaptee());

	/**
	 * Default Constructor of SingleGmePlayerCommands
	 */
	public SingleGamePlayerCommands() {
		d_mapFileAlteration = new MapFileAlteration();
		gameModelAlteration = new GameModelAlteration();

	}

	/**
	 * method to check if the map is valid
	 * @param p_map - map name that has to be loaded
	 * @return alert message that map is valid or not
	 */
	@Override
	public ResponseWrapper loadMap(String p_map) throws GeneralException{

		MapPhaseState.D_CURRENT_MAP = p_map;
		boolean l_UseConquestAdapter = true;
		File l_File = new File(ProjectConfig.D_MAP_FILES_PATH+MapPhaseState.D_CURRENT_MAP);
		try {
			BufferedReader l_BufferedReader = new BufferedReader(new FileReader(l_File));
			while(l_BufferedReader.ready()) {
				String l_FirstLine = l_BufferedReader.readLine();
				if(! l_FirstLine.isEmpty()) {
					if (l_FirstLine.contains(";")) {
						l_UseConquestAdapter = false;
					}
					l_BufferedReader.close();
				}}
		} catch (IOException e) {
			// Do Nothing.
		}
		if (l_UseConquestAdapter){
			d_adapter.readMapFile();
		} else {
			d_mapFileAlteration.readMapFile();
		}

		return new ResponseWrapper(200, "");

	}

	/**
	 * Add a player to the game
	 *
	 * @param p_playerName - player name to be added
	 * @return A {@link ResponseWrapper} object indicating the result of the operation
	 */
	@Override
	public ResponseWrapper addPlayerInGame(String p_playerName) throws GeneralException {

		return this.gameModelAlteration.addPlayerInGame(p_playerName);
	}

	/**
	 * Remove a player from the game
	 *
	 * @param p_playerName - player name to be removed
	 * @return A {@link ResponseWrapper} object indicating the result of the operation
	 */
	@Override
	public ResponseWrapper removeplayerFromGame(String p_playerName) throws GeneralException {

		return this.gameModelAlteration.removePlayerFromGame(p_playerName);
	}

	/**
	 * method to assign countries to the player
	 * @return alert message that command is invalid
	 */
	@Override
	public ResponseWrapper assignCountries() throws GeneralException {

		return this.gameModelAlteration.assignCountries();
	}

	/**
	 * Method that converts input string commands into objects to be used for advance execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_countryNameFrom - Source country where armies are moving from
	 * @param p_countryNameTo - Destination country where armies are moving to
	 * @param p_numerOfarmies - Number of armies being displaced or attacking
	 * @return alert message that advance is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper advance(Player p_currentPlayer, String p_countryNameFrom, String p_countryNameTo,
								   int p_numerOfarmies) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * method to show current state of map
	 * @return alert message that map is properly showing
	 */
	@Override
	public ResponseWrapper showMap() throws GeneralException {
		return this.d_mapFileAlteration.showmap();
	}

	/**
	 * Method that converts input string commands into objects to be used for deploy execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_country - Country where armies will be deployed in
	 * @param p_numerOfarmies - Number of armies that will be deployed
	 * @return alert message that deploy is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper deploy(Player p_currentPlayer, String p_country, int p_numerOfarmies) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Edit a continent to the map.
	 *
	 * @param p_continent - The continent to be added or removed
	 * @param p_command - The command to be added or removed
	 * @return A {@link ResponseWrapper} object indicating the result of the operation.
	 */
	@Override
	public ResponseWrapper editContinent(Continent p_continent, String p_command) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Commit the reinforcement
	 * @return A {@link ResponseWrapper} object indicating the result of the operation
	 */
	@Override
	public ResponseWrapper afterCommitReinforcement() throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Edit a neighbor of the country to the map.
	 *
	 * @param p_mainCountry - The country where neighboring country has to be added or removed
	 * @param p_neighbourCountry - The neighboring country to be added or removed
	 * @param p_command - Check command is add or remove
	 * @return A {@link ResponseWrapper} object indicating the result of the operation
	 */
	@Override
	public ResponseWrapper editNeighbour(Country p_mainCountry, Country p_neighbourCountry, String p_command)
			throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Edit a country to the map.
	 *
	 * @param p_country - The country to be added or removed
	 * @param p_command - The command to be added or removed
	 * @return A {@link ResponseWrapper} object indicating the result of the operation.
	 */
	@Override
	public ResponseWrapper editCountry(Country p_country, String p_command) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * method to check if the map is valid
	 * @return alert message that map is valid or not
	 */
	@Override
	public ResponseWrapper validateMap() throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * method to save current state of map
	 * @param p_mapFileName - name of map file
	 * @return alert message that map has successfully been saved
	 */
	@Override
	public ResponseWrapper saveMap(String p_mapFileName, Boolean saveAsConquest) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * It will open the map if the map is already in the map folder or else it will create a new map in the
	 * map folder
	 * @param p_mapFileName - name of map file
	 * @return alert message if the map has been successfully created or edited
	 */
	@Override
	public ResponseWrapper editOrCreateMap(String p_mapFileName) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Method that converts input string commands into objects to be used for bomb execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_targetCountryName - Name of country that will be bombed
	 * @return alert message that bomb is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper bomb(Player p_currentPlayer, String p_targetCountryName) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Method that converts input string commands into objects to be used for blockade execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_targetCountryName - Name of country in which a blockade is performed on
	 * @return alert message that blockade is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper blockade(Player p_currentPlayer, String p_targetCountryName) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Method that converts input string commands into objects to be used for airlift execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_countryNameFrom - Source country that will airlift the armies to a destination
	 * @param p_countryNameTo - Destination Country that will receive armies from airlift
	 * @param p_numArmies - Number of armies being displaced by airlift
	 * @return alert message that airlift is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper airlift(Player p_currentPlayer, String p_countryNameFrom, String p_countryNameTo, int p_numArmies)  throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Method that converts input string commands into objects to be used for diplomacy execution
	 * @param p_currentPlayer - player executing diplomacy with another player
	 * @param p_otherPlayer - player with whom a peace order is executed on
	 * @return alert message that diplomacy is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper diplomacy(Player p_currentPlayer, String p_otherPlayer) throws GeneralException{
		return printInvalidCommandInState();
	}

	@Override
	public ResponseWrapper commit(Player player) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Reinforcement the army
	 * @return alert message that map has successfully been saved
	 */
	@Override
	public ResponseWrapper doReinforcements() throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * method to get object
	 * @return MapFileAlteration object
	 */
	public MapFileAlteration getD_mapFileAlteration() {
		return d_mapFileAlteration;
	}

	/**
	 * method to set object
	 * @param d_mapFileAlteration - set MapFileAlteration object
	 */
	public void setD_mapFileAlteration(MapFileAlteration d_mapFileAlteration) {
		this.d_mapFileAlteration = d_mapFileAlteration;
	}



}
