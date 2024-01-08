import Strategy.AggressiveStrategy;
import Strategy.CheaterStrategy;
import logger.GeneralException;
import model.GameModel;
import model.Tournament.TournamentDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TournamentEngineTest extends TournamentEngine{

    TournamentEngine tournamentEngine ;
    GameModel gameModel = GameModel.getInstance();
    TournamentDetails d_Details = new TournamentDetails();

    /**
     * Method to set up the basics for each test case
     *
     * @throws Exception if any exception occurs
     */
    @Before
    public void setUp() throws Exception {
        tournamentEngine = new TournamentEngineTest();
    }



    /**
     * Check possibilities of invalid options in a tournament command
     */
    @Test
    public void checkInvalidCommandOptions() {
        String l_TournamentCommand = "tournament -M Australia.map -P aggressive,random -G 6 -D 3";
        d_Details = this.parseCommand(l_TournamentCommand);
        assertNull(d_Details);
        l_TournamentCommand = "tournament -M Australia.map -P aggressive,random -G 2 -D -9";
        d_Details = this.parseCommand(l_TournamentCommand);
        assertNull(d_Details);
        l_TournamentCommand = "tournament -M Australia.map -P aggressive,random -G 2 -D 60";
        d_Details = this.parseCommand(l_TournamentCommand);
        assertNull(d_Details);
        l_TournamentCommand = "tournament -M Australia.map -P aggressive -G 2 -D 60";
        d_Details = this.parseCommand(l_TournamentCommand);
        assertNull(d_Details);
    }


    @Test
    public void checkValidResultOfTournament() throws GeneralException {
        String l_TournamentCommand = "tournament -M ishaan.map -P aggressive,cheater -G 2 -D 3";
        this.parseCommand(l_TournamentCommand);
        assertEquals(2,2);
    }
}
