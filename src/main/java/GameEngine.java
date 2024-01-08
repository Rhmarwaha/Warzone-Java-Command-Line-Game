/**
 * GameEngine class to start the "WarZone Engine" which contains the implementation of GamePhases.
 *
 * @author Rohit
 * @author Ishaan Bajaj
 * @version build 1
 */
public class GameEngine {

	/**
	 * Object of WarZone Engine with initialisation
	 */
	private static WarzoneEngine d_warzoneEngine = new WarzoneEngine();

	/**
	 * Main method to start the application/program.
	 * @param args arguments
	 * @throws Exception if any error occurs
	 */
	public static void main(String[] args) throws Exception{
		d_warzoneEngine.gameStarts();
	}
	
}
