package com.nethermole.roborally;

import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.game.*;
import com.nethermole.roborally.view.AbstractView;
import com.nethermole.roborally.view.POCView;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Gamemaster {

    @Getter
    private Game game;

    @Getter
    private List<Player> players;
    private List<AbstractView> viewers;

    public void startGame(HashMap<Element, Coordinate> elements, List<Player> players){
        this.players = players;
        game = new Game(elements, players, 8,8);
        POCView pocView = POCView.startPOCView(this);
        viewers = new ArrayList<>();
        viewers.add(pocView);
    }

    public void stopGame(){
        for(AbstractView view: viewers){
            view.stopViewing();
        }
        game = null;
    }

    public Board getBoard() throws GameNotStartedException {
        if(game == null){
            throw new GameNotStartedException();
        }

        return game.getBoard();
    }
}
