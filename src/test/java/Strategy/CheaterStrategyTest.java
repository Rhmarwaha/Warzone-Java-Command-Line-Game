package Strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Player;
import model.MapModel;
import model.Country;
import model.Continent;
import controller.MainPlayPhaseController;
import business.MainPlayPhaseBusinessCommands;
import model.ResponseWrapper;

class CheaterStrategyTest {

    private CheaterStrategy d_CheaterStrategy;
    private Player d_Player;
    private MapModel d_MapModel;
    private Continent d_America;
    private Country d_Canada, d_USA, d_Mexico;

    @BeforeEach
    void setUp() {
        d_Player = new Player("Kevin");
        d_MapModel = new MapModel();
        d_CheaterStrategy = new CheaterStrategy(d_Player, d_MapModel, null, null);
        
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
        
        d_Canada.setArmy(5);
        d_USA.setArmy(10);
        d_Mexico.setArmy(15);
        
    }

    @Test
    void testGetStrategyName() {
        assertEquals("CHEATER", d_CheaterStrategy.getStrategyName());
    }
    
    
    @Test
    void testCheat() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	
    	Player temp = new Player("temp");
    	
    	temp.addCountry(d_Canada);
    	temp.addCountry(d_Mexico);
    	
    	d_Player.addCountry(d_USA);
    	
    	Class<?> clazz = Player.class;
    	
    	Method privateMethod = clazz.getDeclaredMethod("cheat");
    	
    	privateMethod.setAccessible(true);
    	
    	privateMethod.invoke(d_Player);
    	
    	assertEquals(3, d_Player.getCountriesHold().size());
    	
    }
    
}
