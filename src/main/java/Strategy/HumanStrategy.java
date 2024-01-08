package Strategy;

import business.MainPlayPhaseBusinessCommands;
import business.Order.Order;
import controller.MainPlayPhaseController;
import logger.GeneralException;
import model.Country;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;

import java.io.Serializable;

public class HumanStrategy extends PlayerStrategy implements Serializable {

	private final String strategyName = "HUMAN";

	@Override
	public String getStrategyName() {
		return strategyName;
	}

	public HumanStrategy() {
	}

	public HumanStrategy(Player p_player, MapModel p_mapModel, MainPlayPhaseController p_mainPlayPhaseController, MainPlayPhaseBusinessCommands p_mainPlayPhaseBusinessCommands) {
		super(p_player, p_mapModel, p_mainPlayPhaseController, p_mainPlayPhaseBusinessCommands);
	}

	@Override
	public ResponseWrapper createOrder() {

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
		return null;
	}

	@Override
	protected Country toMoveTo() {
		return null;
	}

	@Override
	protected Country toDefend() {
		return null;
	}
	
	public ResponseWrapper humanOrderCreation() {
		try {
			return d_mainPlayPhaseController.getMainPlaySetUpCommands(d_player, d_mainPlayPhaseController.getMainPlaySetUpCommandsFromUser());
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}


































