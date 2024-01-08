package Strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class RandomStrategy extends PlayerStrategy implements Serializable {

	private final String strategyName = "RANDOM";
	static int phase = 1;
	
	private int armiesIssuedPhase1;

	Random random;
	Country source_attack = null;
	Country destination_attack = null;

	Country source_move = null;
	Country destination_move = null;
	
	Country actionCountry = null;

	@Override
	public String getStrategyName() {
		return strategyName;
	}

	public RandomStrategy() {
	}

	public RandomStrategy(Player p_player, MapModel p_mapModel, MainPlayPhaseController p_mainPlayPhaseController, MainPlayPhaseBusinessCommands p_mainPlayPhaseBusinessCommands) {
		super(p_player, p_mapModel, p_mainPlayPhaseController, p_mainPlayPhaseBusinessCommands);
		random = new Random();
	}

	@Override
	public ResponseWrapper createOrder() throws GeneralException{
		
		if(phase == 1) {

			Country toDeploy = toDefend();

			armiesIssuedPhase1 = d_player.getArmiesToIssue();
			ResponseWrapper response = d_mainPlayPhaseBusinessCommands.deploy(d_player, toDeploy.getCountryId(), d_player.getArmiesToIssue());
			
			
				++phase;
				return response;
			
		}
		else if(phase == 2) {

				destination_attack = toAttack();
				source_attack = toAttackFrom();

			ResponseWrapper response = null;

			if(destination_attack != null && source_attack != null) {
				response = d_mainPlayPhaseBusinessCommands.advance(d_player, source_attack.getCountryId(), destination_attack.getCountryId(), source_attack.getArmies() - 1);
				--phase;
				d_player.performCommit();
				return response;
			}
			else {
				
				source_move = toMoveFrom();
				destination_move = toMoveTo();
				
				response = d_mainPlayPhaseBusinessCommands.advance(d_player, source_move.getCountryId(), destination_move.getCountryId(), source_move.getArmies() - 1);
 
				--phase;
				d_player.performCommit();
				return response;
				
			}
				
			
		}


		return null;
	}

	@Override
	protected Country toAttack() {

		List<Country> border_countries = new ArrayList<>();
		
		for(Country country : actionCountry.getNeighbors()) {
			border_countries.add(country);
		}

		List<Country> border_countriesPlayerNotOwner = new ArrayList<>();
		//Remove all countries that belong to player
		for(Country country : border_countries) {
			if(country.getCountryOwner() != d_player) {
				border_countriesPlayerNotOwner.add(country);
			}
		}

		return getRandomCountry(border_countries);
	}

	@Override
	protected Country toAttackFrom() {

//		List<Country> potential_source_countries = new ArrayList<>();
//
//		//find all neighboring countries of the target country which belong to the player
//		for(Country country : d_player.getCountriesHold()) {
//			if(country.getNeighbors().contains(destination_attack)) {
//				potential_source_countries.add(country);
//			}
//		}

		return actionCountry;
	}

	@Override
	protected Country toMoveFrom() {

		source_move = toDefend();

		return source_move;
	}

	@Override
	protected Country toMoveTo() {

		List<Country> potential_destination_countries = new ArrayList<>();

		for(Country country : source_move.getNeighbors()) {
			if(d_player.getCountriesHold().contains(country)) {
				potential_destination_countries.add(country);
			}
		}

		return getRandomCountry(potential_destination_countries);
	}

	@Override
	protected Country toDefend() {

		int num = random.nextInt(d_player.getCountriesHold().size());

		actionCountry = d_player.getCountriesHold().get(num);
		
		return actionCountry;
	}

	private Country getRandomCountry(List<Country> countries) {

		if(countries.size() > 1) {
			int num = random.nextInt(countries.size());
			return countries.get(num);
		}
		else if(countries.size() == 1) {
			return countries.get(0);
		}

		return null;
	}





}
