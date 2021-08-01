package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.board.element.Conveyor;
import com.nethermole.roborally.gamepackage.board.element.Gear;
import com.nethermole.roborally.gamepackage.board.element.Laser;
import com.nethermole.roborally.gamepackage.board.element.Orientation;
import com.nethermole.roborally.gamepackage.board.element.Pit;
import com.nethermole.roborally.gamepackage.board.element.Wall;
import com.nethermole.roborally.gamepackage.board.element.Wrench;

public class BoardFactory {

    public Board board_empty() {
        Board board = new Board(12, 12);
        return board;
    }

    public Board board_exchange() {
        Board board = board_empty();

        //row0
        board.addElement(new Wrench(), new Position(0, 0));
        board.addElement(new Conveyor(Direction.DOWN), new Position(1, 0));
        board.addElement(new Wall(Direction.DOWN), new Position(2, 0));
        board.addElement(new Conveyor(Direction.UP), new Position(3, 0));
        board.addElement(new Wall(Direction.DOWN), new Position(4, 0));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 0));
        //board.addElement(new BoardName("Exchange"), new Position(6,0));
        board.addElement(new Wall(Direction.DOWN), new Position(7, 0));
        board.addElement(new Conveyor(Direction.DOWN), new Position(8, 0));
        board.addElement(new Wall(Direction.DOWN), new Position(9, 0));
        board.addElement(new Pit(), new Position(10, 0));

        //row1
        board.addElement(new Conveyor(Direction.UP), new Position(3, 1));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 1));
        board.addElement(new Conveyor(Direction.UP), new Position(6, 1));
        board.addElement(new Conveyor(Direction.DOWN), new Position(8, 1));
        board.addElement(new Wall(Direction.RIGHT), new Position(10, 1));

        //row2
        board.addElement(new Wall(Direction.RIGHT), new Position(0, 2));
        board.addElement(new Pit(), new Position(1, 2));
        board.addElement(new Conveyor(Direction.UP), new Position(3, 2));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 2));
        board.addElement(new Conveyor(Direction.UP), new Position(6, 2));
        board.addElement(new Conveyor(Direction.DOWN), new Position(8, 2));
        board.addElement(new Wall(Direction.UP), new Position(10, 2));
        board.addElement(new Wall(Direction.RIGHT), new Position(11, 2));

        //row3
        board.addElement(new Conveyor(Direction.LEFT), new Position(0, 3));
        board.addElement(new Conveyor(Direction.LEFT), new Position(1, 3));
        board.addElement(new Conveyor(Direction.LEFT), new Position(2, 3));
        board.addElement(new Gear(Spin.COUNTERCLOCKWISE), new Position(3, 3));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 3));
        board.addElement(new Conveyor(Direction.UP), new Position(6, 3));
        board.addElement(new Gear(Spin.COUNTERCLOCKWISE), new Position(8, 3));
        board.addElement(new Conveyor(Direction.LEFT), new Position(9, 3));
        board.addElement(new Conveyor(Direction.LEFT), new Position(10, 3));
        board.addElement(new Conveyor(Direction.LEFT), new Position(11, 3));

        //row4
        board.addElement(new Wall(Direction.LEFT), new Position(0, 4));
        board.addElement(new Wall(Direction.UP), new Position(4, 4));
        board.addElement(new Wall(Direction.RIGHT), new Position(4, 4));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 4));
        board.addElement(new Conveyor(Direction.UP), new Position(6, 4));
        board.addElement(new Wall(Direction.UP), new Position(7, 4));
        board.addElement(new Wall(Direction.LEFT), new Position(7, 4));
        board.addElement(new Wall(Direction.RIGHT), new Position(11, 4));

        //row5
        board.addElement(new Conveyor(Direction.RIGHT), new Position(0, 5));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(1, 5));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(2, 5));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(3, 5));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(4, 5));
        board.addElement(new Conveyor(Direction.RIGHT, 2), new Position(7, 5));
        board.addElement(new Conveyor(Direction.RIGHT, 2), new Position(8, 5));
        board.addElement(new Conveyor(Direction.RIGHT, 2), new Position(9, 5));
        board.addElement(new Conveyor(Direction.RIGHT, 2), new Position(10, 5));
        board.addElement(new Conveyor(Direction.RIGHT, 2), new Position(11, 5));

        //row6
        board.addElement(new Conveyor(Direction.LEFT), new Position(0, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(1, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(2, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(3, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(4, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(7, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(8, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(9, 6));
        board.addElement(new Conveyor(Direction.LEFT), new Position(10, 6));

        //row7
        board.addElement(new Wall(Direction.LEFT), new Position(0, 7));
        board.addElement(new Wall(Direction.RIGHT), new Position(4, 7));
        board.addElement(new Wall(Direction.DOWN), new Position(4, 7));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 7));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(6, 7));
        board.addElement(new Wrench(2), new Position(7, 7));
        board.addElement(new Wall(Direction.LEFT), new Position(7, 7));
        board.addElement(new Wall(Direction.DOWN), new Position(7, 7));
        board.addElement(new Wall(Direction.RIGHT), new Position(11, 7));

        //row8
        board.addElement(new Conveyor(Direction.RIGHT), new Position(0, 8));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(1, 8));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(2, 8));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(3, 8));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 8));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(6, 8));
        board.addElement(new Gear(Spin.COUNTERCLOCKWISE), new Position(8, 8));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(9, 8));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(10, 8));
        board.addElement(new Conveyor(Direction.RIGHT), new Position(11, 8));

        //row9
        board.addElement(new Wall(Direction.LEFT), new Position(0, 9));
        board.addElement(new Wall(Direction.DOWN), new Position(2, 9));
        board.addElement(new Laser(Orientation.VERTICAL), new Position(2, 9));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(3, 9));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 9));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(6, 9));
        board.addElement(new Conveyor(Direction.DOWN), new Position(8, 9));
        board.addElement(new Wall(Direction.RIGHT), new Position(11, 9));

        //row10
        board.addElement(new Conveyor(Direction.LEFT), new Position(0, 10));
        board.addElement(new Gear(Spin.CLOCKWISE), new Position(1, 10));
        board.addElement(new Laser(Orientation.VERTICAL), new Position(2, 10));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(3, 10));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 10));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(6, 10));
        board.addElement(new Conveyor(Direction.DOWN), new Position(8, 10));
        board.addElement(new Wall(Direction.LEFT), new Position(9, 10));
        board.addElement(new Gear(Spin.CLOCKWISE), new Position(10, 10));
        board.addElement(new Conveyor(Direction.LEFT), new Position(11, 10));

        //row11
        board.addElement(new Conveyor(Direction.DOWN), new Position(1, 11));
        board.addElement(new Wall(Direction.UP, true), new Position(2, 11));
        board.addElement(new Laser(Orientation.VERTICAL), new Position(2, 11));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(3, 11));
        board.addElement(new Wall(Direction.UP), new Position(4, 11));
        board.addElement(new Conveyor(Direction.DOWN), new Position(5, 11));
        board.addElement(new Conveyor(Direction.UP, 2), new Position(6, 11));
        board.addElement(new Wall(Direction.UP), new Position(7, 11));
        board.addElement(new Conveyor(Direction.DOWN), new Position(8, 11));
        board.addElement(new Wall(Direction.UP), new Position(9, 11));
        board.addElement(new Conveyor(Direction.UP), new Position(10, 11));
        board.addElement(new Wrench(), new Position(11, 11));

        return board;
    }

}
