package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {

	GameModel GM = GameModel.getInstance(); 
	
	private Player P1, P2;
	
	@BeforeEach
	void setUp() throws Exception {
		P1 = new Player("Kevin");
		P2 = new Player("Rohit");
		
		GM.addPlayerInPlayersList(P1);
		GM.addPlayerInPlayersList(P2);
		
		GM.addPlayerQueue(P1);
		GM.addPlayerQueue(P2);
	}

	@Test
	void getNextPlayer() {
		
		Player P = GM.getNextPlayer();
		P = GM.getNextPlayer();
		
		assertEquals(P2, P);
		
	}
	
	/**
	 * Method to assert that the Singleton instance is lazily initialized and not created until the first call to getInstance.
	 */
	@Test
	void testSingletonGameMode() {
		GameModel l_instance1 = GameModel.getInstance();
		l_instance1.addPlayerInPlayersList(P1);
		GameModel l_instance2 = GameModel.getInstance();
		l_instance2.addPlayerInPlayersList(P2);
        assertSame(l_instance1, l_instance2);
	}


}
