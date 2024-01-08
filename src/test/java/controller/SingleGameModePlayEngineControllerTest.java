package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logger.GeneralException;
import model.ResponseWrapper;

class SingleGameModePlayEngineControllerTest {
	
	private SingleGameModePlayEngineController singleGameModePlayEngineController;
	
	@BeforeEach
	void setUp() {
		singleGameModePlayEngineController = new SingleGameModePlayEngineController();
	}
	@AfterEach()
	void tearDown() {
		singleGameModePlayEngineController = null;
		
	}
	
	@Test
	void getPlaySetupInvalidCommandsTest() throws GeneralException {
		
		String passBombCommand = "bomb Pakistan";
		ResponseWrapper responseOfBombCommandPlaySetUpPhase = singleGameModePlayEngineController.getPlaySetupCommands(passBombCommand);
		
		ResponseWrapper expectedResponse = new ResponseWrapper(404, "Incorrect Command in Current State");
		assertEquals(responseOfBombCommandPlaySetUpPhase.getDescription(),expectedResponse.getDescription());
		assertEquals(responseOfBombCommandPlaySetUpPhase.getStatusValue(),expectedResponse.getStatusValue());
		
	}
	
	
	
	@Test
	void atLeastTwoPlayersBeforeContinueToNextPhase() throws GeneralException {
		
		//loadFirstThenShowMapTest
		String passShowmapCommand = "showmap";
		
		ResponseWrapper responseOfShowmapCommandPlaySetUpPhase = singleGameModePlayEngineController.getPlaySetupCommands(passShowmapCommand);
		ResponseWrapper expectedShowmapCommandResponse = new ResponseWrapper(404, "Launch Load Map command first");
		
		assertEquals(responseOfShowmapCommandPlaySetUpPhase.getDescription(),expectedShowmapCommandResponse.getDescription());
		assertEquals(responseOfShowmapCommandPlaySetUpPhase.getStatusValue(),expectedShowmapCommandResponse.getStatusValue());
		
		
		
	}
}
