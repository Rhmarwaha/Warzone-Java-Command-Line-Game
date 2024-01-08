/**
 * The `WarzoneEngineController` class is responsible for handling user input related to game features in the Warzone game.
 * It allows users to select various game features and returns the selected feature as an integer.
 * 
 * <p>
 * This class is used to gather user input for selecting game features.
 * </p>
 * 
 * @version 1.0
 */
package controller;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The `WarzoneEngineController` class is responsible for handling user input related to game features in the Warzone game.
 * It allows users to select various game features and returns the selected feature as an string.
 * 
 * <p>
 * This class is used to gather user input for selecting game features.
 * </p>
 * 
 * @version 1.0
 */
public class WarzoneEngineController {

	static Scanner d_inputForFeatureSelection;

	/**
	 * Gets the user's selected game feature as an integer.
	 * 
	 * @return The selected game feature as an string. Returns 404 if the input is invalid.
	 */
	public String getGameFeatureInputs() {
		String l_selectedFeature = "";
		try {

			d_inputForFeatureSelection = new Scanner(System.in);
			l_selectedFeature = d_inputForFeatureSelection.nextLine();
			
		
		}catch(InputMismatchException p_inputMismatchException){
			// throws global exception
			System.out.println("Please provide proper input");
			return "404";
		}

		return l_selectedFeature;

	}
	
	
}	
