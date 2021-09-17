package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.ViewStep;
import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.board.element.ElementEnum;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.turn.MovementMethod;
import com.nethermole.roborally.gamepackage.turn.RobotMoveViewStep;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Slf4j
public class Board {

    @Getter
    private List<Player> players;

    @Getter
    private Tile[][] squares;

    private Map<Element, Position> elementPositions;

    public Board(int boardHeight, int boardWidth) {
        elementPositions = new HashMap<>();
        squares = new Tile[boardHeight][boardWidth];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                squares[i][j] = new Tile();
            }
        }
        players = new ArrayList<>();
    }

    public void addElement(Element element, Position position) {
        elementPositions.put(element, position);
        squares[position.getX()][position.getY()].addElement(element);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Position getPositionOfElement(Element element) {
        return elementPositions.get(element);
    }

    //break this out into a movement logic class
    public List<ViewStep> movePlayer(Player player, Movement movement) {
        List<ViewStep> viewSteps = new ArrayList<>();
        switch (movement) {
            case MOVE1:
                viewSteps.addAll(move1(player));
                break;
            case MOVE2:
                viewSteps.addAll(move1(player));
                viewSteps.addAll(move1(player));
                break;
            case MOVE3:
                viewSteps.addAll(move1(player));
                viewSteps.addAll(move1(player));
                viewSteps.addAll(move1(player));
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
                viewSteps.add(backup(player));
                break;
            default:
                return new ArrayList<>();
        }
        return viewSteps;
    }

    public ViewStep resetPlayer(Player player) {
        Position currentPosition = new Position(player.getPosition());
        Position endPosition = new Position(getPositionOfElement(player.getBeacon()));

        ViewStep moveEvent = new RobotMoveViewStep(player, currentPosition, endPosition, player.getFacing(), player.getFacing(), MovementMethod.PIT_DEATH);

        player.setPosition(getPositionOfElement(player.getBeacon()));
        System.out.println("Player " + player.getId() + " died. Resetting to " + player.getPosition());
        return moveEvent;
    }

    public boolean isPositionInSquares(Position position) {
        return
                position.getX() >= 0 &&
                        position.getY() >= 0 &&
                        position.getX() < squares.length &&
                        position.getY() < squares[0].length;
    }

    public boolean isOverPit(Position position) {
        if (!isPositionInSquares(position)) {
            return true;
        }

        Set<Element> elements = squares[position.getX()][position.getY()].getElements();
        for (Element element : elements) {
            if (element.getElementEnum() == ElementEnum.PIT) {
                return true;
            }
        }
        return false;
    }

    public List<ViewStep> move1(Player player) {
        List<ViewStep> viewSteps = new ArrayList<>();
        Position startPosition = player.getPosition();
        Position endPosition = startPosition.moveForward1(player.getFacing());

        if (isWallBetween(startPosition, endPosition)) {
            log.info(player + " hit a wall instead of move1", player.getId());
            //TODO: nomove collision viewstep
            return null;
        } else {
            player.setPosition(endPosition);
            viewSteps.add(new RobotMoveViewStep(player, startPosition, endPosition, player.getFacing(), player.getFacing(), MovementMethod.MOVE));

            if (isOverPit(player.getPosition())) {
                viewSteps.add(resetPlayer(player));
            }
            return viewSteps;
        }
    }

    public ViewStep backup(Player player) {
        Position startPosition = player.getPosition();
        Position endPosition = startPosition.moveBackward1(player.getFacing());

        if (isWallBetween(startPosition, endPosition)) {
            log.info("Robot belonging to player " + player + " hit a wall instead of move1", player.getId());
            return null;
        } else {
            player.setPosition(endPosition);
            ViewStep viewStep = new RobotMoveViewStep(player, startPosition, endPosition, player.getFacing(), player.getFacing(), MovementMethod.MOVE);
            return viewStep;
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

    public Position getRandomEmptySquare() {
        Random random = new Random();
        Position emptySquare = null;
        while (emptySquare == null) {
            int x = random.nextInt(squares.length);
            int y = random.nextInt(squares[0].length);
            Tile tile = squares[x][y];
            if (tile.isEmpty()) {
                emptySquare = new Position(x, y);
            }
        }
        return emptySquare;
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
