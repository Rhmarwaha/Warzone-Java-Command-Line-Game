package business;

import model.Continent;
import model.Country;
import model.GameModel;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;
import business.Order.*;
import logger.GeneralException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * MainPlayPhaseBusinessCommands class to issue the commands of user
 * @author kevin
 * @author raghav
 * @author ishaan
 * @version build 2
 */
public class MainPlayPhaseBusinessCommands extends Phase implements Serializable {

	/**
	 * Object of MapModel class
	 */
	private MapModel mapModel;
	/**
	 * Object of GameModel class
	 */
	private GameModel gameModel;

	private static MainPlayPhaseBusinessCommands d_mainPlayPhaseBusinessCommands;

	/**
	 * Set of player that commit
	 */
	private static Set<Player> CommittedPlayers = new HashSet<>();

	/**
	 * Default constructor
	 */
	public MainPlayPhaseBusinessCommands() {
		mapModel = MapModel.getInstance();
		gameModel = GameModel.getInstance();
	}

	public static MainPlayPhaseBusinessCommands getMainPlayPhaseBusinessCommandsInstance() {
		if(Objects.isNull(d_mainPlayPhaseBusinessCommands)) {
			d_mainPlayPhaseBusinessCommands = new MainPlayPhaseBusinessCommands();
		}
		return d_mainPlayPhaseBusinessCommands;
	}
	/**
	 * Reinforcement the army
	 * @return alert message that map has successfully been saved
	 */
	@Override
	public ResponseWrapper doReinforcements() throws GeneralException{
		System.out.println("ENTER REINFORCEMENT PHASE!!!!");

		for(Continent continent : mapModel.getContinents()) {
			continent.determineContinentOwner();
		}

		for(Player player : gameModel.getPlayers()) {
			player.calculateCurrentArmies();
		}

		return new ResponseWrapper(200, "Reinforcement phase complete");

	}

	/**
	 * Method when the game ends, i.e., the player wins all the countries.
	 * @return response according to the situation
	 * @throws GeneralException general exception
	 */

	public ResponseWrapper endGame() throws GeneralException {
		if (gameModel.numberOfTries >= gameModel.getMaxNumberOfTurns()){
			//mainPlaySetUpResponse.setStatusValue(1000);
			return new ResponseWrapper(1000, "Number of turns have been reached/exceeded!");
		}
		for(Player player : gameModel.getPlayers()) {
			if(player.getCountriesHold().size() == mapModel.getCountries().size()) {
				//mainPlaySetUpResponse.setStatusValue(201);
				return new ResponseWrapper(201, "Player: " + player.getPlayerName() + " CAPTURED ALL COUNTRIES IN THE MAP! GAME ENDS");
			}
		}
		return new ResponseWrapper(199, " GAME HAS NOT ENDED YET");
	}

