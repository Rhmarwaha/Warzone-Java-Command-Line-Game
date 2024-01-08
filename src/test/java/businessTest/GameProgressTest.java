package businessTest;

import model.Continent;
import model.Country;
import model.GameModel;
import model.MapModel;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import persistence.GameModelAlteration;
import persistence.MapFileAlteration;
import business.GameProgress;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameProgressTest extends GameProgress{

    MapFileAlteration d_MFA;
    GameModelAlteration d_GMA;
    GameModel d_gameModel = GameModel.getInstance();
    MapModel d_mapModel = MapModel.getInstance();
    Continent d_America;
    Country d_Canada, d_USA, d_Mexico, d_Guatemala, d_Nicaragua, d_Colombia, d_Venezuela, d_Ecuador, d_Peru, d_Brazil;

    Continent d_Asia;
    Country d_China, d_India, d_Japan, d_Korea;

    Continent d_Europe;
    Country d_France;

    @BeforeEach
    void setUp() throws Exception {

        d_MFA = new MapFileAlteration();
        d_GMA = new GameModelAlteration();

        d_America = new Continent(1,"America");

        d_Canada = new Country("Canada", d_America);
        d_USA = new Country("USA", d_America);
        d_Mexico = new Country("Mexico", d_America);
        d_Guatemala = new Country("Guatemala", d_America);
        d_Nicaragua = new Country("Nicaragua", d_America);
        d_Colombia = new Country("Colombia", d_America);
        d_Venezuela = new Country("Venezuela", d_America);
        d_Ecuador = new Country("Ecuador", d_America);
        d_Peru = new Country("Peru", d_America);
        d_Brazil = new Country("Brazil", d_America);

        d_Asia = new Continent(2,"Asia");

        d_China = new Country("China", d_Asia);
        d_India = new Country("India", d_Asia);
        d_Japan = new Country("Japan", d_Asia);
        d_Korea = new Country("Korea", d_Asia);


        //America

        d_MFA.addContinent(d_America);

        d_MFA.addCountry(d_Canada);
        d_MFA.addCountry(d_USA);
        d_MFA.addCountry(d_Mexico);
        d_MFA.addCountry(d_Guatemala);
        d_MFA.addCountry(d_Nicaragua);
        d_MFA.addCountry(d_Colombia);
        d_MFA.addCountry(d_Venezuela);
        d_MFA.addCountry(d_Ecuador);
        d_MFA.addCountry(d_Peru);
        d_MFA.addCountry(d_Brazil);

        d_MFA.addNeighbour(d_Canada, d_USA);

        d_MFA.addNeighbour(d_USA, d_Canada);
        d_MFA.addNeighbour(d_USA, d_Mexico);

        d_MFA.addNeighbour(d_Mexico, d_USA);
        d_MFA.addNeighbour(d_Mexico, d_Guatemala);

        d_MFA.addNeighbour(d_Guatemala, d_Mexico);
        d_MFA.addNeighbour(d_Guatemala, d_Nicaragua);

        d_MFA.addNeighbour(d_Nicaragua, d_Guatemala);
        d_MFA.addNeighbour(d_Nicaragua, d_Colombia);

        d_MFA.addNeighbour(d_Colombia, d_Nicaragua);
        d_MFA.addNeighbour(d_Colombia, d_Venezuela);
        d_MFA.addNeighbour(d_Colombia, d_Ecuador);
        d_MFA.addNeighbour(d_Colombia, d_Brazil);
        d_MFA.addNeighbour(d_Colombia, d_Peru);

        d_MFA.addNeighbour(d_Venezuela, d_Colombia);
        d_MFA.addNeighbour(d_Venezuela, d_Brazil);

        d_MFA.addNeighbour(d_Ecuador, d_Colombia);
        d_MFA.addNeighbour(d_Ecuador, d_Peru);

        d_MFA.addNeighbour(d_Peru, d_Ecuador);
        d_MFA.addNeighbour(d_Peru, d_Colombia);
        d_MFA.addNeighbour(d_Peru, d_Brazil);

        d_MFA.addNeighbour(d_Brazil, d_Peru);
        d_MFA.addNeighbour(d_Brazil, d_Colombia);
        d_MFA.addNeighbour(d_Brazil, d_Venezuela);

        //Asia

        d_MFA.addContinent(d_Asia);

        d_MFA.addCountry(d_China);
        d_MFA.addCountry(d_India);
        d_MFA.addCountry(d_Japan);
        d_MFA.addCountry(d_Korea);

        d_MFA.addNeighbour(d_India, d_China);

        d_MFA.addNeighbour(d_China, d_India);
        d_MFA.addNeighbour(d_China, d_Japan);
        d_MFA.addNeighbour(d_China, d_Korea);

        d_MFA.addNeighbour(d_Japan, d_China);

        d_MFA.addNeighbour(d_Korea, d_China);

        d_Europe = new Continent(2, "Europe");

        d_France = new Country("France", d_Europe);
        d_GMA.assignCountries();

    }

    @After
    public void tearDown() throws Exception {
        d_mapModel.clearMap();
    }

    @Test
    public void SaveGame() {
        assertEquals(true, SaveGame(d_gameModel,d_mapModel, "output"));
    }

    @Test
    public void loadGameSuccess() throws IOException {
        assertEquals(true, LoadGameTestMethod("output.game"));
    }

}
