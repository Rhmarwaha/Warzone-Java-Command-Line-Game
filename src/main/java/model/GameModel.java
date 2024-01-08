package model;

import java.io.Serializable;
import java.util.*;

import Strategy.AggressiveStrategy;
import Strategy.BenevolentStrategy;
import Strategy.CheaterStrategy;
import Strategy.HumanStrategy;
import Strategy.RandomStrategy;
import business.MainPlayPhaseBusinessCommands;
import controller.MainPlayPhaseController;

/**
 * GameModel class to define different objects in the game
 * @author Kevin
 * @author ishaanbajaj
 * @version build 2
 */
public class GameModel implements Serializable {
	/**
	 * List of players in the game
	 */
	private List<Player> players = new ArrayList<>();
	private HashMap<String, Player> d_Players = new HashMap<>();
	/**
	 * Object of player class to get current player
	 */
	private Player currentPlayer;
	public static Scanner scanner = new Scanner(System.in);
	/**
	 * Map of players who have committed
	 */
	private Map<Player,Boolean> commitState;
	/**
	 * Queue of players
	 */
	private Queue<Player> playerQ = new LinkedList<>();

	/**
	 * Object of GameModel class
	 */
	private static GameModel gameModel;

	private boolean permanentStrategy = false;

	public int numberOfTries = 1;

	public int maxNumberOfTurns = 10;

	private Player winner;
	private Boolean isGameLoaded = false;
	MapModel mapModel = new MapModel();

	/**
	 * Default constructor
	 */
	public GameModel() {

	}



	/**
	 * getInstance method
	 * @return gamemodel
	 */
	public static GameModel getInstance() {

		if (Objects.isNull(gameModel)) {
			gameModel = new GameModel();

		}
		return gameModel;
	}

	public int getMaxNumberOfTurns() {
		return maxNumberOfTurns;
	}

	public void setMaxNumberOfTurns(int maxNumberOfTurns) {
		this.maxNumberOfTurns = maxNumberOfTurns;
	}

	public Boolean getGameLoaded() {
		return isGameLoaded;
	}

	public void setGameLoaded(Boolean gameLoaded) {
		isGameLoaded = gameLoaded;
	}

	/**
	 * method to get list of players
	 * @return players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * method to set players in the list
	 * @param players list of players
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public HashMap<String, Player> getD_Players() {
		return d_Players;
	}

	public void setD_Players(HashMap<String, Player> d_Players) {
		this.d_Players = d_Players;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	/**
	 * method to get current player
	 * @return player
	 */
	public Player getCurrentPlayer() {
		if(currentPlayer == null) {
			currentPlayer = players.get(0);
		}
		return currentPlayer;
	}

	/**
	 * method to set current player
	 * @param currentPlayer player
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * method to add players in the list of players
	 * @param player player
	 */
	public void addPlayerInPlayersList(Player player) {

		if(this.players == null) {
			this.players = new ArrayList<>();
		}
		if(this.commitState == null) {
			this.commitState = new HashMap<>();
		}
		if(this.playerQ == null) {
			this.playerQ = new LinkedList<>();
		}
		this.players.add(player);
		this.commitState.put(player, false);
	}

	/**
	 * method to get next player if exists
	 * @return boolean if next player exists
	 */
	public boolean doNextPlayer() {
		if(currentPlayer == null) {
			currentPlayer = players.get(0);
		}else {
			for(Player player : players) {
				if(currentPlayer.equals(player) &&  !commitState.get(player)) {
					int indexToAdd = players.indexOf(player) + 1 == players.size() ? 0 : 1;

					currentPlayer = players.get(indexToAdd == 0 ? 0 :players.indexOf(player)+indexToAdd);
					return true;
				}
			}
		}
		return false;

	}

	//--------------------------------------------------------------------------------------------------

	/**
	 * method to print cards list for current player
	 */
	public void printCardsListForCurrentPlayer() {
		int card_num = 1;
		System.out.println("CARDS CURRENTLY OWNED BY: " + currentPlayer.getPlayerName());
		System.out.println("*****************************************************");

		for(Card card : currentPlayer.getCardList()) {
			System.out.println(card_num + ". " + card.getCardType().name());
			++card_num;
		}
		System.out.println("*****************************************************");
	}

	/**
	 * method to add player card
	 */
	public void addPlayerCard() {
		for(Player player : players) {
			if(player.getCanAddCard()) {
				player.addCard();
			}
		}
	}

	public int getNumberOfTries() {
		return numberOfTries;
	}

	public void setNumberOfTries(int numberOfTries) {
		this.numberOfTries = numberOfTries;
	}

	public void incrementNumberOfTries(){
		++numberOfTries;
	}

	/**
	 * method to reset peace for all players
	 */
	public void resetPeaceForAllPlayers() {
		for(Player player : players) {
			player.resetPeaceWith();
		}
	}

