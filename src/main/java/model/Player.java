package model;

import java.io.Serializable;
import java.util.*;

import Strategy.CheaterStrategy;
import Strategy.HumanStrategy;
import Strategy.PlayerStrategy;
import business.Order.Order;
import logger.GeneralException;

/**
 * Player class which holds the issue order and next order in the list of orders
 * @author Rohit, Kevins
 * @version build 2
 */
public class Player implements Serializable {

	/**
	 * string max cards limit
	 */
	private static final int MAX_CARD_LIMIT = 3;
	/**
	 * string player name
	 */
	private String playerName;
	//private int armiesHold = 5; // base armies
	/**
	 * integer base armies
	 */
	private int baseArmies = 5;

	public static Scanner scanner;
	/**
	 * integer armies to issue
	 */
	private int armiesToIssue;
	/**
	 * integer reInforcement armies
	 */
	private int reinforcementArmies = 0; // holding countries / 3 down to
	/**
	 * integer bonus armies
	 */
	private int bonusArmies = 0; // continent
	/**
	 * integer current armies assigned to player
	 */
	private int currentArmies = baseArmies + reinforcementArmies + bonusArmies;
	/**
	 * boolean if player can get card in this turn or not
	 */
	private boolean d_can_get_card_this_turn;
	/**
	 * boolean if player has committed
	 */
	private boolean commit;
	/**
	 * list of countries held by player
	 */
	private List<Country> countriesHold = new ArrayList<>();
	/**
	 * map of countries and current armies in the country
	 */
	private Map<Country,Integer> currentArmyInCountry;
	/**
	 * list of cards for player
	 */
	private List<Card> d_cards = new ArrayList<>();
	/**
	 * list/queue of orders for player
	 */
	private Deque<Order> orders_list = new ArrayDeque<>();
	/**
	 * list of card orders list
	 */
	//private List<Order> cardOrders_list;
	/**
	 * Object of Player class
	 */
	private Player peaceWith;

	//STRATEGY PATTERN
	//-------------------------------------------

	private PlayerStrategy strategy;


	public void setStrategy(PlayerStrategy p_strat) {
		strategy = p_strat;
	}

	public PlayerStrategy getStrategy() {
		return strategy;
	}


	public ResponseWrapper issueOrder() throws GeneralException{

		if(strategy instanceof CheaterStrategy) {
			cheat();
			this.performCommit();
			return new ResponseWrapper(200, " Cheating order added in queue");
		}
		else if(strategy instanceof HumanStrategy) {
			return ((HumanStrategy) strategy).humanOrderCreation();
		}
		else {
			ResponseWrapper order;

			order = strategy.createOrder();

			return order;
		}

	}

	private void cheat() {

		//Get all neighbors that don't belong to the player

		List<Country> c_list = new ArrayList<>();

		for(Country country : countriesHold) {
			for(Country neighbor : country.getNeighbors()) {
				if(neighbor.getCountryOwner() != this) {
					c_list.add(neighbor);
				}
			}
		}

		//Immediately capture those countries

		if(c_list.size() > 0) {

			for(Country country : c_list) {
				System.out.println("Cheating player: " + this.getPlayerName() + " HAS CAPTURED " + country.getCountryId());

				country.getCountryOwner().removeCountry(country);

				this.addCountry(country);

			}
		}

		//Double armies in those countries which has an enemy neighbor

		for(Country country : countriesHold) {
			for(Country neighbor : country.getNeighbors()) {
				if(neighbor.getCountryOwner() != this) {
					country.setArmy(country.getArmies()*2);
					break;
				}
			}
		}


	}


	//-------------------------------------------



	/**
	 * Default Constructor
	 * @param playerName player
	 */
	public Player(String playerName) {
		this.playerName = playerName;
		d_can_get_card_this_turn = false;
		commit = false;
	}

