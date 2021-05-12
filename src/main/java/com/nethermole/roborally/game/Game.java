package com.nethermole.roborally.game;

import com.nethermole.roborally.game.board.Board;
import com.nethermole.roborally.game.board.Coordinate;
import com.nethermole.roborally.game.board.element.Element;
import com.nethermole.roborally.game.board.element.Pit;
import com.nethermole.roborally.game.deck.GameState;
import com.nethermole.roborally.game.deck.movement.MovementCard;
import com.nethermole.roborally.game.deck.movement.MovementDeck;
import com.nethermole.roborally.game.player.Player;
import com.nethermole.roborally.game.player.PlayerState;
import com.nethermole.roborally.logs.GameEventLogger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    @Getter
    private Board board;
    private MovementDeck movementDeck;
    @Getter
    private Map<Player, List<MovementCard>> playersHands;
    @Getter
    private Map<Player, List<MovementCard>> playerSubmittedHands;
    @Getter
    private Map<Player, PlayerState> playerStates;
    @Getter
    private GameState gameState;
    private GameEventLogger gameEventLogger;

    public Game(List<Player> players, int boardWidth, int boardHeight, GameEventLogger gameEventLogger) {
        gameState = GameState.STARTING;
        playersHands = new HashMap<>();
        playerStates = new HashMap<>();
        for (Player player : players) {
            playerStates.put(player, PlayerState.NO_INTERACTION_YET);
        }
        playerSubmittedHands = new HashMap<>();

        //todo startLocation as board element?
        Coordinate startLocation = new Coordinate(4, 4);
        for (Player player : players) {
            playersHands.put(player, new ArrayList<>());
            player.setPosition(startLocation);
        }

        this.gameEventLogger = gameEventLogger;
        movementDeck = new MovementDeck();
    }

    public void distributeCards() {
        for (Player player : playersHands.keySet()) {
            List<MovementCard> hand = new ArrayList<>();
            for (int i = 0; i < player.getHealth(); i++) {
                hand.add(movementDeck.drawCard());
            }
            playersHands.put(player, hand);
        }
    }

    public List<MovementCard> getHand(Player player) {
        playerStates.put(player, PlayerState.CHOOSING_MOVEMENT_CARDS);
        return playersHands.get(player);
    }

    public void submitPlayerHand(Player player, List<MovementCard> movementCardList) {
        playerStates.put(player, PlayerState.HAS_SUBMITTED_CARDS);

        validateMovementCards();
        playersHands.put(player, new ArrayList<>());
        playerSubmittedHands.put(player, movementCardList);
    }

    //todo untangle this, put it under test
    public void processTurn() {
        Map<MovementCard, Player> playerByMovementCard = new HashMap<>();

        //gather the cards
        List<List<MovementCard>> cardsByPriorityByPhase = new ArrayList<>();
        for (int phase = 0; phase < 5; phase++) { //5 is the number of movement cards we recieve from players
            List<MovementCard> cardsThisTurn = new ArrayList<>();
            for (Map.Entry<Player, List<MovementCard>> entry : playerSubmittedHands.entrySet()) {
                MovementCard movementCard = entry.getValue().get(phase);
                cardsThisTurn.add(movementCard);

                playerByMovementCard.put(movementCard, entry.getKey());
            }
            cardsThisTurn.sort(Comparator.comparing(MovementCard::getPriority));
            Collections.reverse(cardsThisTurn);
            cardsByPriorityByPhase.add(cardsThisTurn);
        }

        for (List<MovementCard> movementCardsThisTurn : cardsByPriorityByPhase) {
            //perform movement actions
            for (MovementCard movementCard : movementCardsThisTurn) {
                board.movePlayer(playerByMovementCard.get(movementCard), movementCard.getMovement());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
        this.gameState = GameState.TURN_PREPARATION;
        distributeCards();
    }

    public boolean isReadyToProcessTurn() {
        for (Map.Entry<Player, PlayerState> player : playerStates.entrySet()) {
            if (player.getValue() != PlayerState.HAS_SUBMITTED_CARDS) {
                return false;
            }
        }
        return true;
    }

    public void constructBoard(String boardname) {
        //io stuff to get specific board
        if (boardname.equals("Empty")) {
            constructEmptyBoard();
        } else {
            System.out.println("failed to construct board by name");
        }
        return;
    }

    private void validateMovementCards() {
    }

    private void constructBoard(HashMap<Element, Coordinate> boardElements, int boardHeight, int boardWidth, Coordinate startLocation, GameEventLogger gameEventLogger) {
        board = new Board(boardHeight, boardWidth, startLocation, gameEventLogger);

        for (Map.Entry<Element, Coordinate> boardElement : boardElements.entrySet()) {
            board.addElement(boardElement.getKey(), boardElement.getValue());
        }
    }

    private void constructEmptyBoard() {
        constructBoard(new HashMap<>(), 8, 8, new Coordinate(5, 5), new GameEventLogger());
        for (int i = 0; i < 8; i++) {
            board.getSquares()[i][0].addElement(new Pit());
            board.getSquares()[i][7].addElement(new Pit());
            board.getSquares()[0][i].addElement(new Pit());
            board.getSquares()[7][i].addElement(new Pit());
        }
    }

}