	/**
	 * Method that converts input string commands into objects to be used for deploy execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_country - Country where armies will be deployed in
	 * @param p_numerOfarmies - Number of armies that will be deployed
	 * @return alert message that deploy is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper deploy(Player p_currentPlayer, String p_country , int p_numerOfarmies) throws GeneralException{
		Country targettedCountry = null;
		for(Country countryInList : p_currentPlayer.getCountriesHold()) {
			if(countryInList.getCountryId().equals(p_country)) {
				targettedCountry = countryInList;
			}
		}

		Order deploy = new DeployOrder(targettedCountry,p_numerOfarmies,p_currentPlayer);
		if(deploy.valid() && p_currentPlayer.getArmiesToIssue()>0) {
			p_currentPlayer.addOrder(deploy);
			p_currentPlayer.setArmiesToIssue(p_currentPlayer.getArmiesToIssue() - p_numerOfarmies);
			return new ResponseWrapper(200, " Deploy order to: " + targettedCountry.getCountryId() + " added in queue");
		} else if (p_currentPlayer.getArmiesToIssue()<=0){
			return new ResponseWrapper(200, "No armies left in this turn.");
		}else {
			return new ResponseWrapper(204,"Targeted country doesn't belong to you.");
		}


	}

	/**
	 * Method that converts input string commands into objects to be used for advance execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_countryNameFrom - Source country where armies are moving from
	 * @param p_countryNameTo - Destination country where armies are moving to
	 * @param p_numerOfarmies - Number of armies being displaced or attacking
	 * @return alert message that advance is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper advance(Player p_currentPlayer, String p_countryNameFrom, String p_countryNameTo, int p_numerOfarmies) throws GeneralException{
		Country countryFrom = null;
		Country countryTo = null;
		for(Country countryInList : p_currentPlayer.getCountriesHold()) {
			if(countryInList.getCountryId().equals(p_countryNameFrom)) {
				countryFrom = countryInList;
				for(Country countryInNeighbours : countryInList.getNeighbors()) {
					if(countryInNeighbours.getCountryId().equals(p_countryNameTo)) {
						countryTo = countryInNeighbours;
					}
				}
			}
		}

		Order advance = new AdvanceOrder(countryFrom, countryTo, p_numerOfarmies, p_currentPlayer);
		if(advance.valid()) {
			p_currentPlayer.addOrder(advance);
			return new ResponseWrapper(200, "Advance order added in queue");
		}else {
			return new ResponseWrapper(204,"One of the following occurred: \n"
					+ "1. Source country does not belong to you\n"
					+ "2. Destination Country is not a neighbouring country\n"
					+ "3. Destination Country does not exist in the map\n"
					+ "4. YOU CANNOT MOVE ALL YOUR ARMIES, MUST LEAVE AT LEAST 1 BEHIND!!!\n");
		}

	}

	/**
	 * Method that converts input string commands into objects to be used for bomb execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_targetCountryName - Name of country that will be bombed
	 * @return alert message that bomb is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper bomb(Player p_currentPlayer, String p_targetCountryName) throws GeneralException{
		Country targetCountry = null;

		for(Country country : mapModel.getCountries()) {
			if(country.getCountryId().equals(p_targetCountryName)) {
				targetCountry = country;
				break;
			}
		}

		Order bomb = new BombOrder(p_currentPlayer, targetCountry);
		if(bomb.valid()) {
			p_currentPlayer.addOrder(bomb);
			return new ResponseWrapper(200, " Bomb order added in queue");
		}else {
			return new ResponseWrapper(204,"One of the following occured: \n"
					+ "1. You do not possess a bomb card\n"
					+ "2. You are targeting a country that belongs to you\n"
					+ "3. You are targeting a country that is not adjacent to one of your countries\n"
					+ "4. That country does not exist in the map\n");
		}
	}

	/**
	 * Method that converts input string commands into objects to be used for blockade execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_targetCountryName - Name of country in which a blockade is performed on
	 * @return alert message that blockade is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper blockade(Player p_currentPlayer, String p_targetCountryName) throws GeneralException {
		Country targetCountry = null;

		for(Country country : p_currentPlayer.getCountriesHold()) {
			if(country.getCountryId().equals(p_targetCountryName)) {
				targetCountry = country;
				break;
			}
		}

		Order blockade = new BlockadeOrder(p_currentPlayer, targetCountry);
		if(blockade.valid()) {
			p_currentPlayer.addOrder(blockade);
			return new ResponseWrapper(200, " Blockade order added in queue");
		}else {
			return new ResponseWrapper(204, "Can only perform blockade on your own country\n");
		}
	}

	/**
	 * Method that converts input string commands into objects to be used for airlift execution
	 * @param p_currentPlayer - Current player object that is inputting string command
	 * @param p_countryNameFrom - Source country that will airlift the armies to a destination
	 * @param p_countryNameTo - Destination Country that will receive armies from airlift
	 * @param p_numArmies - Number of armies being displaced by airlift
	 * @return alert message that airlift is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper airlift(Player p_currentPlayer, String p_countryNameFrom, String p_countryNameTo, int p_numArmies)  throws GeneralException {
		Country countryFrom = null;
		Country countryTo = null;

		for(Country country : p_currentPlayer.getCountriesHold()) {
			if(country.getCountryId().equals(p_countryNameFrom)) {
				countryFrom = country;
			}

			if(country.getCountryId().equals(p_countryNameTo)) {
				countryTo = country;
			}
		}

		Order airlift = new AirliftOrder(p_currentPlayer, countryFrom, countryTo, p_numArmies);
		if(airlift.valid()) {
			p_currentPlayer.addOrder(airlift);
			return new ResponseWrapper(200, " Airlift order added in queue");
		}else {
			return new ResponseWrapper(204, "One of the following occurred: \n"
					+ "1. You do not possess an airlift card\n"
					+ "2. Country you are airlifting from does not belongs to you\n"
					+ "3. Country you are airlifting to does not belongs to you\n"
					+ "4. That country does not exist in the map\n");
		}

	}

	/**
	 * Method that converts input string commands into objects to be used for diplomacy execution
	 * @param p_currentPlayer - player executing diplomacy with another player
	 * @param p_otherPlayer - player with whom a peace order is executed on
	 * @return alert message that diplomacy is successful or unsuccessful
	 */
	@Override
	public ResponseWrapper diplomacy(Player p_currentPlayer, String p_otherPlayer) throws GeneralException{
		Player peaceWith = null;

		for(Player player : gameModel.getPlayers()) {
			if(player.getPlayerName().equals(p_otherPlayer)) {
				peaceWith = player;
			}
		}

		Order diplomacy = new DiplomacyOrder(p_currentPlayer, peaceWith);
		if(diplomacy.valid()) {
			p_currentPlayer.addOrder(diplomacy);
			return new ResponseWrapper(200, " Diplomacy order added in queue");
		}else {
			return new ResponseWrapper(204, " Player you want to make peace with does not exist");
		}
	}


