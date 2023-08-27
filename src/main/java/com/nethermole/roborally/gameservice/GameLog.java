package com.nethermole.roborally.gameservice;

import com.nethermole.roborally.gamepackage.ViewStep;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GameLog {

    List<List<ViewStep>> viewStepsByTurn;

    public GameLog() {
        viewStepsByTurn = new ArrayList<>();
    }

    public void addViewSteps(int turn, List<ViewStep> viewSteps) {
        while (turn >= viewStepsByTurn.size()) {
            viewStepsByTurn.add(new ArrayList<>());
        }
        for (ViewStep viewStep : viewSteps) {
            addViewStep(turn, viewStep);
        }
    }

    public void addViewStep(int turn, ViewStep viewStep) {
        viewStepsByTurn.get(turn).add(viewStep);
    }

    public List<ViewStep> getViewstepsByTurn(int turn) {
        return viewStepsByTurn.get(turn);
    }

    public void dumpToFile(){
        String uuid = UUID.randomUUID().toString().replace("-","_");

        String filename = (new Timestamp(new Date().getTime())).toString()
                .replace("-","_")
                .replace(" ","__")
                .replace(":","_")
                .substring(0,20)
                + uuid;

        try {
            PrintWriter printWriter = new PrintWriter("gamereports/" + filename);

            printWriter.print(viewStepsByTurn);
            printWriter.close();

        } catch(Exception e){
            System.out.print("printwriter exception: " + e.getMessage());
        }
    }

}
