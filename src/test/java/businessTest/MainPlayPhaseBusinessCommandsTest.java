package businessTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import business.MainPlayPhaseBusinessCommands;
import business.SingleGamePlayerCommands;
import logger.GeneralException;
import model.Continent;
import model.Country;
import model.GameModel;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;

class MainPlayPhaseBusinessCommandsTest {

	private MainPlayPhaseBusinessCommands mainPlayPhaseBusinessCommands = new MainPlayPhaseBusinessCommands();
	
	private ResponseWrapper finalResponse = new ResponseWrapper(201,"");
	
	private MapModel mapModel = MapModel.getInstance();
	private GameModel gameModel = GameModel.getInstance();
	
	private Player P1;
	
	private Continent d_America;
	
	private Country d_Canada, d_USA, d_Mexico;
	
	private SingleGamePlayerCommands singleGamePlayCommand=new SingleGamePlayerCommands();

	
	@BeforeEach
	void setUp(){
		
		gameModel.setPlayers(null);
		d_America = new Continent("North America", "10");
		
		d_Canada = new Country("Canada", d_America);
		d_USA = new Country("USA", d_America);
		d_Mexico = new Country("Mexico", d_America);
		
		P1 = new Player("Kevin");
		
		gameModel.addPlayerInPlayersList(P1);
		
		mapModel.addContinent(d_America);
		
		mapModel.addContinentCountries(d_America, d_Canada);
		mapModel.addContinentCountries(d_America, d_USA);
		mapModel.addContinentCountries(d_America, d_Mexico);
		
		mapModel.addBorders(d_Canada, d_USA);
		
		mapModel.addBorders(d_USA, d_Canada);
		mapModel.addBorders(d_USA, d_Mexico);
		
		mapModel.addBorders(d_Mexico, d_USA);
		
		P1.addCountry(d_Canada);
		P1.addCountry(d_USA);
		P1.addCountry(d_Mexico);
		
		P1.getCountry(d_Canada).setArmy(6);
		P1.getCountry(d_USA).setArmy(10);
		P1.getCountry(d_Mexico).setArmy(15);
	}

	@Test
	void doReinforcements() throws GeneralException {
		
		mainPlayPhaseBusinessCommands.doReinforcements();
		
		assertEquals(P1.getCurrentArmies(), 16);
		
	}
	
	@Test
	void endGame() throws GeneralException {
		mainPlayPhaseBusinessCommands.endGame();
		
		assertEquals(201, finalResponse.getStatusValue());
	}
	
	@Test
	void addPlayer() throws GeneralException {
		
		boolean inPlayerList = false;
		
		for(Player player : this.gameModel.getPlayers()) {
			if(player == P1) {
				inPlayerList = true;
			}
		}
		
		assertTrue(inPlayerList);
	}
	
	@Test
	void removePlayer() throws GeneralException {
		
		boolean playerRemoved = false;
		singleGamePlayCommand.removeplayerFromGame(P1.getPlayerName());
		if(this.gameModel.getPlayers()==null)
		{
			playerRemoved=true;
		}else if(!this.gameModel.getPlayers().contains(P1))
		{
			playerRemoved=true;
		}
		assertTrue(playerRemoved);
		
	}
}
