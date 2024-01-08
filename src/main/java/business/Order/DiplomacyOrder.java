package business.Order;

import model.Card;
import model.Player;
import model.ResponseWrapper;

/**
 * Class that defines diplomacy functionalities
 * 
 * @author spankeydamankey
 * @version build 2
 */
public class DiplomacyOrder implements Order{

	/**
	 * Object of player class - to get current turn player
	 */
	Player currentPlayer;
	/**
	 * Object of player class - to get the other player to get peace with.
	 */
	Player peaceWith;
	
	Boolean valid;
	
	/**
	 * parameterized constructor that to build a diplomacy order
	 * @param currentPlayer - player that will execute diplomacy
	 * @param peaceWith - other player that will make peace with
	 */
	public DiplomacyOrder(Player currentPlayer, Player peaceWith) {
		this.currentPlayer = currentPlayer;
		this.peaceWith = peaceWith;
		valid = false;
	}

	/**
	 * set peace between both players
	 */
	@Override
	public void execute() {
		currentPlayer.setPeaceWith(peaceWith);
		peaceWith.setPeaceWith(currentPlayer);
	}

	/**
	 * 1. check if player that initiates diplomacy has a diplomacy card
	 * 2. check if player that receives diplomacy order is valid
	 * 3. If above are true, remove diplomacy card from the player
	 */
	@Override
	public boolean valid() {
		
		boolean hasCard = false;
		
		for(Card card : currentPlayer.getCardList()) {
			if(card.getCardType() == Card.CardType.DIPLOMACY) {
				hasCard = true;
			}
		}
		if(!hasCard) {

			return false;
		}
		
		if(peaceWith != null) {
			
			for(Card card : currentPlayer.getCardList()) {
				if(card.getCardType() == Card.CardType.DIPLOMACY) {
					currentPlayer.getCardList().remove(card);
					break;
				}
			}
			valid = true;
			return true;
		}
		
		return false;
	}

	/**
	 * Print execution of diplomacy order
	 */
	@Override
	public void printOrder() {
		System.out.println("*****************************************************");
		System.out.println("Diplomacy Order executed by: " + currentPlayer.getPlayerName());
		System.out.println("Diplomacy Order executed with: " + peaceWith.getPlayerName());
		System.out.println("*****************************************************");
	}

	@Override
	public ResponseWrapper getOrderStatus() {
		if(valid) {
			return new ResponseWrapper(200, " Diplomacy order added in queue");
		}else {
			return new ResponseWrapper(204, " Player you want to make peace with does not exist");
		}
	}

}
