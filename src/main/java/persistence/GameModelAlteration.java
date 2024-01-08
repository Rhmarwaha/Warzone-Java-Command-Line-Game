package persistence;

import model.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import controller.MainPlayPhaseController;


/**
 * GameModelAlteration class
 * @author ishaanbajaj
 * @author Rohit
 * @version build 2
 */
public class GameModelAlteration implements Serializable {
	/**
	 * Object of gamemodel class
	 */
	private GameModel gameModel;
	/**
	 * Object of mapmodel class
	 */
	private MapModel mapModel;

	/**
	 * Default Constructor
	 */
	public GameModelAlteration() {
		gameModel = GameModel.getInstance();
		mapModel = MapModel.getInstance();
	}

	/**
	 * method to add player in the game
	 * @param playerName string player name
	 * @return response
	 */
	public ResponseWrapper addPlayerInGame(String playerName) {
		Player player = new Player(playerName);
		this.gameModel.addPlayerInPlayersList(player);
		this.gameModel.addPlayerQueue(player);

		gameModel.changeStrat(player, MainPlayPhaseController.getInstance());

		return new ResponseWrapper(200,"Player added successfully: " + playerName);

	}

	/**
	 * method to remove player from game
	 * @param playerName playername string
	 * @return response
	 */
	public ResponseWrapper removePlayerFromGame(String playerName) {

		Boolean playerFoundToBeRemoved=false;

		if(this.gameModel.getPlayers()==null)
		{
			return new ResponseWrapper(200,"Player doesnt exist in the map");
		}
		for(Player player: this.gameModel.getPlayers()) {
			if(player.getPlayerName().equals(playerName)) {
				this.gameModel.getPlayers().remove(player);
				break;
			}
		}
		if(Boolean.FALSE.equals(playerFoundToBeRemoved))
		{
			return new ResponseWrapper(200,"Player doesnt exist in the map");
		}
		return new ResponseWrapper(200,"Player removed successfully.");
	}

	/**
	 * method to assign countries
	 * @return response
	 */
	public ResponseWrapper assignCountries() {
		int index = 0;
		List<Player> l_inGamePlayers = this.gameModel.getPlayers();

		List<Country> l_availableCountriesList = this.mapModel.getCountries();
		Collections.shuffle(l_availableCountriesList);

		for (Country l_Country : l_availableCountriesList) {
			Player l_Player = l_inGamePlayers.get(index);
			l_Player.addCountryHold(l_Country);
			l_Country.setCountryOwner(l_Player);

			if (index < this.gameModel.getPlayers().size() - 1) {
				index++;
			} else {
				index = 0;
			}
		}

		return new ResponseWrapper(200, "Countries Assigned");
	}




}
