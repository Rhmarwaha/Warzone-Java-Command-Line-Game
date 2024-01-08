import GamePhase.MapPhaseState;
import Strategy.*;
import business.MainPlayPhaseBusinessCommands;
import business.SingleGamePlayerCommands;
import controller.MainPlayPhaseController;
import logger.ConsoleWriter;
import logger.GeneralException;
import logger.LogEntryBuffer;
import logger.LogGenerator;
import model.GameModel;
import model.MapModel;
import model.Player;
import model.ResponseWrapper;
import model.Tournament.TournamentDetails;
import model.Tournament.TournamentResults;
import persistence.GameModelAlteration;
import persistence.MapFileAlteration;

import java.io.Serializable;
import java.util.*;

public class TournamentEngine implements Serializable {

    private LogEntryBuffer d_logger;
    private ConsoleWriter d_consoleWriter;
    private LogGenerator d_logGenrator;
    TournamentDetails d_TournamentDetails;

    List<TournamentResults> d_Results = new ArrayList<>();

    MapFileAlteration mapFileAlteration;
    GameModelAlteration gameModelAlteration;
    MainPlayPhaseController p_mainPlayPhaseController;

    GameModel d_gameModel;
    MapModel d_mapModel;
    SingleGamePlayerCommands singleGamePlayerCommands;
    private final static Scanner l_Scanner = new Scanner(System.in);

    public TournamentEngine() {
        d_logger = new LogEntryBuffer();
        d_consoleWriter = new ConsoleWriter();
        d_logGenrator = LogGenerator.getInstance();
        d_logger.addObserver(d_consoleWriter);
        d_logger.addObserver(d_logGenrator);
        d_gameModel = GameModel.getInstance();
        mapFileAlteration = new MapFileAlteration();
        gameModelAlteration = new GameModelAlteration();
        singleGamePlayerCommands = new SingleGamePlayerCommands();
        p_mainPlayPhaseController = new MainPlayPhaseController();

    }

    public ResponseWrapper startTournamentMode() throws GeneralException{
        printTournamentDetails();
        executeTournament();
        return new ResponseWrapper(200, "");
    }

    public TournamentDetails printTournamentDetails() {
        d_logger.setLogMessage("");
        d_logger.setLogMessage("****************************************");
        d_logger.setLogMessage("*********** TOURNAMENT MODE ************");
        d_logger.setLogMessage("****************************************");
        d_logger.setLogMessage("");
        d_logger.setLogMessage("-> Enter the tournament command: tournament -M Map1.map,Map2.map -P strategy1,strategy2 -G noOfGames -D noOfTurns");
        d_logger.setLogMessage("");
        d_logger.setLogMessage("****************************************");
        d_logger.setLogMessage("****(Getting input from the user...)****");
        String l_TournamentCommand = l_Scanner.nextLine();
        d_TournamentDetails = parseCommand(l_TournamentCommand);
        if (Objects.isNull(d_TournamentDetails)) {
            printTournamentDetails();
        }
        return d_TournamentDetails;
    }

