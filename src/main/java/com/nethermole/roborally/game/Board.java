package com.nethermole.roborally.game;

import lombok.Getter;

public class Board {

    @Getter
    private Tile[][] squares;

    public Board(int boardHeight, int boardWidth){
        squares = new Tile[boardHeight][boardWidth];
        for(int i=0; i<boardHeight; i++){
            for(int j=0; j<boardWidth; j++){
                squares[i][j]= new Tile();
            }
        }
    }

    public void addElement(Element element, Coordinate coordinate){
        squares[coordinate.getY()][coordinate.getX()].addElement(element);
    }

}
