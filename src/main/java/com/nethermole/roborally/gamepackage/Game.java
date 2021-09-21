package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.GameConstants;
import com.nethermole.roborally.PlayerHandProcessor;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.Tile;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class Game {

    @Setter
    private Map<Integer, Player> players;

    @Setter
    private GameLog gameLog;

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

    @Setter
    private Beacon startBeacon;

    @Getter
    @Setter
    private List<Checkpoint> checkPoints;

    @Getter
    private Player winningPlayer;

    @Getter
    private int currentTurn;

    void initializeFields() {
        playerSubmittedHands = new HashMap<>();
        playersHands = new HashMap<>();
        playerStates = new HashMap<>();

        gameState = GameState.STARTING;
        movementDeck = new MovementDeck();
        currentTurn = 0;

        for (Player player : players.values()) {
            playerStates.put(player, PlayerState.NO_INTERACTION_YET);
            player.setFacing(Direction.UP);
        }

        //is the below necessary?
        for (Player player : players.values()) {
            playersHands.put(player, new ArrayList<>());
        }
    }

    public Position getStartPosition() {
        return board.getPositionOfElement(startBeacon);
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

    public void processTurn() {
        PlayerHandProcessor playerHandProcessor = new PlayerHandProcessor();
        List<List<MovementCard>> organizedPlayerHands = playerHandProcessor.submitHands(playerSubmittedHands);
        for (int i = 0; i < GameConstants.PHASES_PER_TURN; i++) {
            processPhase(organizedPlayerHands.get(i), playerHandProcessor);
            checkForWinner();
            if (winningPlayer != null) {
                break;
            }
        }
    }

    public void incrementCurrentTurn() {
        currentTurn++;
    }

    public void setupForNextTurn() {
        this.gameState = GameState.TURN_PREPARATION;
        distributeCards();
        npcPlayersSelectCards();
    }

    public void processPhase(List<MovementCard> movementCards, PlayerHandProcessor playerHandProcessor) {
        for (MovementCard movementCard : movementCards) {
            Player player = playerHandProcessor.getPlayerWhoSubmittedCard(movementCard);
            List<ViewStep> viewSteps = board.movePlayer(player, movementCard.getMovement());
            gameLog.addViewSteps(currentTurn, viewSteps);
        }
    }

    public void checkForWinner() {
        for (Player player : players.values()) {
            Tile tile = board.getTileAtPosition(player.getPosition());
            if (tile != null) {
                Set<Element> elements = tile.getElements();
                if (elements.contains(checkPoints.get(checkPoints.size() - 1))) {
                    winningPlayer = player;
                    System.out.println("Player " + player.getId() + " won the game!");
                }
            }
        }
    }

    public void npcPlayersSelectCards() {
        for (Player player : players.values()) {
            if (player instanceof NPCPlayer) {
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

    public Player getPlayer(int id) {
        return players.get(id);
    }

}
