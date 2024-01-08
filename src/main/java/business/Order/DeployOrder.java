package business.Order;

import model.Country;
import model.Player;
import model.ResponseWrapper;

/**
 * Class that defines deployment functionalities
 * 
 * @author Kevin
 * @author ishaanbajaj
 * @version build 2
 */
public class DeployOrder implements Order{

	/**
	 * object of class country - to get the target country
	 */
	private Country targetCountry;
	/**
	 * number of armies to deploy
	 */
	private int to_deploy_armies;
	/**
	 * Object of player class
	 */
	private Player player;
	
	boolean valid;

	/**
	 * parameterized constructor that to build a deployment order
	 * @param targetCountry - country will receive armies
	 * @param to_deploy_armies - nummber of army units for deployment
	 * @param player - player that wants to execute a deployment order
	 */
	public DeployOrder(Country targetCountry, int to_deploy_armies, Player player) {
		super();
		this.targetCountry = targetCountry;
		this.to_deploy_armies = to_deploy_armies;
		this.player = player;
		valid = false;
	}
	
	/**
	 * Adds army to the target country
	 */
	@Override
	public void execute() {
		targetCountry.armiesDeploy(to_deploy_armies);
	}

	/**
	 * 1. Check if target country exists in the map and the amount deployed is valid
	 * 2. Check if target country belongs to the player
	 */
	@Override
	public boolean valid() {
		
		if(targetCountry == null || to_deploy_armies > player.getArmiesToIssue() || to_deploy_armies <= 0 ) {
			return false;
		}
		else if(player.getCountriesHold().contains(targetCountry)) {
			valid = true;
			return true;
		}
		
		return false;
		
	}

	/**
	 * Print execution of deployment order
	 */
	@Override
	public void printOrder() {
		System.out.println("*****************************************************");
		System.out.println(player.getPlayerName() + " deployed " + to_deploy_armies + " armies at " + targetCountry.getCountryId());
		System.out.println("*****************************************************");
	}

	@Override
	public ResponseWrapper getOrderStatus() {
		if(valid && player.getArmiesToIssue()>0) {
			return new ResponseWrapper(200, " Deploy order to: " + targetCountry.getCountryId() + " added in queue");
		} else if (player.getArmiesToIssue()<=0){
			return new ResponseWrapper(200, "No armies left in this turn.");
		}else {
			return new ResponseWrapper(204,"Targeted country doesn't belong to you.");
		}
	}

}