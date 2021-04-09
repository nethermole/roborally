package com.nethermole.roborally.game.board;

import com.nethermole.roborally.game.board.element.Element;
import com.nethermole.roborally.game.board.element.ElementEnum;
import com.nethermole.roborally.game.deck.movement.Movement;
import com.nethermole.roborally.game.player.Player;
import com.nethermole.roborally.game.turn.Event;
import com.nethermole.roborally.game.turn.MovementMethod;
import com.nethermole.roborally.game.turn.RobotMoveEvent;
import com.nethermole.roborally.logs.GameEventLogger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Board {

    private List<Player> players;
    private GameEventLogger gameEventLogger;

    @Getter
    private Tile[][] squares;

    Coordinate start;

    public Board(int boardHeight, int boardWidth, Coordinate start, GameEventLogger gameEventLogger){
        this(boardHeight, boardWidth);
        this.start = start;
        this.gameEventLogger = gameEventLogger;
    }

    public Board(int boardHeight, int boardWidth){
        squares = new Tile[boardHeight][boardWidth];
        for(int i=0; i<boardHeight; i++){
            for(int j=0; j<boardWidth; j++){
                squares[i][j]= new Tile();
            }
        }

        players = new ArrayList<>();
    }

    public void addElement(Element element, Coordinate coordinate){
        squares[coordinate.getY()][coordinate.getX()].addElement(element);
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void movePlayer(Player player, Movement movement){
        switch(movement){
            case MOVE1:
               move1(player);
                break;
            case MOVE2:
                move1(player);
                move1(player);
                break;
            case MOVE3:
                move1(player);
                move1(player);
                move1(player);
                break;
            case TURN_RIGHT:
                turnRight(player);
                break;
            case TURN_LEFT:
                turnLeft(player);
                break;
            case UTURN:
                uturn(player);
                break;
            case BACKUP:
                backup(player);
                break;
            default:
                throw new RuntimeException("hit default case in movePlayer");
        }
    }

    public List<Event> move1(Player player){
        Coordinate startPosition = player.getPosition();
        Coordinate endPosition = startPosition.moveForward1(player.getFacing());
        player.setPosition(endPosition);

        if(isWallBetween(startPosition, endPosition)){
            log.info(player + " hit a wall instead of move1", player.getId());
            return null;
        } else{
            log.info(String.format("Robot belonging to player %s moved from %s to %s", player, startPosition, endPosition));

            List<Event> eventList = new ArrayList<>();
            RobotMoveEvent moveEvent = new RobotMoveEvent(player, startPosition, endPosition, player.getFacing(), player.getFacing(), MovementMethod.MOVE);
            eventList.add(moveEvent);

            return eventList;
        }
    }

    private void backup(Player player) {
        Coordinate startPosition = player.getPosition();
        Coordinate endPosition = startPosition.moveBackward1(player.getFacing());
        player.setPosition(endPosition);

        if(isWallBetween(startPosition, endPosition)){
            log.info("Robot belonging to player " + player + " hit a wall instead of move1", player.getId());
        } else {
            RobotMoveEvent moveEvent = new RobotMoveEvent(player, startPosition, endPosition, player.getFacing(), player.getFacing(), MovementMethod.MOVE);
            gameEventLogger.log(moveEvent);
        }
    }

    private void uturn(Player player){
        Direction startFacing = player.getFacing();
        player.setFacing(Direction.turnLeft(Direction.turnLeft(player.getFacing())));
        RobotMoveEvent playerMoveEvent = new RobotMoveEvent(player, player.getPosition(), player.getPosition(), startFacing, player.getFacing(), MovementMethod.TURN);

        gameEventLogger.log(playerMoveEvent);
    }

    private void turnLeft(Player player) {
        Direction startFacing = player.getFacing();
        player.setFacing(Direction.turnLeft(player.getFacing()));
        RobotMoveEvent playerMoveEvent = new RobotMoveEvent(player, player.getPosition(), player.getPosition(), startFacing, player.getFacing(), MovementMethod.TURN);

        gameEventLogger.log(playerMoveEvent);
    }

    private void turnRight(Player player) {
        Direction startFacing = player.getFacing();
        player.setFacing(Direction.turnRight(player.getFacing()));
        RobotMoveEvent playerMoveEvent = new RobotMoveEvent(player, player.getPosition(), player.getPosition(), startFacing, player.getFacing(), MovementMethod.TURN);

        gameEventLogger.log(playerMoveEvent);
    }

    private boolean isWallBetween(Coordinate startPosition, Coordinate endPosition){
        if(startPosition.getX() == endPosition.getX()){
            Coordinate leftPosition = startPosition.getX() < endPosition.getX() ? startPosition : endPosition;
            for(int i = leftPosition.getX() + 1; i < endPosition.getX(); i++){
                if(squares[leftPosition.getY()][i].hasElement(ElementEnum.VERTICAL_WALL)){
                    return true;
                }
            }
        }
        if(startPosition.getY() == endPosition.getY()){
            Coordinate topPosition = startPosition.getY() < endPosition.getY() ? startPosition : endPosition;
            for(int i = topPosition.getY() + 1; i < endPosition.getY(); i++){
                if(squares[i][topPosition.getX()].hasElement(ElementEnum.HORIZONTAL_WALL)){
                    return true;
                }
            }
        }
        return false;
    }



}
