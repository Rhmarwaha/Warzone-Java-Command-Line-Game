/**
 * The `MapEngineController` class is responsible for handling map-related commands and interactions between the user interface and the map editing functionality.
 * It processes user input and delegates commands to the `ExecuteMapsCommands` class for map manipulation.
 *
 * <p>
 * This class supports commands for editing continents, countries, neighbors, showing the map, saving the map, and validating the map.
 * </p>
 *
 * @see ExecuteMapsCommands
 * @author Rohit
 * @version 1.0
 */
package controller;

import java.util.Scanner;
import java.util.logging.Logger;

import business.ExecuteMapsCommands;
import business.Phase;
import logger.ConsoleWriter;
import logger.GeneralException;
import logger.LogEntryBuffer;
import logger.LogGenerator;
import model.Continent;
import model.Country;
import model.ResponseWrapper;

/**
 * The `MapEngineController` class is responsible for handling map-related commands and interactions between the user interface and the map editing functionality.
 * It processes user input and delegates commands to the `ExecuteMapsCommands` class for map manipulation.
 *
 * <p>
 * This class supports commands for editing continents, countries, neighbors, showing the map, saving the map, and validating the map.
 * </p>
 *
 * @see ExecuteMapsCommands
 * @author Rohit
 * @author ishaanbajaj
 * @version build 2
 */
public class MapEngineController {

	/**
	 * scanner
	 */
	private static Scanner d_inputForMapCommands;
	/**
	 * Object of ExecuteMapCommands
	 */
	private ExecuteMapsCommands d_executeMapsCommands;
	/**
	 * LogGenerator
	 */
	private LogGenerator d_logGenrator;
	/**
	 * Object of Phase - mapPhase
	 */
	private Phase mapPhase;

	/**
	 * default error message for incorrect input command
	 */
	public static final String INCORRECT_COMMAND = "Please enter proper command";
	/**
	 * GeneralException
	 */
	private GeneralException gException = new GeneralException();

	private LogEntryBuffer d_logger;
	private ConsoleWriter d_consoleWriter;

	/**
	 * Enumerates the supported map-related commands.
	 */
	enum MapCommands {
		EDIT_CONTINENT("editcontinent"), EDIT_COUNTRY("editcountry"), EDIT_NEIGHBOR("editneighbor"),
		SHOW_MAP("showmap"), SAVE_MAP("savemap"), EDIT_MAP("editmap"), VALIDATE_MAP("validatemap"), NONE("nothing");


		public final String l_exactCommand;

		MapCommands(String p_exactCommand) {
			this.l_exactCommand = p_exactCommand;
		}


	}

	/**
	 * add player string command
	 */
	private static final String COMMAND_ADD = "-add";
	/**
	 * remove player string command
	 */
	private static final String COMMAND_REMOVE = "-remove";

	/**
	 * method to set phase to map phase
	 * @param mapPhase map phase state
	 */
	public void setMapPhase(Phase mapPhase) {
		this.mapPhase = mapPhase;
	}

	/**
	 * Default Constructor for the `MapEngineController` class.
	 * Initializes the input scanner, map command executor, and log generator.
	 */
	public MapEngineController() {
		d_inputForMapCommands = new Scanner(System.in);
		d_executeMapsCommands = new ExecuteMapsCommands();
		this.d_logGenrator = LogGenerator.getInstance();
		setMapPhase(d_executeMapsCommands);
		d_consoleWriter = new ConsoleWriter();
		d_logger = new LogEntryBuffer();
		d_logger.addObserver(d_consoleWriter);
		d_logger.addObserver(d_logGenrator);
	}

	/**
	 * Gets the main map commands from the user and processes them.
	 *
	 * @return A {@link ResponseWrapper} indicating the result of the command execution.
	 * @throws GeneralException If there is an error during command execution.
	 */
	public ResponseWrapper getMainMapCommandsFromUser() throws GeneralException {

		String l_userEnteredMainMapCommands = d_inputForMapCommands.nextLine();

		if (l_userEnteredMainMapCommands.trim().isEmpty()) {

			return new ResponseWrapper(404, INCORRECT_COMMAND); // nothing entered please enter proper
			// command
		}
		String[] l_splitMainMapCommand = l_userEnteredMainMapCommands.trim().replaceAll(" +", " ").split("\\s+");

		gException.validateCommand(l_userEnteredMainMapCommands);

		switch (l_splitMainMapCommand[0]) {

			case "editmap":

				ResponseWrapper mapResponse = mapPhase.editOrCreateMap(l_splitMainMapCommand[1]);
				ResponseWrapper l_response = mapPhase.validateMap();
				if (l_response.getStatusValue() == 404) {
					System.out.println("Validation Failed : " + l_response.getDescription());
				} else {
					System.out.println(l_response.getDescription());

				}
				return mapResponse;
			case "continue":


				d_logGenrator.clearLogs();
				return new ResponseWrapper(201, "Return from current command and move to game play");


			default:
				return new ResponseWrapper(404, INCORRECT_COMMAND); // nothing entered please enter proper
			// command
		}

	}

