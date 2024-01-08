package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logger.GeneralException;
import model.Player;
import model.ResponseWrapper;

class MainPlayPhaseControllerTest {
private MainPlayPhaseController mainPlayPhaseController;
	
	@BeforeEach
	void setUp() {
		mainPlayPhaseController = new MainPlayPhaseController();
	}
	@AfterEach()
	void tearDown() {
		mainPlayPhaseController = null;
		
	}
	
	@Test
	void getPlaySetupInvalidCommandsTest() throws GeneralException {
		
		String passEditCountryCommand = "editcountry -add INDIA";
		
		Player player = new Player("ROHIT");
		
		ResponseWrapper responseOfBombCommandPlaySetUpPhase = mainPlayPhaseController.getMainPlaySetUpCommands(player,passEditCountryCommand);
		
		ResponseWrapper expectedResponse = new ResponseWrapper(404, "Incorrect Command in Current State");
		assertEquals(responseOfBombCommandPlaySetUpPhase.getDescription(),expectedResponse.getDescription());
		assertEquals(responseOfBombCommandPlaySetUpPhase.getStatusValue(),expectedResponse.getStatusValue());
		
	}
	

}
