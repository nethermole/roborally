package com.nethermole.roborally.gamepackage.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nethermole.roborally.gamepackage.ViewStep;
import com.nethermole.roborally.gamepackage.action.BoardAction;
import com.nethermole.roborally.gamepackage.action.PitKillAction;
import com.nethermole.roborally.gamepackage.action.RobotMoveAction;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.board.element.ElementEnum;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.play.CardPlay;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.turn.MovementMethod;
import com.nethermole.roborally.gamepackage.turn.RobotMoveViewStep;
import com.nethermole.roborally.gameservice.GameLog;
import com.nethermole.roborally.gameservice.GameReport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class Board {

    @Getter
    private List<Player> players;

    @JsonProperty
    private Map<Integer, Map<Integer, Tile>> squares;

    private Map<Element, Position> elementPositions;

    @Setter
    private GameReport gameReport;

    @Setter
    private GameLog gameLog;

    public Board(int boardHeight, int boardWidth) {
        elementPositions = new HashMap<>();
        squares = new HashMap<>();
        for (int x = 0; x < boardHeight; x++) {
            squares.put(x, new HashMap<>());
            for (int y = 0; y < boardWidth; y++) {
                squares.get(x).put(y, new Tile());
            }
        }
        players = new ArrayList<>();
    }

    public int getWidth(){
        int maxX = 0;

        Iterator<Integer> xIter = squares.keySet().iterator();
        while(xIter.hasNext()){
            int xVal = xIter.next();
            if(xVal > maxX){
                maxX = xVal;
            }
        }
        return maxX;
    }

    public int getHeight(){
        int maxWidth = 0;
        for(Map<Integer, Tile> yCoord : squares.values()){
            for(Integer y : yCoord.keySet()){
                if(y > maxWidth){
                    maxWidth = y;
                }
            }
        }
        return maxWidth;
    }

    public Tile getTileAtPosition(Position position) {
        Map<Integer, Tile> row = squares.get(position.getX());
        if (row == null) {
            return Tile.getOutOfBounds();
        }

        Tile tile = row.get(position.getY());
        if(tile == null){
            return Tile.getOutOfBounds();
        }
        return tile;
    }

    public List<Map.Entry<Element, Position>> getAllElementsOfType(Class clazz){
        return elementPositions.entrySet().stream().filter(it -> it.getKey().getClass() == clazz).collect(Collectors.toList());
    }

    public void addBoard(Board otherBoard, int posX, int posY) {
        int startX = (posX * 12);
        int startY = (posY * 12);

        for (int x = 0; x < 12; x++) {
            Map<Integer, Tile> row = squares.getOrDefault(startX + x, new HashMap<>());
            for (int y = 0; y < 12; y++) {
                Map<Integer, Tile> otherRow = otherBoard.squares.getOrDefault(x, null);
                if (otherRow != null) {
                    Tile tile = otherRow.get(y);
                    row.put(startY + y, tile);
                }
            }
            squares.put(startX + x, row);
        }
    }

    public void addElement(Element element, Position position) {
        elementPositions.put(element, position);
        squares.get(position.getX()).get(position.getY()).addElement(element);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Position getPositionOfElement(Element element) {
        return elementPositions.get(element);
    }

    //break this out into a movement logic class
    public List<ViewStep> movePlayer(Player player, MovementCard movementCard) {
        CardPlay cardPlay = new CardPlay();
        cardPlay.setPlayerId(player.getId());
        cardPlay.setMovementCard(movementCard);

        List<ViewStep> viewSteps = new ArrayList<>();
        switch (movementCard.getMovement()) {
            case MOVE1:
                List<BoardAction> boardActions = moveForward(player, 1);
                cardPlay.addBoardActions(boardActions);
                break;
            case MOVE2:
                //viewSteps.addAll(moveForward(player, 2));
                break;
            case MOVE3:
                //viewSteps.addAll(moveForward(player, 3));
                break;
            case TURN_RIGHT:
                viewSteps.add(turnRight(player));
                break;
            case TURN_LEFT:
                viewSteps.add(turnLeft(player));
                break;
            case UTURN:
                viewSteps.add(uturn(player));
                break;
            case BACKUP:
                viewSteps.addAll(backup(player));
                break;
            default:
                return new ArrayList<>();
        }
        gameReport.addPlay(cardPlay);
        return viewSteps;
    }

    public PitKillAction pitKillPlayer(Player player, Position pitLocation) {
        Position endPosition = new Position(getPositionOfElement(player.getBeacon()));
        player.setPosition(endPosition);

        PitKillAction pitKillAction = new PitKillAction(player.getId(), pitLocation, player.getPosition());
        log.info("Player " + player.getId() + " died. Resetting to " + player.getPosition());
        return pitKillAction;
    }

    //TODO: LOGIC INVOLVING THIS USUALLY CHECK ENDPOSITION, NOT ENTIRE PATHWAY
    public boolean isPit(Position position) {
        Tile tile = getTileAtPosition(position);
        return tile.hasElement(ElementEnum.PIT);
    }

    public List<BoardAction> moveForward(Player player, int distance) {
        List<BoardAction> boardActions = new ArrayList<>();

        Position startPosition = player.getPosition();
        Position endPosition = startPosition.moveForward(player.getFacing(), distance);
        player.setPosition(endPosition);


        RobotMoveAction robotMoveAction = new RobotMoveAction(player.getId(), startPosition, endPosition);
        boardActions.add(robotMoveAction);

        //logic is wrong. Implement a pit-checker for every square the robot moves over
//        if (isPit(endPosition)) {
//            PitKillAction pitKillAction = pitKillPlayer(player, endPosition);
//            boardActions.add(pitKillAction);
//        }


        return boardActions;
    }

    public List<ViewStep> backup(Player player) {
        List<ViewStep> viewSteps = new ArrayList<>();
        Position startPosition = player.getPosition();
        Position endPosition = startPosition.moveBackward1(player.getFacing());

        if (isWallBetween(startPosition, endPosition)) {
            log.info("Robot belonging to player " + player + " hit a wall instead of move1", player.getId());
            return null;
        } else {
            player.setPosition(endPosition);
//            if (isPit(player.getPosition())) {
//                //viewSteps.add(pitKillPlayer(player));
//            }

            viewSteps.add(new RobotMoveViewStep(player, startPosition, endPosition, player.getFacing(), player.getFacing(), MovementMethod.MOVE));
            return viewSteps;
        }
    }

    public ViewStep uturn(Player player) {
        Direction startFacing = player.getFacing();
        player.setFacing(Direction.turnLeft(Direction.turnLeft(player.getFacing())));
        ViewStep viewStep = new RobotMoveViewStep(player, player.getPosition(), player.getPosition(), startFacing, player.getFacing(), MovementMethod.TURN);
        return viewStep;
    }

    public ViewStep turnLeft(Player player) {
        Direction startFacing = player.getFacing();
        player.setFacing(Direction.turnLeft(player.getFacing()));
        ViewStep viewStep = new RobotMoveViewStep(player, player.getPosition(), player.getPosition(), startFacing, player.getFacing(), MovementMethod.TURN);
        return viewStep;
    }

    public ViewStep turnRight(Player player) {
        Direction startFacing = player.getFacing();
        player.setFacing(Direction.turnRight(player.getFacing()));
        ViewStep viewStep = new RobotMoveViewStep(player, player.getPosition(), player.getPosition(), startFacing, player.getFacing(), MovementMethod.TURN);
        return viewStep;
    }

    public Position getRandomEmptySquare(Random random) {
        for (int i = 0; i < 500; i++) {   //retry count
            int x = random.nextInt(squares.size());
            int y = random.nextInt(squares.get(x).size());

            if(squares.get(x).containsKey(y)) {
                if (squares.get(x).get(y).getElements().isEmpty()) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    public Position getPositionOfCheckpoint(int checkpointIndex){
        Optional<Map.Entry<Element, Position>> element = elementPositions.entrySet().stream()
                .filter(entry -> entry.getKey().getClass() == Checkpoint.class)
                .filter(entry -> ((Checkpoint) entry.getKey()).getBase1index() == checkpointIndex)
                .findFirst();
        if(element.isPresent()){
            return element.get().getValue();
        } else {
            log.trace("Position not found for checkpoint"+checkpointIndex);
            return null;
        }
    }

    //TODO: needs to detect other walls
    public boolean isWallBetween(Position startPosition, Position endPosition) {
        /*
        if (startPosition.getX() == endPosition.getX()) {
            Position leftPosition = startPosition.getX() < endPosition.getX() ? startPosition : endPosition;
            for (int i = leftPosition.getX() + 1; i < endPosition.getX(); i++) {
                if (squares[leftPosition.getY()][i].hasElement(ElementEnum.WALL_LEFT)) {
                    return true;
                }
            }
        }
        if (startPosition.getY() == endPosition.getY()) {
            Position topPosition = startPosition.getY() < endPosition.getY() ? startPosition : endPosition;
            for (int i = topPosition.getY() + 1; i < endPosition.getY(); i++) {
                if (squares[i][topPosition.getX()].hasElement(ElementEnum.WALL_DOWN)) {
                    return true;
                }
            }
        }
        return false;
         */
        return false;
    }


}
