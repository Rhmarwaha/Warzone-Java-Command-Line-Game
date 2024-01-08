package business;

import logger.GeneralException;
import model.Continent;
import model.Country;
import model.Player;
import model.ResponseWrapper;

import java.io.Serializable;


/**
 * GameEngine class to start the "WarZone Engine" which contains the implementation of GamePhases.
 *
 * @author raghav
 * @version build 2
 */
public abstract class Phase  implements Serializable {
	
	
	protected Phase() {
	}
	
	/**
	 * Method that converts input string commands into objects to be used for deploy execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_country - Country where armies will be deployed in
	 * @param p_numerOfarmies - Number of armies that will be deployed
	 * @return alert message that deploy is successful or unsuccessful
	 * @throws GeneralException throwing general exception
	 */
	public abstract ResponseWrapper deploy(Player p_currentPlayer, String p_country, int p_numerOfarmies) throws GeneralException;
	
	/**
	 * Method that converts input string commands into objects to be used for advance execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_countryNameFrom - Source country where armies are moving from
	 * @param p_countryNameTo - Destination country where armies are moving to
	 * @param p_numerOfarmies - Number of armies being displaced or attacking
	 * @return alert message that advance is successful or unsuccessful
	 * @throws GeneralException throwing general exception
	 */
	public abstract ResponseWrapper advance(Player p_currentPlayer, String p_countryNameFrom, String p_countryNameTo, int p_numerOfarmies) throws GeneralException;

	/**
	 * Method that converts input string commands into objects to be used for bomb execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_targetCountryName - Name of country that will be bombed
	 * @return alert message that bomb is successful or unsuccessful
	 * @throws GeneralException throwing general exception
	 */
	public abstract ResponseWrapper bomb(Player p_currentPlayer, String p_targetCountryName) throws GeneralException;
	
	/**
	 * Method that converts input string commands into objects to be used for blockade execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_targetCountryName - Name of country in which a blockade is performed on
	 * @throws GeneralException throwing general exception
	 * @return alert message that blockade is successful or unsuccessful
	 */
	public abstract ResponseWrapper blockade(Player p_currentPlayer, String p_targetCountryName) throws GeneralException;
	
	/**
	 * Method that converts input string commands into objects to be used for airlift execution
	 * @param p_currentPlayer - Current player object that is inputing string command
	 * @param p_countryNameFrom - Source country that will airlift the armies to a destination
	 * @param p_countryNameTo - Destination Country that will receive armies from airlift
	 * @param p_numArmies - Number of armies being displaced by airlift
	 * @return alert message that airlift is successful or unsuccessful
	 * @throws GeneralException throwing general exception
	 */
	public abstract ResponseWrapper airlift(Player p_currentPlayer, String p_countryNameFrom, String p_countryNameTo, int p_numArmies) throws GeneralException;

	/**
	 * Method that converts input string commands into objects to be used for diplomacy execution
	 * @param p_currentPlayer - player executing diplomacy with another player
	 * @param p_otherPlayer - player with whom a peace order is executed on
	 * @return alert message that diplomacy is successful or unsuccessful
	 * @throws GeneralException throwing general exception
	 */
	public abstract ResponseWrapper diplomacy(Player p_currentPlayer, String p_otherPlayer) throws GeneralException;

	/**
	 * Method to commit the commands entered by individual players.
	 * @param p_currentPlayer current player
	 * @return ResponseWrapper returning response
	 * @throws GeneralException if anything goes wrong
	 */
	public abstract ResponseWrapper commit(Player p_currentPlayer) throws GeneralException;
	/**
	 * method to show current state of map
	 * @throws GeneralException if anything goes wrong
	 * @return alert message that map is properly showing
	 */
	public abstract ResponseWrapper showMap() throws GeneralException;

	/**
	 * method to show Invalid Command to the user
	 * @return alert message that command is invalid
	 */
	public ResponseWrapper printInvalidCommandInState() {
		return new ResponseWrapper(404, "Incorrect Command in Current State");
	}

	/**
	 * method to assign countries to the player
	 * @throws GeneralException if anything goes wrong
	 * @return alert message that command is invalid
	 */
	public abstract ResponseWrapper assignCountries() throws GeneralException;

	/**
     * Edit a continent to the map.
     *
     * @param p_continent - The continent to be added or removed
     * @param p_command - Check command is add or remove
     * @throws GeneralException if anything goes wrong
     * @return A {@link ResponseWrapper} object indicating the result of the operation.
     */
	public abstract ResponseWrapper editContinent(Continent p_continent, String p_command) throws GeneralException;

	/**
     * Add a player to the game
     *
     * @param p_playerName - player name to be added
     * @throws GeneralException if anything goes wrong
     * @return A {@link ResponseWrapper} object indicating the result of the operation
     */
	public abstract ResponseWrapper addPlayerInGame(String p_playerName) throws GeneralException;

	/**
     * Remove a player from the game
     *@throws GeneralException if anything goes wrong
     * @param p_playerName - player name to be removed
     * @return A {@link ResponseWrapper} object indicating the result of the operation
     */
	public abstract ResponseWrapper removeplayerFromGame(String p_playerName) throws GeneralException;

	/**
     * Commit the reinforcement
     * @throws GeneralException if anything goes wrong
     * @return A {@link ResponseWrapper} object indicating the result of the operation
     */
	public abstract ResponseWrapper afterCommitReinforcement()
			throws GeneralException;

	/**
     * Edit a neighbor of the country to the map.
     *
     * @param p_mainCountry - The country where neighboring country has to be added or removed
     * @param p_neighbourCountry - The neighboring country to be added or removed
     * @param p_command - Check command is add or remove
     * @throws GeneralException if anything goes wrong
     * @return A {@link ResponseWrapper} object indicating the result of the operation
     */
	public abstract ResponseWrapper editNeighbour(Country p_mainCountry, Country p_neighbourCountry, String p_command)
			throws GeneralException;
	
	/**
     * Edit a country to the map.
     *
     * @param p_country - The country to be added or removed
     * @param p_command - The command to be added or removed
     * @throws GeneralException if anything goes wrong
     * @return A {@link ResponseWrapper} object indicating the result of the operation.
     */
	public abstract ResponseWrapper editCountry(Country p_country, String p_command) throws GeneralException;

	/**
	 * method to check if the map is valid
	 * @throws GeneralException if anything goes wrong
	 * @return alert message that map is valid or not
	 */
	public abstract ResponseWrapper validateMap() throws GeneralException;

	/**
	 * method to check if the map is valid
	 * @param p_map - map name that has to be loaded
	 * @throws GeneralException if anything goes wrong
	 * @return alert message that map is valid or not
	 */
	public abstract ResponseWrapper loadMap(String p_map) throws GeneralException;

	/**
	 * method to save current state of map
	 * @param p_mapFileName - name of map file
	 * @throws GeneralException if anything goes wrong
	 * @return alert message that map has successfully been saved
	 */
	public abstract ResponseWrapper saveMap(String p_mapFileName, Boolean saveAsConquest) throws GeneralException;
	
	/**
	 * It will open the map if the map is already in the map folder or else it will create a new map in the
	 * map folder
	 * @param p_mapFileName - name of map file
	 * @throws GeneralException if anything goes wrong
	 * @return alert message if the map has been successfully created or edited
	 */

	public abstract ResponseWrapper editOrCreateMap(String p_mapFileName) throws GeneralException;

	/**
	 * Reinforcement the army
	 * @throws GeneralException if anything goes wrong
	 * @return alert message that map has successfully been saved
	 */
	public abstract ResponseWrapper doReinforcements() throws GeneralException;

}