	/**
	 * method to add players in the commit set and execute the orders
	 * @param player player that pressed the commit button
	 */
	public void addCommitPlayer(Player player){
		CommittedPlayers.add(player);
//		if (CommittedPlayers.size() == gameModel.getPlayers().size()){
//			executeOrders();
//		}
	}

	/**
	 * method to execute the orders for each player from the list of orders.
	 */
	public void executeOrders() {
		int l_Counter = 0;
		while (l_Counter < gameModel.getPlayers().size()) {
			l_Counter = 0;
			for (Player l_Player : gameModel.getPlayers()) {
				Order l_Order = l_Player.nextOrder();
				if (l_Order == null) {
					l_Counter++;
				} else {
					l_Order.execute();
					l_Order.printOrder();
				}
			}
		}
		CommittedPlayers.clear();
	}

	/**
	 * method to perform commit for player when player pressed the commit button
	 * @param player current player
	 * @return response
	 * @throws GeneralException if something goes wrong
	 */

	@Override
	public ResponseWrapper commit(Player player) throws GeneralException {
		addCommitPlayer(player);
		player.performCommit();
		//player.resetArmiesToIssue();
		return new ResponseWrapper(200, player.getPlayerName() + " has performed a commit");
	}

	/**
	 * method to show current state of map
	 * @return alert message that map is properly showing
	 */
	@Override
	public ResponseWrapper showMap() throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * method to assign countries to the player
	 * @return alert message that command is invalid
	 */
	@Override
	public ResponseWrapper assignCountries() throws GeneralException {
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
	 * Add a player to the game
	 *
	 * @param p_playerName - player name to be added
	 * @return A {@link ResponseWrapper} object indicating the result of the operation
	 */
	@Override
	public ResponseWrapper addPlayerInGame(String p_playerName) throws GeneralException {
		return printInvalidCommandInState();
	}

	/**
	 * Remove a player from the game
	 *
	 * @param p_playerName - player name to be removed
	 * @return A {@link ResponseWrapper} object indicating the result of the operation
	 */
	@Override
	public ResponseWrapper removeplayerFromGame(String p_playerName) throws GeneralException {
		printInvalidCommandInState();
		return new ResponseWrapper(404, "Incorrect Command");
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
	public ResponseWrapper editNeighbour(Country p_mainCountry, Country p_neighbourCountry, String p_command) throws GeneralException {
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
	 * method to check if the map is valid
	 * @param p_map - map name that has to be loaded
	 * @return alert message that map is valid or not
	 */
	@Override
	public ResponseWrapper loadMap(String p_map) throws GeneralException {
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

}