	/**
	 * method to get queue of players
	 * @return player player in queue
	 */
	public Queue<Player> getPlayerQueue() {
		return playerQ;
	}

	/**
	 * method to add player in queue
	 * @param player player
	 */
	public void addPlayerQueue(Player player) {
		playerQ.add(player);
	}

	/**
	 * method to get next player
	 * @return player
	 */
	public Player getNextPlayer() {
		Player next = null;
		Player put_back = null;
		boolean found = false;
		while(!found) {
			next =  playerQ.poll();
			put_back = next;
			if(next.getCommit()) {
				playerQ.add(next);
			}
			else {
				found = true;
			}
		}
		currentPlayer = next;
		playerQ.add(put_back);
		return next;
	}

	public Map<Player, Boolean> getCommitState() {
		return commitState;
	}

	public void setCommitState(Map<Player, Boolean> commitState) {
		this.commitState = commitState;
	}

	/**
	 * method to reset commit state of players
	 */
	public void resetCommit() {
		for(Player player : players) {
			player.resetCommit();
		}
	}

	/**
	 * method to check all commit state players
	 * @return allcommit returning commit state
	 */
	public boolean checkAllCommit() {
		boolean allCommit = true;
		for(Player player : players) {
			if(player.getCommit() == false) {
				allCommit = false;
			}
		}
		return allCommit;
	}



	public void editStrategy(MainPlayPhaseController p_mainPlayPhaseController) {
		scanner = new Scanner(System.in);

		boolean found = false;


		System.out.println("DO YOU WANT TO EDIT A PLAYER STRATEGY? Press 1 for yes or 0 for no or 2 to permanently set the strategies: ");
		int userInput = scanner.nextInt();

		if(userInput == 1) {

			do {
				printPlayerStrategies();
				System.out.print("Enter the name of the player you want to edit or enter exit: ");
				String input = scanner.next();

				if(input.equalsIgnoreCase("exit")) {
					
					break;
				}
				else {
					for(Player player : players) {
						if(player.getPlayerName().equalsIgnoreCase(input)) {
							found = changeStrat(player, p_mainPlayPhaseController);
							break;
						}
					}
				}

				if(!found) {
					System.out.println("Player Not Found - Please Enter Correct Name");
				}

			}
			while(true);

		}
		else if(userInput == 2) {
			permanentStrategy = true;
		}


	}

	public Queue<Player> getPlayerQ() {
		return playerQ;
	}

	public void setPlayerQ(Queue<Player> playerQ) {
		this.playerQ = playerQ;
	}

	public boolean isStrategyPermanent() {
		return permanentStrategy;
	}

	public boolean changeStrat(Player player, MainPlayPhaseController p_mainPlayPhaseController) {


		while(true)
		{
			System.out.println("Choose which strategy to set for the following player: " + player.getPlayerName());

			System.out.println("------------------------------------------------------------------------");

			System.out.println("1. Human Strategy");
			System.out.println("2. Aggressive Strategy");
			System.out.println("3. Benevolent Strategy");
			System.out.println("4. Random Strategy");
			System.out.println("5. Cheater Strategy");
			System.out.println("6. Exit");

			System.out.print("Enter a number between 1 to 6: ");
			int value = scanner.nextInt();

			switch(value) {

				case 1:
					player.setStrategy(new HumanStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance()));
					return true;

				case 2:
					player.setStrategy(new AggressiveStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance()));
					return true;

				case 3:
					player.setStrategy(new BenevolentStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance()));
					return true;

				case 4:
					player.setStrategy(new RandomStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance()));
					return true;

				case 5:
					player.setStrategy(new CheaterStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance()));
					return true;

				case 6:
					return true;

				default:
					System.out.println("INVALID INPUT");

			}
		}



	}

	public void printPlayerStrategies() {
		String l_Columns = "%-21s   %-22s%n";

		System.out.println("CURRENT PLAYER STRATEGIES");
		System.out.println("*************************");

		for(Player player : players) {
			System.out.format(l_Columns, player.getPlayerName(), player.getStrategy().getStrategyName());
		}
	}

	//--------------------------------------------------------------------------------------------------

	public void GameModelBuilder(GameModel d_GameModel){
		this.setPlayers(d_GameModel.getPlayers());
		this.setCurrentPlayer(d_GameModel.getCurrentPlayer());
		this.setCommitState(d_GameModel.getCommitState());
		this.setPlayerQ(d_GameModel.getPlayerQ());
		this.setWinner(d_GameModel.getWinner());
		System.out.println("Loaded Game Model successfully...");
	}

	public void clearGameModel() {
		if (GameModel.getInstance().getPlayers() !=null){
			GameModel.getInstance().getPlayers().clear();
		}
		if (GameModel.getInstance().getPlayerQueue() != null) {
			GameModel.getInstance().getPlayerQueue().clear();
		}
		if (GameModel.getInstance().getCommitState() != null){
			GameModel.getInstance().getCommitState().clear();
		}

	}































}
