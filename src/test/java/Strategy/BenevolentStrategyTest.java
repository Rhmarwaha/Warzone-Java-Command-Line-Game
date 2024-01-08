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


class BenevolentStrategyTest {

	private Player d_Player;
    private MapModel d_MapModel;
    private BenevolentStrategy d_BenevolentStrategy;

    private Continent d_America;
    private Country d_Canada, d_USA, d_Mexico;
    
    @BeforeEach
    void setUp() {
        d_Player = new Player("Kevin");
        d_MapModel = new MapModel();
        d_BenevolentStrategy = new BenevolentStrategy(d_Player, d_MapModel, null, null);

        d_America = new Continent("North America");
        d_Canada = new Country("Canada", d_America);
        d_USA = new Country("USA", d_America);
        d_Mexico = new Country("Mexico", d_America);

        d_MapModel.addContinent(d_America);
        d_MapModel.addContinentCountries(d_America, d_Canada);
        d_MapModel.addContinentCountries(d_America, d_USA);
        d_MapModel.addContinentCountries(d_America, d_Mexico);
    }
    
    
    @Test
    void testToDefend() {
        d_Player.addCountryHold(d_Canada);
        d_Player.addCountryHold(d_USA);
        d_Player.addCountryHold(d_Mexico);
        
        d_Canada.setArmy(10);
        d_USA.setArmy(20);
        d_Mexico.setArmy(5);
        Country expectedCountry = d_Mexico;
        Country actualCountry = d_BenevolentStrategy.toDefend();
        assertEquals(expectedCountry, actualCountry);
    }
    
    @Test
    void testToMoveFrom() {
        d_Player.addCountry(d_Canada);
        d_Player.addCountry(d_USA);
        d_Player.addCountry(d_Mexico);
        
        d_Canada.setArmy(10);
        d_USA.setArmy(20); // Strongest country
        d_Mexico.setArmy(5);

        Country expectedCountry = d_USA;
        Country actualCountry = d_BenevolentStrategy.toMoveFrom();
        assertEquals(expectedCountry, actualCountry);
    }
    
    @Test
    void testToMoveTo() {
        
        d_Canada.setArmy(10);
        d_USA.setArmy(5);      
        d_Mexico.setArmy(15); 

        
        d_Player.addCountry(d_Canada);
        d_Player.addCountry(d_Mexico);
        d_Mexico.getNeighbors().add(d_USA); 
        d_USA.getNeighbors().add(d_Mexico); 

        
        d_BenevolentStrategy.toMoveFrom();
       
        Country actualCountry = d_BenevolentStrategy.toMoveTo();
        
       
        assertEquals(d_USA, actualCountry);
    }
    
    
    @Test
    void testgetStrongest() {
    	 d_Canada.setArmy(10);
         d_USA.setArmy(5);      
         d_Mexico.setArmy(15);
         
         d_Player.addCountry(d_Canada);
         d_Player.addCountry(d_USA);
         d_Player.addCountry(d_Mexico);
         
         Country strongest = d_BenevolentStrategy.getStrongest();
         
         assertEquals(strongest, d_Mexico);
         
    }
    
    @Test
    void testgetWeakest() {
    	 d_Canada.setArmy(10);
         d_USA.setArmy(5);      
         d_Mexico.setArmy(15);
         
         d_Player.addCountry(d_Canada);
         d_Player.addCountry(d_USA);
         d_Player.addCountry(d_Mexico);
         
         Country weakest = d_BenevolentStrategy.getWeakest();
         
         assertEquals(weakest, d_USA);
         
    }

}
