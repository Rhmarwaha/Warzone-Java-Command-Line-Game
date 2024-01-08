package business.Order;

import model.Card;
import model.Country;
import model.Player;
import model.ResponseWrapper;

/**
 * Class that defines blockade functionalities
 * 
 * @author Kevin
 * @author ishaanbajaj
 * @version build 2
 */
public class BlockadeOrder implements Order{


	/**
	 * Object of class Country - the country to be targeted.
	 */
	private Country targetCountry;
	/**
	 * Object of class player - to get current turn player
	 */
	private Player player;
	
	boolean valid;
	
	/**
	 * Parameterized constructor that to build a blockade order
	 * @param p_player - player that wants to execute blockade order
	 * @param p_targetCountry - blockade will occur in this country
	 */
	public BlockadeOrder(Player p_player, Country p_targetCountry) {
		super();
		player = p_player;
		targetCountry = p_targetCountry;
		valid = false;
	}
	
	/**
	 * Triples the army in the target country and sets it to neutral
	 */
	@Override
	public void execute() {

		player.removeCountryHold(targetCountry);
		
		int doubleArmies = 3 * targetCountry.getArmies();
		
		targetCountry.setArmy(doubleArmies);
		
		targetCountry.setNeutral();
		
	}

	/**
	 * 1. check if player has a blockade card
	 * 2. check if the target country belongs to the player
	 * 3. If the above are true, remove blockade card from the player and return true
	 */
	@Override
	public boolean valid() {

		boolean hasCard = false;
		
		for(Card card : player.getCardList()) {
			if(card.getCardType() == Card.CardType.BLOCKADE) {
				hasCard = true;
			}
		}
		if(!hasCard) {
			return false;
		}
		
		if(targetCountry == null) {
			return false;
		}
		else if(player.getCountriesHold().contains(targetCountry)) {
			
			for(Card card : player.getCardList()) {
				if(card.getCardType() == Card.CardType.BLOCKADE) {
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
	 * Print execution of blockade order
	 */
	@Override
	public void printOrder() {
		System.out.println("*****************************************************");
		System.out.println("Blockade Order executed by: " + player.getPlayerName());
		System.out.println("Blockade Order executed on: " + targetCountry.getCountryId());
		System.out.println("*****************************************************");
	}

	@Override
	public ResponseWrapper getOrderStatus() {

		if(valid) {
			return new ResponseWrapper(200, " Blockade order added in queue");
		}
		else {
			return new ResponseWrapper(204, "Can only perform blockade on your own country\n");
		}
	}

}