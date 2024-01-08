package business;

import Strategy.HumanStrategy;
import controller.MainPlayPhaseController;
import controller.SingleGameModePlayEngineController;
import logger.ConsoleWriter;
import logger.GeneralException;
import logger.LogEntryBuffer;
import model.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LoadGameEngine implements Serializable {

    String l_Table = "- %-21s - %-16s - %-22s%n";
    String l_Table2 = "- %-21s - %-16s - %-13s - %-13s%n";
    String l_Columns = " %-16s  %-20s   %-22s%n";
    String l_NTable = "- %-21s - %-22s%n";
    String l_NColumns = "%-21s   %-22s%n";

    /**
     * MainPlayPhaseController object
     */
    private MainPlayPhaseController  mainPlayPhaseController;
    /**
     * MainPlayPhaseBusinessCommands object
     */
    private MainPlayPhaseBusinessCommands mainPlayPhaseBusinessCommands = new MainPlayPhaseBusinessCommands();
    private LogEntryBuffer d_logger;
    /**
     * Console Writer
     */
    private ConsoleWriter d_consoleWriter;

    public LoadGameEngine() {
        mainPlayPhaseBusinessCommands = new MainPlayPhaseBusinessCommands();
        d_logger = new LogEntryBuffer();
        d_consoleWriter = new ConsoleWriter();
    }

    private void printMainPlaySetupCommands() {
        //Reinforcement


        // attack
        System.out.println(" ");
        System.out.println("****************************************");
        System.out.println("************ ORDER CREATION ************");
        System.out.println("****************************************");
        System.out.println(" ");
        System.out.println("-> Deploy Order Command:  deploy countryID numarmies");
        System.out.println("-> Advance Order Command: advance countrynamefrom countynameto numarmies");
        System.out.println("-> Bomb Order Command: bomb countryID");
        System.out.println("-> Airlift Order Command: airlift sourcecountryID targetcountryID numarmies");
        System.out.println("-> Blockade Order Command: blockade countryID");
        System.out.println("-> Diplomacy Order Command: negotiate playerID");
        System.out.println("-> Commit Orders: commit");
        System.out.println("-> To save the game: savegame filename");
        System.out.println("-> Exit Game: exit");
        System.out.println(" ");
        System.out.println("***** Input any command to proceed *****");
        System.out.println("****(Getting input from the user...)****");
        System.out.println(" ");

        //commit

        // who win
    }

    public ResponseWrapper continueGamePlay(GameModel gameModel, MapModel d_mapModel) throws GeneralException {
        ResponseWrapper mainPlaySetUpResponse = null;
        while(true) {
            if(gameModel.getPlayers()==null)
            {
                System.out.println("Cannot Continue with GamePlay Phase as players are not defined or map is not choosen");
                return new ResponseWrapper(404, "Cannot Continue with GamePlay Phase as players are not available");
            }

            gameModel.resetPeaceForAllPlayers();
            gameModel.resetCommit();

            // do Reinforcements
            mainPlayPhaseBusinessCommands.doReinforcements();

            // edit strategy
            if(!gameModel.isStrategyPermanent()) {
                gameModel.editStrategy(mainPlayPhaseController);
            }

            for(Player player : gameModel.getPlayers()) {
                if(player.getCanAddCard() && player.getStrategy() instanceof HumanStrategy) {
                    player.addCard();
                }
                player.endTurnCardReset();
            }

            if (gameModel.numberOfTries <= gameModel.getMaxNumberOfTurns()){
                gameModel.incrementNumberOfTries();
                while(true) {
                    // get player's turn
                    this.printMainPlaySetupCommands();
                    Player currentPlayer = gameModel.getNextPlayer();

                    if(currentPlayer.getCountriesHold().isEmpty()) {
                        currentPlayer.performCommit();
                        mainPlaySetUpResponse = new ResponseWrapper(200, currentPlayer.getPlayerName() + "HAS NO MORE COUNTRIES  AND IS OUT OF THE GAME");
                        System.out.println(mainPlaySetUpResponse.getDescription());
                    }
                    else {
                        System.out.println("***********************************************************************");
                        System.out.println(" Current Player  !  Initial Assigned  !  Left Armies  !  Strategy Name");
                        System.out.println("***********************************************************************");
                        System.out.format(l_Table2, currentPlayer.getPlayerName(), currentPlayer.getCurrentArmies(),  currentPlayer.getArmiesToIssue(), currentPlayer.getStrategy().getStrategyName());
                        System.out.println("*****************************************************");
                        String country_title = "Country Name";
                        String armies_title = "Country Armies";
                        String neighbors_title = "Neighbors";
                        System.out.format(l_Columns, country_title, armies_title, neighbors_title);
                        System.out.format("*****************************************************%n");

                        Map<Country, List<Country>> neighbors = d_mapModel.getBorders();
                        for (Country l_Country : currentPlayer.getCountriesHold()) {
                            if (neighbors.containsKey(l_Country)){
                                System.out.format(l_Table, l_Country.getCountryId(), l_Country.getArmies(), this.getCountriesList(neighbors.get(l_Country)));
                            }
                        }
                        System.out.format("*****************************************************\n\n");

                        System.out.format("*****************************************************\n");
                        String neutralCountry_title = "Neutral Countries";

                        System.out.format(l_NColumns, neutralCountry_title, armies_title);
                        System.out.format("*****************************************************\n");
                        for(Country country : d_mapModel.getCountries()) {
                            if(country.getCountryOwner() == null) {
                                System.out.format(l_NTable, country.getCountryId(), country.getArmies());
                            }
                        }
                        System.out.format("*****************************************************\n");

                        gameModel.printCardsListForCurrentPlayer();
                        // ask for attack commands phase  with player


                        mainPlaySetUpResponse = currentPlayer.issueOrder();



                        System.out.println(mainPlaySetUpResponse.getDescription());


                    }


                    if(gameModel.checkAllCommit()) {
                        mainPlayPhaseBusinessCommands.executeOrders();
                        break;
                    }

                }
            }

            // in execution if player capture country he will get card
            // in execution if player going to win
            else {

            }


            mainPlaySetUpResponse = mainPlayPhaseBusinessCommands.endGame();

            if(mainPlaySetUpResponse.getStatusValue() == 201) {

                System.out.println("WINNER OF GAME");
                System.out.println("**************");

                for(Player player : gameModel.getPlayers()) {
                    if(player.getCountriesHold().size() > 0) {
                        System.out.println(player.getPlayerName());
                        gameModel.setWinner(player);
                    }
                }

                System.out.println("*********");
                System.out.println("GAME ENDS");
                System.out.println("*********");
                break;
            } else if (mainPlaySetUpResponse.getStatusValue() == 1000){
                System.out.println("THE GAME ENDED IN A DRAW IN " + gameModel.getMaxNumberOfTurns() + " TURNS.");
                System.out.println("************************");
                break;
            }


        }
        return null;
    }

    public String getCountriesList(List<Country> countriesList) {
        String l_countList = "";
        StringBuilder stringBuilder = new StringBuilder();

        for (Country country : countriesList) {
            stringBuilder.append(country.getCountryId());
            stringBuilder.append("-");
        }
        l_countList = stringBuilder.toString();
        return l_countList.length() > 0 ? l_countList.substring(0, l_countList.length() - 1) : "";
    }
}
