package com.nethermole.roborally.gamepackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfig {

    public static GameConfig standardHuman4Player = new GameConfig(1,3);
    public static GameConfig standardBot4Player = new GameConfig(0,4);

    //Start Info
    int humanPlayers;
    int botPlayers;
    private Long gameSeed;

    //Standard config
    private int maxHandSize;

    public GameConfig(int humanPlayers, int botPlayers){
        this.humanPlayers = humanPlayers;
        this.botPlayers = botPlayers;

        maxHandSize = 5;
    }

    public GameConfig(int humanPlayers, int botPlayers, Long gameSeed){
        this.humanPlayers = humanPlayers;
        this.botPlayers = botPlayers;
        this.gameSeed = gameSeed;

        maxHandSize = 5;
    }

    public Long getGameSeed(){
        if(gameSeed == null){
            gameSeed = (new Random()).nextLong();
        }
        return gameSeed;
    }


}
