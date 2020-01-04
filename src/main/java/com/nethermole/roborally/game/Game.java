package com.nethermole.roborally.game;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    @Getter
    private Board board;

    private List<Player> players;

    public Game(HashMap<Element, Coordinate> boardElements, List<Player> players, int boardWidth, int boardHeight){
        this.players = players;
        constructBoard(boardElements, boardHeight, boardWidth);
    }

    private void constructBoard(HashMap<Element, Coordinate> boardElements, int boardHeight, int boardWidth){
        board = new Board(boardHeight,boardWidth);
        for(Map.Entry<Element, Coordinate> boardElement : boardElements.entrySet()){
            board.addElement(boardElement.getKey(), boardElement.getValue());
        }
    }

}
