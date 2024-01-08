package Strategy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Player;
import model.Continent;
import model.Country;
import model.MapModel;
import controller.MainPlayPhaseController;
import business.MainPlayPhaseBusinessCommands;
import logger.GeneralException;
import model.ResponseWrapper;

class AggressiveStrategyTest {
	
	private Player d_Player;
    private MainPlayPhaseController d_MainPlayPhaseController;
    private MainPlayPhaseBusinessCommands d_MainPlayPhaseBusinessCommands;
    private AggressiveStrategy d_AggressiveStrategy;
    
    private MapModel d_MapModel = new MapModel();
	private Continent d_America;
	private Country d_Canada, d_USA, d_Mexico;

    @BeforeEach
    void setUp() {
        d_Player = new Player("Kevin");
        d_MapModel = new MapModel();
        d_AggressiveStrategy = new AggressiveStrategy(d_Player, d_MapModel, null, null);

        d_America = new Continent("North America");
        d_Canada = new Country("Canada", d_America);
        d_USA = new Country("USA", d_America);
        d_Mexico = new Country("Mexico", d_America);

        d_MapModel.addContinent(d_America);
        d_MapModel.addContinentCountries(d_America, d_Canada);
        d_MapModel.addContinentCountries(d_America, d_USA);
        d_MapModel.addContinentCountries(d_America, d_Mexico);

        d_MapModel.addBorders(d_Canada, d_USA);
        d_MapModel.addBorders(d_USA, d_Mexico);
        d_MapModel.addBorders(d_USA, d_Canada);
        d_MapModel.addBorders(d_Mexico, d_Mexico);
    }

    @Test
    void testGetStrategyName() {
        String l_StrategyName = d_AggressiveStrategy.getStrategyName();
        assertEquals("AGGRESSIVE", l_StrategyName);
    }
    
    @Test
    void testToDefend() {
        d_Player.addCountry(d_Canada);
        d_Player.addCountry(d_USA);
        d_Player.addCountry(d_Mexico);

        d_Canada.setArmy(5);
        d_USA.setArmy(10);
        d_Mexico.setArmy(15);

        Country l_Result = d_AggressiveStrategy.toDefend();
        assertEquals(d_Mexico, l_Result);
    }
    
    @Test
    void testToAttackFrom() {
        d_Player.addCountry(d_Mexico);
        d_Mexico.setArmy(20);
        d_AggressiveStrategy.toDefend();

        Country l_Result = d_AggressiveStrategy.toAttackFrom();
        assertEquals(d_Mexico, l_Result);
    }
    
    
    @Test
    void testToAttack() {
    	d_Player.addCountry(d_USA);
    	
    	 d_Canada.setArmy(5);
         d_USA.setArmy(10);
         d_Mexico.setArmy(15);
         
         d_AggressiveStrategy.toDefend();
         
         Country l_Result = d_AggressiveStrategy.toAttack();
         assertEquals(d_Canada, l_Result);
         
    }
    
    
}

