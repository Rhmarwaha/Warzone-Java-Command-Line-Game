package Strategy;

import business.MainPlayPhaseBusinessCommands;
import business.Order.Order;
import controller.MainPlayPhaseController;
import model.Country;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;

import java.io.Serializable;

public class CheaterStrategy extends PlayerStrategy implements Serializable {

	private final String strategyName = "CHEATER";

	@Override
	public String getStrategyName() {
		return strategyName;
	}

	public CheaterStrategy() {
	}

	public CheaterStrategy(Player p_player, MapModel p_mapModel, MainPlayPhaseController p_mainPlayPhaseController, MainPlayPhaseBusinessCommands p_mainPlayPhaseBusinessCommands) {
		super(p_player, p_mapModel, p_mainPlayPhaseController, p_mainPlayPhaseBusinessCommands);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResponseWrapper createOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Country toAttack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Country toAttackFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Country toMoveFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Country toMoveTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Country toDefend() {
		// TODO Auto-generated method stub
		return null;
	}

}