	public Player(PlayerStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * method to get armies to issue
	 * @return integer armies
	 */
	public int getArmiesToIssue() {
		return armiesToIssue;
	}

	/**
	 * method to set armies to issue
	 * @param armiesToIssue armies to issue
	 */
	public void setArmiesToIssue(int armiesToIssue) {
		this.armiesToIssue = armiesToIssue;
	}

//	public void resetArmiesToIssue() {
//		this.armiesToIssue = 5;
//	}

	/**
	 * method to get next order of player
	 * @return order
	 */
	public Order nextOrder() {
		return orders_list.poll();
	}

	/**
	 * method to get list of countries held by player
	 * @return returning countries hold
	 */
	public List<Country> getCountriesHold() {
		return countriesHold;
	}


	/**
	 * method get current armies in the country
	 * @return map
	 */
	public Map<Country, Integer> getCurrentArmyInCountry() {
		return currentArmyInCountry;
	}

	/**
	 * method to set current armies in the country
	 * @param currentArmyInCountry map
	 */
	public void setCurrentArmyInCountry(Map<Country, Integer> currentArmyInCountry) {
		this.currentArmyInCountry = currentArmyInCountry;
	}

	/**
	 * method to set countries held
	 * @param countriesHold list
	 */
	public void setCountriesHold(List<Country> countriesHold) {
		this.countriesHold = countriesHold;
	}

	/**
	 * method to add countries held by player
	 * @param country country
	 */
	public void addCountryHold(Country country) {
		if(this.countriesHold == null) {
			this.countriesHold = new ArrayList<>();
		}
		if(this.currentArmyInCountry == null) {
			this.currentArmyInCountry = new HashMap<>();
		}
		this.countriesHold.add(country);
		this.currentArmyInCountry.put(country, 0);
	}

	/**
	 * method to remove countries held by player
	 * @param country country
	 */
	public void removeCountryHold(Country country) {
		if(this.countriesHold == null) {
			this.countriesHold = new ArrayList<>();
		}
		if(this.currentArmyInCountry == null) {
			this.currentArmyInCountry = new HashMap<>();
		}
		this.countriesHold.remove(country);
		this.currentArmyInCountry.remove(country);
	}



	//-------------------------------------------

	/**
	 * method to add country under player
	 * @param country country
	 */
	public void addCountry(Country country) {
		if(this.countriesHold == null) {
			this.countriesHold = new ArrayList<>();
		}

		this.countriesHold.add(country);
		country.setCountryOwner(this);
		d_can_get_card_this_turn = true;
	}

	/**
	 * method to remove country
	 * @param country country
	 */
	public void removeCountry(Country country) {
		if(this.countriesHold == null) {
			this.countriesHold = new ArrayList<>();
		}

		this.countriesHold.remove(country);
	}

	/**
	 * method to get country
	 * @param p_country country
	 * @return country
	 */
	public Country getCountry(Country p_country) {
		for(Country country : countriesHold) {
			if(country == p_country) {
				return country;
			}
		}
		return null;
	}

	/**
	 * method to get player to peace with
	 * @return player
	 */
	public Player getPeaceWith() {
		return peaceWith;
	}

	/**
	 * method to set player to peace with
	 * @param player player
	 */
	public void setPeaceWith(Player player) {
		peaceWith = player;
	}

	/**
	 * method to reset peace with player
	 */
	public void resetPeaceWith() {
		peaceWith = null;
	}

	/**
	 * method to get list of cards for players
	 * @return list of cards
	 */
	public List<Card> getCardList() {
		return d_cards;
	}

	/**
	 * method to add card for player
	 */
	public void addCard()
	{
		Card newCard = Card.generateRandomCard();

		if(d_cards.size() < MAX_CARD_LIMIT)
		{
			d_cards.add(newCard);
		}
		else
		{
			cardLimitExceeded(newCard);
		}
	}

	/**
	 * method to add specific card
	 * @param card card
	 */
	public void addSpecificCard(Card card) {

		if(d_cards.size() < MAX_CARD_LIMIT) {
			d_cards.add(card);
		}
		else {
			cardLimitExceeded(card);
		}
	}

	/**
	 * method to check if card limits is exceeded or not.
	 * @param newCard card
	 */
	private void cardLimitExceeded(Card newCard) {
		System.out.println("Card limit that a player can possess has been exceeded, please choose the following options\n");

		System.out.println("The following cards are in your possession");
		System.out.println("------------------------------------------");

		for(int i = 0; i < d_cards.size(); ++i)
		{
			System.out.println(i+1 + ".	" + d_cards.get(i).getCardType().name());
		}

		System.out.println();
		System.out.println("NEW CARD");
		System.out.println("---------");

		System.out.println(newCard.getCardType().name()+"\n");


		int card_num;
		System.out.println("Please select which card you want to replace or PRESS 0 to keep current cards");

		while(true)
		{
			scanner = new Scanner(System.in);


			if(scanner.hasNextInt()) {
				card_num = scanner.nextInt();

				if(card_num >= 0 && card_num <= MAX_CARD_LIMIT) {
					break;
				}
				else
				{
					System.out.println("Invalid input");

					for(int i = 0; i < d_cards.size(); ++i)
					{
						System.out.println("Select " + (i+1) + " to replace:	" + d_cards.get(i).getCardType().name());
					}

				}
			}
			else
			{
				System.out.println("Invalid input");

				for(int i = 0; i < d_cards.size(); ++i)
				{
					System.out.println("Select " + (i+1) + " to replace:	" + d_cards.get(i).getCardType().name());
				}


			}
		}

		if(card_num != 0)
		{
			d_cards.set(card_num - 1, newCard);
		}
	}

	/**
	 * method to print list of cards for player
	 */
	public void printCardList() {
		int card_num = 1;
		System.out.println("CARDS CURRENTLY OWNED BY: " + playerName);
		System.out.println("***********************************************************");

		for(Card card : d_cards) {
			System.out.println(card_num + ". " + card.getCardType().name());
			++card_num;
		}
		System.out.println("***********************************************************\n\n");
	}

	/**
	 * method to check whether card can be added
	 * @return boolean
	 */
	public boolean getCanAddCard() {
		return d_can_get_card_this_turn;
	}

	/**
	 * method to end turn card reset
	 */
	public void endTurnCardReset()
	{
		d_can_get_card_this_turn = false;
	}

	/**
	 * method to calculate reinforcement armies
	 * @return integer armies
	 */
	private int CalculateReinforcementArmies() {
		reinforcementArmies = countriesHold.size()/3;
		return reinforcementArmies;
	}
	/**
	 * method to calculate bonus armies
	 * @return integer armies
	 */
	private int CalculateBonusArmies() {
		MapModel map = MapModel.getInstance();

		for(Continent continent : map.getContinents()) {
			if(continent.getContinentOwner() == this) {
				int bonus = Integer.parseInt(continent.getContientValue());
				bonusArmies += bonus;
			}
		}

		return bonusArmies;
	}
	/**
	 * method to calculate current armies
	 */
	public void calculateCurrentArmies() {
		bonusArmies = 0;
		currentArmies = baseArmies + CalculateReinforcementArmies() + CalculateBonusArmies();
		armiesToIssue = currentArmies;
	}

	/**
	 * method to get commit for player
	 * @return boolean
	 */
	public boolean getCommit() {
		return commit;
	}

	/**
	 * method to perform commit to true
	 */
	public void performCommit() {
		commit = true;
	}

	/**
	 * method to reset back the commit
	 */
	public void resetCommit() {
		commit  = false;
	}

	//-------------------------------------------


	/**
	 * method to get player name
	 * @return player name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * method to set player name
	 * @param playerName string
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * method to get current armies
	 * @return armies
	 */
	public int getCurrentArmies() {
		return currentArmies;
	}


	/**
	 * method to set current armies
	 * @param currentArmies armies
	 */
	public void setCurrentArmies(int currentArmies) {
		this.currentArmies = currentArmies;
	}

	/**
	 * method to get orders of player
	 * @return orders queue
	 */
	public Deque<Order> getOrders() {
		return orders_list;
	}

	/**
	 * method to set orders
	 * @param d_Orders orders
	 */
	public void setOrders(Deque<Order> d_Orders) {
		this.orders_list = d_Orders;
	}

	/**
	 * method to add orders of player
	 * @param order  order
	 */
	public void addOrder(Order order) {
		if(orders_list == null) {
			this.orders_list = new ArrayDeque<>();
		}
		this.orders_list.add(order);
	}

	public void clearCountriesHold(){
		this.countriesHold.clear();
	}



}
