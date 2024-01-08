package model.Tournament;

import Strategy.PlayerStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TournamentDetails {

    private List<String> d_mapList = new ArrayList<>();

    private Set<PlayerStrategy> d_PlayerStrategies = new HashSet<>();

    private int d_GamesNumber;

    private int d_MaxNumberOfTries;


    public List<String> getD_mapList() {
        return d_mapList;
    }

    public void setD_mapList(List<String> d_mapList) {
        this.d_mapList = d_mapList;
    }

    public Set<PlayerStrategy> getD_PlayerStrategies() {
        return d_PlayerStrategies;
    }

    public void setD_PlayerStrategies(Set<PlayerStrategy> d_PlayerStrategies) {
        this.d_PlayerStrategies = d_PlayerStrategies;
    }

    public int getD_GamesNumber() {
        return d_GamesNumber;
    }

    public void setD_GamesNumber(int d_GamesNumber) {
        this.d_GamesNumber = d_GamesNumber;
    }

    public int getD_MaxNumberOfTries() {
        return d_MaxNumberOfTries;
    }

    public void setD_MaxNumberOfTries(int d_MaxNumberOfTries) {
        this.d_MaxNumberOfTries = d_MaxNumberOfTries;
    }
}
