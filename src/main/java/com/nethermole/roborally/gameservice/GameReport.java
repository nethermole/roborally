package com.nethermole.roborally.gameservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.play.Play;
import com.nethermole.roborally.gamepackage.player.Player;
import lombok.Data;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class GameReport {

    //Use unique gamereport hash to update view?
    String gameUUID;
    Map<String, Player> players;
    List<TurnReport> turns;

    public GameReport() {
        turns = new ArrayList<>();
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public void startNewTurn(){
        turns.add(new TurnReport());
    }

    public void addPlay(Play play){
        if(turns.size() > 0){
            turns.get(turns.size()-1).plays.add(play);
        } else {
            System.out.println("dont do this (gamereport.addPlay)");
        }
    }

    public void dumpToFile() {
        String uuid = "REPORT-" + UUID.randomUUID().toString().replace("-","_");

        String filename = (new Timestamp(new Date().getTime())).toString()
                .replace("-","_")
                .replace(" ","__")
                .replace(":","_")
                .substring(0,20)
                + uuid;

        try {
            PrintWriter printWriter = new PrintWriter("gamereports/" + filename);

            printWriter.print((new ObjectMapper().writerWithDefaultPrettyPrinter()).writeValueAsString(this));
            printWriter.close();

        } catch(Exception e){
            System.out.print("printwriter exception: " + e.getMessage());
        }
    }


}
