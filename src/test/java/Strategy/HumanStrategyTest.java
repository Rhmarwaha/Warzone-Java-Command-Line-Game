package Strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import business.MainPlayPhaseBusinessCommands;
import model.MapModel;
import model.Player;

class HumanStrategyTest {
	
	PlayerStrategy  strategy;
	
	Player player;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		player = new Player("Raghav");
		
		strategy= new HumanStrategy(player, MapModel.getInstance(),null, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance());
		
		player.setStrategy(strategy);
		
		}
			
	@Test
	void test() {
		
		assertEquals(strategy instanceof HumanStrategy && strategy.getStrategyName().equals("HUMAN") && player.getStrategy() instanceof HumanStrategy,true);
		
	}

}
