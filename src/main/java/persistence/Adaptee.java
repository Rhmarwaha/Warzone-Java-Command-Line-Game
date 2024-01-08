package persistence;

import Constants.ProjectConfig;
import GamePhase.MapPhaseState;
import logger.ConsoleWriter;
import logger.LogEntryBuffer;
import logger.LogGenerator;
import model.*;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Adaptee {

    private MapModel d_mapModel;
    private GameModel gameModel;
    private LogEntryBuffer d_logger;
    private ConsoleWriter d_consoleWriter;
    private LogGenerator d_logGenrator;
    private FileReader d_mapFileReader;
    private BufferedReader d_bufferReader;
    private FileWriter d_mapFileWriter;
    private BufferedWriter d_bufferWriter;
    StringBuilder stringBuilder = new StringBuilder();

    public Adaptee() {
        d_mapModel = MapModel.getInstance();
        gameModel  = GameModel.getInstance();
        d_logger = new LogEntryBuffer();
        d_consoleWriter = new ConsoleWriter();
        d_logGenrator = LogGenerator.getInstance();
        d_logger.addObserver(d_consoleWriter);
        d_logger.addObserver(d_logGenrator);
    }

    public void readMapFile() {
        d_logger.setLogMessage("Conquest Map is loaded.");
        this.d_mapModel.clearMap();

        try {

            d_mapFileReader = new FileReader(ProjectConfig.D_MAP_FILES_PATH+ MapPhaseState.D_CURRENT_MAP);
            d_bufferReader = new BufferedReader(d_mapFileReader);

        } catch (FileNotFoundException p_e) {
            // Log for error in the function
            p_e.printStackTrace();
        }

        String l_mapFileLine ;
        boolean l_isMapContent , l_isCountriesTableContent,l_isContinentTableContent,l_isBorderTableContent;
        l_isMapContent = l_isCountriesTableContent = l_isContinentTableContent = l_isBorderTableContent = false;

        try {
            while ((l_mapFileLine = d_bufferReader.readLine()) != null) {
                if(l_mapFileLine.equals("MAP")) {
                    l_isMapContent = true;
                    l_isCountriesTableContent = l_isContinentTableContent = l_isBorderTableContent = false;
                    continue;
                }else if(l_mapFileLine.equals("CONTINENTS_TABLE")) {
                    l_isContinentTableContent = true;
                    l_isMapContent = l_isCountriesTableContent = l_isBorderTableContent = false;
                    continue;
                }else if(l_mapFileLine.equals("COUNTRIES_TABLE")) {
                    l_isCountriesTableContent = true;
                    l_isMapContent = l_isContinentTableContent = l_isBorderTableContent = false;
                    continue;
                }else if(l_mapFileLine.equals("BORDERS_TABLE")) {
                    l_isBorderTableContent = true;
                    l_isMapContent = l_isCountriesTableContent = l_isContinentTableContent = false;
                    continue;
                }

                if(l_isMapContent) {
                    this.d_mapModel.setMapName(l_mapFileLine);
                }else if(l_isContinentTableContent) {
                    String[] l_continentRow = l_mapFileLine.trim().split("\\s+");
                    try {
                        this.d_mapModel.addContinent(new Continent(Integer.parseInt(l_continentRow[0]),l_continentRow[1],l_continentRow[2]));
                    }catch(IndexOutOfBoundsException ex) {

                    }

                }else if(l_isCountriesTableContent) {
                    String[] l_countryRow = l_mapFileLine.trim().split("\\s+");
                    try {
                        Country l_country = new Country(Integer.parseInt(l_countryRow[0]), l_countryRow[1]);

                        if(this.d_mapModel.getContinents().size() <= Integer.parseInt(l_countryRow[2])) {
                            this.d_mapModel.addContinentCountries(null, l_country);
                        }else {
                            this.d_mapModel.addContinentCountries(this.d_mapModel.getContinents().get(Integer.parseInt(l_countryRow[2])), l_country);
                        }

                    }catch(IndexOutOfBoundsException ex) {
                        System.out.println(ex);
                    }


                }else if(l_isBorderTableContent) {
                    String[] l_borderRow = l_mapFileLine.trim().split("\\s+");
                    Country l_mainCountry = null ;
                    try {
                        if(this.d_mapModel.getCountries().size() > Integer.parseInt(l_borderRow[0])){
                            l_mainCountry = this.d_mapModel.getCountries().get(Integer.parseInt(l_borderRow[0]));
                        }else {

                        }

                    }catch(IndexOutOfBoundsException ex) {
                        System.out.println(ex+"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
                    }

                    for(int counter = 1; counter<l_borderRow.length; counter++) {
                        try {
                            if(this.d_mapModel.getCountries().size() <= Integer.parseInt(l_borderRow[counter])) {
                                this.d_mapModel.addBorders(l_mainCountry,null);
                            }else {
                                this.d_mapModel.addBorders(l_mainCountry, this.d_mapModel.getCountries().get(Integer.parseInt(l_borderRow[counter])));
                            }

                        }catch(IndexOutOfBoundsException | NullPointerException ex) {

                        }

                    }
                }

            }
        } catch (IOException p_e) {

            p_e.printStackTrace();
        }
    }


    private void writeMapFile(String p_mapFileName) {
        try {
            if(! MapPhaseState.D_CURRENT_MAP.equals(p_mapFileName)) {
                try{
                    if(new File(ProjectConfig.D_MAP_FILES_PATH + p_mapFileName).createNewFile()) {
                        MapPhaseState.D_CURRENT_MAP = p_mapFileName;
                    }
                }catch(IOException p_exception) {

                }

            }

            d_mapFileWriter = new FileWriter(ProjectConfig.D_MAP_FILES_PATH+MapPhaseState.D_CURRENT_MAP);
            d_bufferWriter = new BufferedWriter(d_mapFileWriter);

            stringBuilder.append("MAP\n" + MapPhaseState.D_CURRENT_MAP+"\nCONTINENTS_TABLE\n");

            for(Continent continent : this.d_mapModel.getContinents()) {
                stringBuilder.append(continent.getUniqueContinetId())
                        .append(" ")
                        .append(continent.getContinentId())
                        .append(" ")
                        .append(continent.getContientValue())
                        .append("\n");
            }

            if(this.d_mapModel.getCountries() != null){
                stringBuilder.append("COUNTRIES_TABLE\n");
                for(Country country : this.d_mapModel.getCountries()) {
                    stringBuilder.append(country.getUniqueCountryId())
                            .append(" ")
                            .append(country.getCountryId())
                            .append(" ")
                            .append(country.getContinent().getUniqueContinetId())
                            .append("\n");
                }
            }
            if(this.d_mapModel.getBorders() != null) {
                stringBuilder.append("BORDERS_TABLE");
                for(Map.Entry<Country, List<Country>> border: this.d_mapModel.getBorders().entrySet()) {
                    stringBuilder.append("\n")
                            .append(border.getKey().getUniqueCountryId());
                    for(Country country : border.getValue()) {
                        stringBuilder.append(" ")
                                .append(country.getUniqueCountryId());

                    }
                }
            }
            d_bufferWriter.write(stringBuilder.toString());
            d_bufferWriter.flush();
        } catch (  IOException p_e) {
            // Log
            p_e.printStackTrace();
        }


    }

    public ResponseWrapper saveMap(String p_mapFileName) {
        this.writeMapFile(p_mapFileName);
        return new ResponseWrapper(200, "Save Map successfully ");
    }
}