    public void executeTournament() throws GeneralException {
        for (String l_File : d_TournamentDetails.getD_mapList()) {
            for (int l_game = 1; l_game <= d_TournamentDetails.getD_GamesNumber(); l_game++) {
                GameModel.getInstance().maxNumberOfTurns = d_TournamentDetails.getD_MaxNumberOfTries();
                d_mapModel = MapModel.getInstance();
                d_mapModel.clearMap();
                d_gameModel.clearGameModel();
                TournamentResults l_Result = new TournamentResults();
                d_Results.add(l_Result);
                l_Result.setD_GameNumber(l_game);
                l_Result.setD_MapName(l_File);
                singleGamePlayerCommands.loadMap(l_File);
                for (PlayerStrategy l_PlayerStrategy : d_TournamentDetails.getD_PlayerStrategies()) {
                    Player l_Player = new Player(l_PlayerStrategy.getStrategyName());
                    l_Player.setStrategy(getPlayerStrategy(l_PlayerStrategy.getStrategyName(), l_Player));
                    d_gameModel.addPlayerInPlayersList(l_Player);
                    d_gameModel.addPlayerQueue(l_Player);
                }
                gameModelAlteration.assignCountries();
                SingleGameModePlayEngine l_GameEngine = new SingleGameModePlayEngine();
                l_GameEngine.continueGamePlay();
                Player l_winner = d_gameModel.getWinner();
                if (Objects.nonNull(l_winner)) {
                    l_Result.setD_GameWinner(l_winner.getPlayerName());
                } else {
                    l_Result.setD_GameWinner("Draw");
                }
            }
        }

        String l_Table = "%-19s%-32s%-19s%n";
        System.out.format("******************************************************************%n");
        System.out.format("Map            !       Winner               !   Game Number       %n");
        System.out.format("******************************************************************%n");

        for (TournamentResults l_Result : d_Results) {

            System.out.format(l_Table, l_Result.getD_MapName(), l_Result.getD_GameWinner(), l_Result.getD_GameNumber() );

        }
        System.out.format("******************************************************************%n");

    }

    public PlayerStrategy getPlayerStrategy(String nameOfStrategy, Player player) {
        if (nameOfStrategy.equalsIgnoreCase("aggressive")){
            return new AggressiveStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance());
        } else if ((nameOfStrategy.equalsIgnoreCase("benevolent"))){
            return new BenevolentStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance());
        } else if ((nameOfStrategy.equalsIgnoreCase("cheater"))){
            return new CheaterStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance());
        } else if ((nameOfStrategy.equalsIgnoreCase("random"))){
            return new RandomStrategy(player, MapModel.getInstance(), p_mainPlayPhaseController, MainPlayPhaseBusinessCommands.getMainPlayPhaseBusinessCommandsInstance());
        }
        return null;
    }

    public TournamentDetails parseCommand(String p_TournamentCommand) {
        try {
            d_TournamentDetails = new TournamentDetails();
            if (!p_TournamentCommand.isEmpty() &&
                    p_TournamentCommand.contains("-M") && p_TournamentCommand.contains("-P")
                    && p_TournamentCommand.contains("-G") && p_TournamentCommand.contains("-D")) {
                List<String> l_CommandList = Arrays.asList(p_TournamentCommand.split(" "));
                String l_MapValue = l_CommandList.get(l_CommandList.indexOf("-M") + 1);
                String l_PlayerTypes = l_CommandList.get(l_CommandList.indexOf("-P") + 1);
                String l_GameCount = l_CommandList.get(l_CommandList.indexOf("-G") + 1);
                String l_maxTries = l_CommandList.get(l_CommandList.indexOf("-D") + 1);
                d_TournamentDetails.getD_mapList().addAll(Arrays.asList(l_MapValue.split(",")));
                if(l_PlayerTypes.contains("human")) {
                    d_logger.setLogMessage("Tournament mode does not support human player: Switch to Single Game Mode");
                    return null;
                }
                for (String l_Strategy : l_PlayerTypes.split(",")) {
                    d_TournamentDetails.getD_PlayerStrategies().add(PlayerStrategy.getStrategy(l_Strategy));
                }
                if (d_TournamentDetails.getD_PlayerStrategies().size() < 2) {
                    return null;
                }
                int l_NumOfGames = Integer.parseInt(l_GameCount);
                int l_NumofTurns = Integer.parseInt(l_maxTries);
                if (l_NumOfGames > 0 && l_NumOfGames <= 5 && l_NumofTurns > 0 && l_NumofTurns <= 50) {
                    d_TournamentDetails.setD_GamesNumber(l_NumOfGames);
                    d_TournamentDetails.setD_MaxNumberOfTries(l_NumofTurns);
                } else {
                    d_logger.setLogMessage("Give correct number of games and turns");
                    return null;
                }
            } else {
                return null;
            }
            return d_TournamentDetails;
        } catch (Exception e) {
            d_logger.setLogMessage("Check your command");
            d_logger.setLogMessage("command should be in this format: tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns");
            return null;
        }
    }
}