	/**
	 * Gets the edit map commands from the user and processes them.
	 *
	 * @return A {@link ResponseWrapper} indicating the result of the command execution.
	 * @throws GeneralException If there is an error during command execution.
	 */
	public ResponseWrapper getEditMapCommandsFromUser() throws GeneralException {

		String l_userEnteredCommand = d_inputForMapCommands.nextLine();
		if (l_userEnteredCommand.isEmpty()) {
			return new ResponseWrapper(404, INCORRECT_COMMAND);
		}
		// Filter
		String[] l_splittedCommands = l_userEnteredCommand.trim().replaceAll(" +", " ").split("\\s+");
		gException.validateCommand(l_userEnteredCommand);

		switch (l_splittedCommands[0]) {
			// edit continents
			case "editcontinent":

				return handleEditContinentCommand(l_splittedCommands);

			// edit country
			case "editcountry":

				return handleEditCountryCommand(l_splittedCommands);

			case "editneighbour":

				return handleEditNeighborCommand(l_splittedCommands);

			case "showmap":

				return mapPhase.showMap();

			case "savemap":
				d_logger.setLogMessage(" Select the map file format you want to save the map in:");
				d_logger.setLogMessage("1. Domination map");
				d_logger.setLogMessage("2. Conquest map");
				String userInputForMap = d_inputForMapCommands.nextLine();
				if (userInputForMap.equals("1")) {
					return mapPhase.saveMap(l_splittedCommands[1], false);
				}else if (userInputForMap.equals("2")){
					return mapPhase.saveMap(l_splittedCommands[1], true);
				}
				else {
					return new ResponseWrapper(300, "Please enter the right value");
				}

			case "validatemap":

				return mapPhase.validateMap();

			case "exit":

				d_logGenrator.clearLogs();
				return new ResponseWrapper(204, "Return from current command");

			case "deploy":

				return mapPhase.deploy(null, null, 0);

			case "advance":

				return mapPhase.advance(null, null, null, 0);

			case "bomb":

				return mapPhase.bomb(null, null);

			case "loadmap":

				return mapPhase.loadMap(null);

			case "gameplayer":

				return mapPhase. addPlayerInGame(l_userEnteredCommand);

			case "assigncountries":

				return mapPhase.assignCountries();

			default:
				return new ResponseWrapper(404, INCORRECT_COMMAND);


		}
	}

	/**
	 * method to handle edit continent commands
	 * @param p_splittedCommands get command from user
	 * @return response
	 */
	private ResponseWrapper handleEditContinentCommand(String[] p_splittedCommands) {
		switch (p_splittedCommands[1]) {

			case COMMAND_ADD:

				// call business file to execute command
				if (p_splittedCommands.length == 4) {
					Continent l_continent = new Continent(p_splittedCommands[2], p_splittedCommands[3]);
					return d_executeMapsCommands.addContinent(l_continent);
				} else {
					return new ResponseWrapper(404, INCORRECT_COMMAND);
				}

			case COMMAND_REMOVE:
				if (p_splittedCommands.length == 3) {
					Continent l_continent = new Continent(p_splittedCommands[2]);
					return d_executeMapsCommands.removeContinent(l_continent);
				} else {
					return new ResponseWrapper(404, INCORRECT_COMMAND);
				}
				// call business file to execute command

			default:
				return new ResponseWrapper(404, INCORRECT_COMMAND);

		}
	}

	/**
	 * method to handle edit country commands
	 * @param p_splittedCommands user commands split according to code
	 * @return response
	 */
	private ResponseWrapper handleEditCountryCommand(String[] p_splittedCommands){
		switch (p_splittedCommands[1]) {
			case COMMAND_ADD:

				if (p_splittedCommands.length == 4) {
					Country l_country = new Country(p_splittedCommands[2], new Continent(p_splittedCommands[3]));
					return d_executeMapsCommands.addCountry(l_country);
				} else {
					// please provide proper parameters
					return new ResponseWrapper(404, INCORRECT_COMMAND);
				}


			case COMMAND_REMOVE:
				if (p_splittedCommands.length == 3) {
					Country l_country = new Country(p_splittedCommands[2]);
					return d_executeMapsCommands.removeCountry(l_country);
				} else {
					// please provide proper parameters
					return new ResponseWrapper(404, INCORRECT_COMMAND);
				}

			default:
				return new ResponseWrapper(404, INCORRECT_COMMAND);

		}
	}

	/**
	 * method to handle edit neighbor commands
	 * @param p_splittedCommands user commands split according to code
	 * @return response
	 */
	private ResponseWrapper handleEditNeighborCommand(String[] p_splittedCommands){
		switch (p_splittedCommands[1]) {
			case COMMAND_ADD:
				if (p_splittedCommands.length == 4) {
					Country l_country = new Country(p_splittedCommands[2]);
					Country p_neighbourCountry = new Country(p_splittedCommands[3]);
					return d_executeMapsCommands.addNeighbour(l_country, p_neighbourCountry);

				} else {
					return new ResponseWrapper(404, INCORRECT_COMMAND);
				}

			case COMMAND_REMOVE:
				if (p_splittedCommands.length == 4) {
					Country l_country = new Country(p_splittedCommands[2]);
					Country l_neighbourCountry = new Country(p_splittedCommands[3]);
					return d_executeMapsCommands.removeNeighbour(l_country, l_neighbourCountry);

				} else {
					return new ResponseWrapper(404, INCORRECT_COMMAND);
				}

			default:
				return new ResponseWrapper(404, INCORRECT_COMMAND);

		}
	}

}