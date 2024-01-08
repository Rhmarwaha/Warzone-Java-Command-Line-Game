package GamePhase;


/**
 * class containing method for clearing current phase of map
 * 
 * @author Rohit
 * @version build 2
 *
 */
public class MapPhaseState {

	/**
	 * Constructor
	 */
	private MapPhaseState(){}
	
	/**
	 * current phase of the map
	 */
	public static String D_CURRENT_MAP;
	
	
	/**
	 * Method to clear the map phase by setting it to null
	 */
	public static void clearMapPhaseStates() {
		D_CURRENT_MAP = null;
	}

}