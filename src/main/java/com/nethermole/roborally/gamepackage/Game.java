package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.board.element.Pit;
import com.nethermole.roborally.gamepackage.deck.GameState;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.deck.movement.MovementDeck;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.PlayerState;
import com.nethermole.roborally.gameservice.GameLog;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    @Getter
    @Setter
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

    private Map<Integer, Player> players;

    @Getter
    private int currentTurn;

    GameLog gameLog;

    public Game(Map<Integer, Player> players, GameLog gameLog, Position startLocation) {
        this.players = players;
        this.gameLog = gameLog;
        gameState = GameState.STARTING;
        playersHands = new HashMap<>();
        for(Player player : players.values()){
            playersHands.put(player, new ArrayList<>());
        }

        playerStates = new HashMap<>();
        for (Player player : players.values()) {
            playerStates.put(player, PlayerState.NO_INTERACTION_YET);
        }
        playerSubmittedHands = new HashMap<>();

        //todo startLocation as board element?
        for (Player player : players.values()) {
            playersHands.put(player, new ArrayList<>());
            player.setPosition(startLocation);
            player.setRespawnPosition(startLocation);
        }

        movementDeck = new MovementDeck();
        currentTurn = 0;
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
                List<ViewStep> viewSteps = board.movePlayer(playerByMovementCard.get(movementCard), movementCard.getMovement());
                gameLog.addViewSteps(currentTurn, viewSteps);
            }
        }
        currentTurn++;
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

    private void validateMovementCards() {
    }

}
