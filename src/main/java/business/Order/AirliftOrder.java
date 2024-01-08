package business.Order;

import model.Player;
import model.ResponseWrapper;
import model.Card;
import model.Card.CardType;
import model.Country;

/**
 * 
 * Airlift that allows a single transfer of armies from any of your territories.
 * Must wait next turn to attack.
 * Transfer occurs before attack takes place.
 * 
 * @author Kevin
 * @author ishaanbajaj
 * @version build 2
 */
public class AirliftOrder implements Order{

	/**
	 * Object of class - the country from where the armies are being airlifted
	 */
	private Country originCountry;
	/**
	 * Object of class - the country from where the armies are being airlifted
	 */
	private Country destinationCountry;
	/**
	 * number of armies to airlift
	 */
	private int armies_to_move;
	/**
	 * Object of player class to get current player
	 */
	private Player player;
	
	private boolean valid;
	
	/**
	 * Parameterized constructor to build an airlift order
	 * @param player - the one that wants to execute an airlift order
	 * @param countryFrom - source country
	 * @param countryTo - destination country
	 * @param armies_to_move - amount of armies to airlift
	 */
	public AirliftOrder(Player player, Country countryFrom, Country countryTo, int armies_to_move) {
		this.player = player;
		originCountry = countryFrom;
		destinationCountry = countryTo;
		this.armies_to_move = armies_to_move;
		valid = false;
	}
	
	/**
	 * Execute airlift by moving the armies from source country to target country
	 */
	@Override
	public void execute() {
		
		int originCountryArmyBefore = originCountry.getArmies();
		int originCountryArmyAfter = originCountryArmyBefore - armies_to_move;
		originCountry.setArmy(originCountryArmyAfter);
		
		int destinationCountryArmyBefore = destinationCountry.getArmies();
		int destinationCountryArmyAfter = destinationCountryArmyBefore + armies_to_move;
		destinationCountry.setArmy(destinationCountryArmyAfter);
		
		
	}
	
	/**
	 * 1. Player requires airlift card in order for execution to be valid
	 * 2. source and destination countries must belong to the player and source and destination cannot be the same
	 * 3. number of armies to move must be valid
	 */
	@Override
	public boolean valid() {
		
		boolean hasCard = false;
		
		for(Card card : player.getCardList()) {
			if(card.getCardType() == CardType.AIRLIFT) {
				hasCard = true;
			}
		}
		if(!hasCard) {
			return false;
		}
		
		if(originCountry == null || destinationCountry == null
		|| !player.getCountriesHold().contains(originCountry) 
		|| !player.getCountriesHold().contains(destinationCountry)
		|| originCountry == destinationCountry) {
			return false;
		}
		else 
		if(armies_to_move < originCountry.getArmies() && armies_to_move > 0) {
			
			for(Card card : player.getCardList()) {
				if(card.getCardType() == CardType.AIRLIFT) {
					player.getCardList().remove(card);
					break;
				}
			}
			valid = true;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Print execution of airlift order
	 */
	@Override
	public void printOrder() {
		System.out.println("*****************************************************");
		System.out.println("Airlift Order executed by: " + player.getPlayerName());
		System.out.println("Airlifting: " + armies_to_move + " armies from: " + originCountry.getCountryId() + " to: " + destinationCountry.getCountryId());;
		System.out.println("*****************************************************");
		
	}

	@Override
	public ResponseWrapper getOrderStatus() {
		
		if(valid) {
			return new ResponseWrapper(200, " Airlift order added in queue");
		}
		else {
			return new ResponseWrapper(204, "One of the following occurred: \n"
					+ "1. You do not possess an airlift card\n"
					+ "2. Country you are airlifting from does not belongs to you\n"
					+ "3. Country you are airlifting to does not belongs to you\n"
					+ "4. That country does not exist in the map\n");
		}
	}

}