import GamePhase.MapPhaseState;

import business.GameProgress;
import business.MainPlayPhaseBusinessCommands;
import business.Phase;
import controller.WarzoneEngineController;
import logger.ConsoleWriter;
import logger.LogGenerator;
import logger.LogEntryBuffer;
import model.ResponseWrapper;

/**
 * Class to start the game with multiple options to the user.
 * @author Rohit
 * @author Ishaan Bajaj
 * @author Raghav Manchanda
 * @author Bhavya Bugude
 * @author Sumit Kumar
 * @author Kevin
 * @version build 1
 */
public class WarzoneEngine {


	/**
	 * Data member of WarZoneEngineController
	 */
	private WarzoneEngineController d_warzoneEngineController;
	/**
	 * LogGenerator to generate engine logs
	 */
	private LogGenerator d_logGenrator;
	/**
	 * MapEngine object to offer map options
	 */
	private MapEngine d_mapEngine;
	
	/**
	 * to print the statements in the console
	 */
	private LogEntryBuffer d_logger;
	private ConsoleWriter d_consoleWriter;

	private SingleGameModePlayEngine singleGameModePlayEngine;
	private TournamentEngine tournamentEngine;
	private Phase playPhase;
	private MainPlayPhaseBusinessCommands mainPlayPhaseBusinessCommands;
	GameProgress gameProgress;

	/**
	 * constructor for WarZoneEngine
	 */
	public WarzoneEngine() {
		d_warzoneEngineController = new WarzoneEngineController();
		d_mapEngine = new MapEngine();
		d_logGenrator = LogGenerator.getInstance();
		singleGameModePlayEngine = new SingleGameModePlayEngine();
		mainPlayPhaseBusinessCommands = new MainPlayPhaseBusinessCommands();
		tournamentEngine = new TournamentEngine();
		d_consoleWriter = new ConsoleWriter();
		d_logger = new LogEntryBuffer();
		d_logger.addObserver(d_consoleWriter);
		d_logger.addObserver(d_logGenrator);
		gameProgress = new GameProgress();

	}


	/**
	 * method to print all the available commands at the start New Game, Load Game,
	 * Single Game Mode, Tournament Mode, Exit Game
	 */

	private void printAvailableWarZoneCommands() {

		// Initialising war zone game comments
		// printout different features
		// 1. Map Features
		// 2. Single game mode between multiple players
		// 3. tournament mode between multiple players

		d_logger.setLogMessage("");
		d_logger.setLogMessage("****************************************");
		d_logger.setLogMessage("*********** RISK GAME by W13 ***********");
		d_logger.setLogMessage("****************************************");
		d_logger.setLogMessage("************** MAIN MENU ***************");
		d_logger.setLogMessage("****************************************");
		d_logger.setLogMessage("");
		d_logger.setLogMessage("-> To start the game: gamestart -new");
		d_logger.setLogMessage("-> To enter tournament mode: gamestart -tournament");
		d_logger.setLogMessage("-> To load an existing game: gamestart -loadgame");
		d_logger.setLogMessage("-> To end the game: gameend -end");
		d_logger.setLogMessage("");
		d_logger.setLogMessage("***** Input any command to proceed *****");
		d_logger.setLogMessage("****(Getting input from the user...)****");


	}

	/**
	 * method to start the game
	 * @throws Exception - input command is incorrect
	 */

	public synchronized void gameStarts() throws Exception{
		while (true) {
			d_logGenrator.logInfoMsg("WAR ZONE GAME STARTS", 'I');
			this.printAvailableWarZoneCommands();

			// get commands input from warZone controller file
			String userInput = this.d_warzoneEngineController.getGameFeatureInputs();
			if(userInput.equals("gamestart -new")) {
				d_logGenrator.logInfoMsg("MAP ENGINE STARTS", 'I');
				// sends to map engine
				ResponseWrapper responseWrapper = d_mapEngine.startMapEngine();
				
				// clear Map states
				MapPhaseState.clearMapPhaseStates();
				
				if(responseWrapper.getStatusValue() == 201) {
					//start game
					singleGameModePlayEngine.startGamePlayMode();
				}
				
				break;
			}
			else if (userInput.equals("gamestart -tournament")){
				tournamentEngine.startTournamentMode();
			} else if (userInput.equals("gamestart -loadgame")) {
				gameProgress.LoadGame("");
			} else if(userInput.equals("gameend -end")) {
				System.out.println("Game Ending!");
				System.exit(0);
			} else {
				System.out.println("Please Enter Proper Command!");
				continue;
			}
			
			break;
		}

	}

}
