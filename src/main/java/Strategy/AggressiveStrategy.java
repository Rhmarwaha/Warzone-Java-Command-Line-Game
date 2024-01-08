package Strategy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import business.MainPlayPhaseBusinessCommands;
import business.Order.AdvanceOrder;
import business.Order.DeployOrder;
import business.Order.Order;
import controller.MainPlayPhaseController;
import logger.GeneralException;
import model.Country;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;

public class AggressiveStrategy extends PlayerStrategy implements Serializable {

	private final String strategyName = "AGGRESSIVE";
	int phase = 1;
	
	private Country strongest;
	
	private int armiesIssuedPhase1;
	//private MainPlayPhaseBusinessCommands d_mainPlayPhaseBusinessCommands;


	@Override
	public String getStrategyName() {
		return strategyName;
	}

	public AggressiveStrategy() {
	}

	public AggressiveStrategy(Player p_player, MapModel p_mapModel, MainPlayPhaseController p_mainPlayPhaseController, MainPlayPhaseBusinessCommands p_mainPlayPhaseBusinessCommands) {
		super(p_player, p_mapModel, p_mainPlayPhaseController, p_mainPlayPhaseBusinessCommands);
	}

	@Override
	public ResponseWrapper createOrder() throws GeneralException{

		if(phase == 1) {
			
			Country toDeploy = toDefend();
			
			armiesIssuedPhase1 = d_player.getArmiesToIssue();
			
			ResponseWrapper response = d_mainPlayPhaseBusinessCommands.deploy(d_player, toDeploy.getCountryId(), armiesIssuedPhase1);
			
				++phase;

				return response;
			
		}
		else if(phase == 2) {

			Country attack_source = toAttackFrom();
			Country attack_destination = toAttack();

			ResponseWrapper response = null;
			
			//System.out.println("TRYING TO ADVANCE: " + (d_player.getCurrentArmies() + armiesIssuedPhase1 - 1) + " FROM: " + attack_source.getCountryId() + " TO: " + attack_destination.getCountryId());

			if(attack_destination != null && attack_source != null ) {
					System.out.println("Building ATTACK ORDER");
					response = d_mainPlayPhaseBusinessCommands.advance(d_player, attack_source.getCountryId(), attack_destination.getCountryId(), attack_source.getArmies() - 1);

					--phase;
					d_player.performCommit();
					return response;
			}
			else {
				System.out.println("Building MOVE ORDER");
				Country move_source = toMoveFrom();
				Country move_destination = toMoveTo();


				if(move_source != null && move_destination != null) {
					
						response = d_mainPlayPhaseBusinessCommands.advance(d_player, move_source.getCountryId(), move_destination.getCountryId(), move_source.getArmies() - 1);
					}
				
				--phase;
				d_player.performCommit();
				return response;
				}
				
					
			

			}
		
		return null;

		}


	@Override
	protected Country toAttack() {

		
		Country attack = null;
		List<Country> neighbors = new ArrayList<>();

		//Get all neighbors for source country
		for(Country country : strongest.getNeighbors()) {
			neighbors.add(country);
		}
		Collections.shuffle(neighbors);

		List<Country> neighborsNotInPlayerList = new ArrayList<>();

		//Get all neighbors for source country that do not belong to the player
		for(Country country : neighbors)
		{
			if(!d_player.getCountriesHold().contains(country))
			{
				neighborsNotInPlayerList.add(country);
				
			}
		}

		Country minArmyCountry = null;
		//Find the country with lowest army
		if(!neighborsNotInPlayerList.isEmpty()) {
			minArmyCountry = neighborsNotInPlayerList.get(0);

			for(Country country : neighborsNotInPlayerList) {
				if(country.getArmies() < minArmyCountry.getArmies()) {
					minArmyCountry = country;
				}
			}
		}


		return minArmyCountry;
	}

	@Override
	protected Country toAttackFrom() {

		return strongest;
	}



	@Override
	protected Country toMoveFrom() {

		return strongest;
	}

	@Override
	protected Country toMoveTo() {

		Country theStrongest = strongest;
		Country destination = null;

        List<Country> neighbors = new ArrayList<>(strongest.getNeighbors());

		List<Country> neighborsOwned = new ArrayList<>();

		for(Country country : neighbors) {
			if(country.getCountryOwner() == d_player) {
				neighborsOwned.add(country);
			}
		}

		if(!neighborsOwned.isEmpty()) {
			Collections.shuffle(neighborsOwned);
			destination = neighborsOwned.get(0);
		}

		return destination;
	}




	@Override
	protected Country toDefend() {

		strongest = getStrongest();

		return strongest;
	}

	private Country getStrongest() {

		if(!d_player.getCountriesHold().isEmpty()) {
			strongest = d_player.getCountriesHold().get(0);

			for(Country country : d_player.getCountriesHold()) {
				if(strongest.getArmies() < country.getArmies()) {
					strongest = country;
				}
			}
		}




		return strongest;

	}






}
