package Strategy;

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

import java.io.Serializable;

public class BenevolentStrategy extends PlayerStrategy implements Serializable {

	private final String strategyName = "BENEVOLENT";
	static int phase = 1;

	private Country strongest;
	private Country weakest;

	@Override
	public String getStrategyName() {

		return strategyName;
	}

	public BenevolentStrategy() {
	}

	public BenevolentStrategy(Player p_player, MapModel p_mapModel, MainPlayPhaseController p_mainPlayPhaseController, MainPlayPhaseBusinessCommands p_mainPlayPhaseBusinessCommands) {
		super(p_player, p_mapModel, p_mainPlayPhaseController, p_mainPlayPhaseBusinessCommands);
	}

	@Override
	public ResponseWrapper createOrder() throws GeneralException{

		ResponseWrapper response;
		
		if(phase == 1) {
			
			Country toDeploy = toDefend();
			
			response = d_mainPlayPhaseBusinessCommands.deploy(d_player, toDeploy.getCountryId(), d_player.getArmiesToIssue());
		
				++phase;
				return response;
			
		}
		else if(phase == 2) {

			strongest = getStrongest();
			weakest = getWeakest();

			response = d_mainPlayPhaseBusinessCommands.advance(d_player, strongest.getCountryId(), weakest.getCountryId(), strongest.getArmies()/2);

			
				phase = 1;
				d_player.performCommit();
				return response;
			
		}



		return null;
	}

	@Override
	protected Country toAttack() {
		return null;
	}

	@Override
	protected Country toAttackFrom() {
		return null;
	}

	@Override
	protected Country toMoveFrom() {

		strongest = getStrongest();
		weakest = strongest;

		return strongest;
	}

	@Override
	protected Country toMoveTo() {

		for(Country country : strongest.getNeighbors()) {
			if(country.getArmies() < weakest.getArmies()) {
				weakest = country;
			}
		}

		if(weakest == strongest) {
			return null;
		}
		else return weakest;
	}

	@Override
	protected Country toDefend() {

		weakest = getWeakest();

		return weakest;
	}

	public Country getWeakest() {

		if(!d_player.getCountriesHold().isEmpty()) {
			weakest = d_player.getCountriesHold().get(0);

			for(Country country : d_player.getCountriesHold()) {
				if(weakest.getArmies() > country.getArmies()) {
					weakest = country;
				}
			}
		}


		return weakest;

	}

	public Country getStrongest() {

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


























