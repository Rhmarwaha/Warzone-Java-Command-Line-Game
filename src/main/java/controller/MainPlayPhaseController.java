package controller;

import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

import business.GameProgress;
import business.MainPlayPhaseBusinessCommands;
import business.Phase;
import logger.GeneralException;
import model.GameModel;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;

import static java.lang.System.exit;

/**
 * The Controller will take input commands from user and send skimmed commands to business layer
 * @author Rohit
 * @version build2
 */
public class MainPlayPhaseController implements Serializable {

	/**
	 *  Creating scanner variable to get input from user
	 */
	private static Scanner d_MainPlaylSetupCommands;
	private GeneralException gException;
	/**
	 * Incorrect command message
	 */
	public static final String INCORRECT_COMMAND="Please enter proper command";
	/**
	 * variable to connect to business layer
	 */
	private MainPlayPhaseBusinessCommands mainPlayPhaseBusinessCommands;
	private Phase playPhase;

	private static MainPlayPhaseController mainPlayPhaseController;
	GameProgress gameProgress;
	GameModel d_gameModel;
	MapModel d_mapModel;

	/**
	 * constructor for initializing main play phase controller elements
	 */
	public MainPlayPhaseController() {

		d_MainPlaylSetupCommands = new Scanner(System.in);
		gException=new GeneralException();
		mainPlayPhaseBusinessCommands = new MainPlayPhaseBusinessCommands();
		setPlayPhase(mainPlayPhaseBusinessCommands);
		gameProgress = new GameProgress();
		d_gameModel = GameModel.getInstance();
		d_mapModel = MapModel.getInstance();

	}

	public static MainPlayPhaseController getInstance() {
		if(Objects.isNull(mainPlayPhaseController)) {
			mainPlayPhaseController = new MainPlayPhaseController();
		}
		return mainPlayPhaseController;
	}

	/**
	 * For setting the phases of game like startup phase , main play phase
	 * @param playPhase input for play phase
	 */
	public void setPlayPhase(Phase playPhase) {
		this.playPhase = playPhase;
	}

	/**
	 * getting main play commands from user
	 * @return command from user
	 */
	public String getMainPlaySetUpCommandsFromUser() {
		return d_MainPlaylSetupCommands.nextLine();
	}

	/**
	 * method to get the command from player
	 * @param currentPlayer Current Turn Player
	 * @param l_userEnteredMainPlayCommands user entered command
	 * @return response
	 * @throws GeneralException if something goes wrong
	 */
	public ResponseWrapper getMainPlaySetUpCommands(Player currentPlayer,String l_userEnteredMainPlayCommands) throws GeneralException {


		if (l_userEnteredMainPlayCommands.trim().isEmpty()) {

			return new ResponseWrapper(404, INCORRECT_COMMAND);
		}
		String[] l_splitInitialSetupCommand = l_userEnteredMainPlayCommands.trim().replaceAll(" +", " ").split("\\s+");

		gException.validateCommand(l_userEnteredMainPlayCommands);

		switch (l_splitInitialSetupCommand[0]) {

			case "commit":
				return playPhase.commit(currentPlayer);

			case "deploy":

				return playPhase.deploy(currentPlayer,l_splitInitialSetupCommand[1], Integer.parseInt(l_splitInitialSetupCommand[2]));

			case "advance":
				return playPhase.advance(currentPlayer,l_splitInitialSetupCommand[1],l_splitInitialSetupCommand[2],Integer.parseInt(l_splitInitialSetupCommand[3]));

			case "bomb":
				return playPhase.bomb(currentPlayer, l_splitInitialSetupCommand[1]);

			case "blockade":

				return playPhase.blockade(currentPlayer,l_splitInitialSetupCommand[1]);

			case "airlift":

				return playPhase.airlift(currentPlayer, l_splitInitialSetupCommand[1], l_splitInitialSetupCommand[2], Integer.parseInt(l_splitInitialSetupCommand[3]));

			case "negotiate":

				return playPhase.diplomacy(currentPlayer, l_splitInitialSetupCommand[1]);

			case "loadmap":
				return playPhase.loadMap(null);

			case "gameplayer":

				return playPhase.addPlayerInGame(null);

			case "assigncountries":
				return playPhase.assignCountries();

			case "showmap":


				return playPhase.showMap();


			case "savemap":

				return playPhase.saveMap(null, null);


			case "validatemap":

				return playPhase.validateMap();

			case "editcountry":

				return playPhase.editCountry(null, null);

			case "editneighbour":
				return playPhase.editNeighbour(null, null, null);

			case "editcontinent":
				return playPhase.editContinent(null, null);

			case "savegame":
				if (gameProgress.SaveGame(d_gameModel, d_mapModel, l_splitInitialSetupCommand[1])){
					System.out.println("Saved Game successfully.");
					System.out.println("Exiting Game Now.");
					exit(0);
				} else {
					System.out.println("Could not save game.");
				}

			case "exit":
				System.out.println("Player exit. Ending Game Now!");
				System.exit(0);

			default:
				return new ResponseWrapper(404, INCORRECT_COMMAND);
		}


	}




}
