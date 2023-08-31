package com.nethermole.roborally.gamepackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfig {

    public static GameConfig standardHuman4Player = new GameConfig(1,3);
    public static GameConfig standardBot4Player = new GameConfig(0,4);

    //Start Info
    int humanPlayers;
    int botPlayers;

    //Standard config
    @JsonIgnore
    private int maxHandSize;

    public GameConfig(int humanPlayers, int botPlayers){
        this.humanPlayers = humanPlayers;
        this.botPlayers = botPlayers;

        maxHandSize = 5;
    }


}
