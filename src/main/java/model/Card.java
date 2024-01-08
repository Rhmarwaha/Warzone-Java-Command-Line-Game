package model;
import java.util.*;

/**
 * Card Class
 * @author Kevin
 * @author ishaanbajaj
 * @version build 2
 */
public class Card
{
	/**
	 * card enum to store default card types
	 */
	public enum CardType
	{
		/**
		 * bomb card
		 * blockade card
		 * airlift card
		 * diplomacy card
		 */
		BOMB(1), BLOCKADE(2), AIRLIFT(3), DIPLOMACY(4);

		/**
		 * integer to get card ID
		 */
		private final int cardID;

		/**
		 * cardtype
		 * @param i cardID
		 */
		CardType(int i)
		{
			cardID = i;
		}

		/**
		 * method to get card id
		 * @return integer id
		 */
		public int getID()
		{
			return cardID;
		}
	}

	/**
	 * card type
	 */
	private CardType cardType;
	/**
	 * random
	 */
	private static final Random random = new Random();

	/**
	 * constructor
	 * @param cardType cardtype
	 */
	public Card(CardType cardType)
	{
		this.cardType = cardType;
	}

	/**
	 * method to get card type
	 * @return cardType
	 */
	public CardType getCardType()
	{
		return cardType;
	}

	/**
	 * method to generate a random card
	 * @return card
	 */
	public static Card generateRandomCard()
	{
			
		int randomID = random.nextInt(4) + 1;
			
		CardType randomCard = null;
			
			for(CardType cardType : CardType.values())
			{
				if(cardType.getID() == randomID)
				{
					randomCard = cardType;
					break;
				}
			}
			
			if(randomCard == null)
			{
				throw new IllegalArgumentException("Trying to insert null");
			}
		//return new Card(CardType.AIRLIFT);

		return new Card(randomCard);
	}
}