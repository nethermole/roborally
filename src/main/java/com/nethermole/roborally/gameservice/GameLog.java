package com.nethermole.roborally.gameservice;

import com.nethermole.roborally.gamepackage.ViewStep;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameLog {

    List<List<ViewStep>> viewStepsByTurn;

    @PostConstruct
    public void init() {
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

}
