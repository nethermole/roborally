package com.nethermole.roborally.gamepackage;

import lombok.Data;

@Data
public class GameConfig {

    public static GameConfig standardHuman4Player = new GameConfig(1,3);
    public static GameConfig standardBot4Player = new GameConfig(0,4);

    //Start Info
    int humanPlayers;
    int botPlayers;

    //Standard config
    int maxHandSize;

    public GameConfig(int humanPlayers, int botPlayers){
        this.humanPlayers = humanPlayers;
        this.botPlayers = botPlayers;

        maxHandSize = 5;
    }


}
