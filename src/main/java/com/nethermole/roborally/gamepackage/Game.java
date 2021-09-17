package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.deck.GameState;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.deck.movement.MovementDeck;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.PlayerState;
import com.nethermole.roborally.gamepackage.player.bot.NPCPlayer;
import com.nethermole.roborally.gameservice.GameLog;
import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private List<Checkpoint> checkPoints;

    private Map<Integer, Player> players;

    @Getter
    Player winningPlayer;

    @Getter
    private int currentTurn;

    GameLog gameLog;

    public Game(Map<Integer, Player> players, GameLog gameLog) {
        this.players = players;
        this.gameLog = gameLog;
        gameState = GameState.STARTING;
        playersHands = new HashMap<>();
        for (Player player : players.values()) {
            playersHands.put(player, new ArrayList<>());
        }

        playerStates = new HashMap<>();
        for (Player player : players.values()) {
            playerStates.put(player, PlayerState.NO_INTERACTION_YET);
        }
        playerSubmittedHands = new HashMap<>();

        for (Player player : players.values()) {
            playersHands.put(player, new ArrayList<>());
        }

        movementDeck = new MovementDeck();
        currentTurn = 0;
    }

    public Position getStartPosition(){
        return board.getPositionOfElement(checkPoints.get(0));
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
                List<ViewStep> viewSteps = board.movePlayer(playerByMovementCard.get(movementCard), movementCard.getMovement());
                gameLog.addViewSteps(currentTurn, viewSteps);
                checkForWinner();
                if(winningPlayer != null){
                    currentTurn++;
                    return;
                }
            }
        }
        currentTurn++;
        this.gameState = GameState.TURN_PREPARATION;
        distributeCards();
        npcPlayersSelectCards();
    }

    public void checkForWinner(){
        for(Player player : players.values()){
            Position position = player.getPosition();
            if(positionInSquares(position)) {
                Set<Element> elements = board.getSquares()[position.getX()][position.getY()].getElements();
                if (elements.contains(checkPoints.get(checkPoints.size() - 1))) {
                    winningPlayer = player;
                    System.out.println("Player " + player.getId() + " won the game!");
                }
            }
        }
    }

    public boolean positionInSquares(Position position){
        return
                position.getX() >=0 &&
                position.getY() >=0 &&
                position.getX() < board.getSquares().length &&
                position.getX() < board.getSquares()[0].length;
    }

    public void npcPlayersSelectCards(){
        for(Player player : players.values()){
            if(player instanceof NPCPlayer){
                NPCPlayer npcPlayer = (NPCPlayer) player;
                List<MovementCard> npcPlayerCards = playersHands.get(player);
                List<MovementCard> selectedCards = npcPlayer.chooseCards(npcPlayerCards);
                submitPlayerHand(player, selectedCards);
            }
        }
    }

    public boolean isReadyToProcessTurn() {
        for (Map.Entry<Player, PlayerState> player : playerStates.entrySet()) {
            if (player.getValue() != PlayerState.HAS_SUBMITTED_CARDS) {
                return false;
            }
        }
        return true;
    }

    public void setBoards(List<Board> boards, List<Position> checkpointPositions) {
        this.checkPoints = new ArrayList<>();

        if (boards.size() != 1) {
            throw new NotImplementedException();
        }
        this.board = boards.get(0);
        for(int i = 0; i < checkpointPositions.size() ; i++){
            Checkpoint checkpoint = new Checkpoint(i);
            checkPoints.add(checkpoint);
            board.addElement(checkpoint, checkpointPositions.get(i));
        }

        Beacon startBeacon = Beacon.startBeacon(checkpointPositions.get(0));
        board.addElement(startBeacon, startBeacon.getPosition());
        for(Player player : players.values()){
            player.setBeacon(startBeacon);
            player.setPosition(startBeacon.getPosition());
            player.setFacing(Direction.UP);
        }
    }

}
