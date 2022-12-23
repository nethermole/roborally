package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.GameConstants;
import com.nethermole.roborally.PlayerHandProcessor;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@NoArgsConstructor
public class Game {

    private static Logger log = LogManager.getLogger(Game.class);

    @Getter
    private Map<Integer, Player> players;

    @Setter
    private GameLog gameLog;

    @Getter
    @Setter
    private Board board;

    private MovementDeck movementDeck;

    @Getter
    private Map<Integer, List<MovementCard>> playersHands;

    @Getter
    private Map<Integer, List<MovementCard>> playerSubmittedHands;

    @Getter
    private Map<Integer, PlayerState> playerStates;

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

    private int maxTurn;

    @Setter
    private Random random;

    Game(Random random){
        this.random = random;
    }

    void initializeFields(Map<Integer, Player> players) {
        playerSubmittedHands = new HashMap<>();
        playersHands = new HashMap<>();
        playerStates = new HashMap<>();

        currentTurn = 0;
        maxTurn = 5000;

        gameState = GameState.STARTING;
        movementDeck = new MovementDeck(random);
        this.players = players;

        for (Player player : players.values()) {
            playerStates.put(player.getId(), PlayerState.NO_INTERACTION_YET);
            player.setFacing(Direction.UP);
        }

        //is the below necessary?
        for (Player player : players.values()) {
            playersHands.put(player.getId(), new ArrayList<>());
        }
    }

    public Position getStartPosition() {
        return board.getPositionOfElement(startBeacon);
    }

    public void distributeCards() {
        playerSubmittedHands = new HashMap<>();
        for (Integer playerId : playersHands.keySet()) {
            List<MovementCard> hand = new ArrayList<>();
            for (int i = 0; i < players.get(playerId).getHealth(); i++) {
                hand.add(movementDeck.drawCard());
            }
            playersHands.put(playerId, hand);
        }
    }

    public List<MovementCard> getHand(int playerId) {
        if(playerStates.get(playerId) != PlayerState.HAS_SUBMITTED_CARDS){
            playerStates.put(playerId, PlayerState.CHOOSING_MOVEMENT_CARDS);
        }

        return playersHands.get(playerId);
    }

    public void submitPlayerHand(int playerId, List<MovementCard> movementCardList) throws InvalidSubmittedHandException {
        log.info("Player " + players.get(playerId).getName() + " submitted hand: " + movementCardList);

        List<MovementCard> originalPlayerHandCopy = new ArrayList<>(playersHands.get(playerId));
        for(MovementCard movementCard : movementCardList){
            boolean playerWasDealtSubmittedCard = originalPlayerHandCopy.remove(movementCard);
            if(!playerWasDealtSubmittedCard){
                throw new InvalidSubmittedHandException(playersHands.get(playerId), movementCardList, playerId, this);
            }
        }

        playerStates.put(playerId, PlayerState.HAS_SUBMITTED_CARDS);
        playersHands.put(playerId, new ArrayList<>());
        playerSubmittedHands.put(playerId, movementCardList);
    }

    public void processTurn() {
        PlayerHandProcessor playerHandProcessor = new PlayerHandProcessor();
        List<List<MovementCard>> organizedPlayerHands = playerHandProcessor.submitHands(playerSubmittedHands);
        for (int i = 0; i < GameConstants.PHASES_PER_TURN; i++) {
            processPhase(organizedPlayerHands.get(i), playerHandProcessor);
            checkForWinner();
            if(currentTurn > maxTurn){
                winningPlayer = Player.instance();
            }
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
        try{
            npcPlayersSelectCards();
        } catch (InvalidSubmittedHandException e){
            log.error(e.getMessage());
            throw new IllegalStateException("npcPlayersSelectCards() submitted invalidCards");
        }
    }

    public void processPhase(List<MovementCard> movementCards, PlayerHandProcessor playerHandProcessor) {
        for (MovementCard movementCard : movementCards) {
            int playerPlayerId = playerHandProcessor.getPlayerWhoSubmittedCard(movementCard);

            Player player = players.get(playerPlayerId);
            List<ViewStep> viewSteps = board.movePlayer(player, movementCard.getMovement());

            Optional checkpoint = board.getTileAtPosition(player.getPosition()).getElements().stream().filter(element -> element.getClass() == Checkpoint.class).findFirst();
            if(checkpoint.isPresent()){
                player.touchCheckpoint((Checkpoint) checkpoint.get());
            }

            gameLog.addViewSteps(currentTurn, viewSteps);
        }
    }

    public void checkForWinner() {
        for (Player player : players.values()) {
            if(player.getMostRecentCheckpointTouched() == checkPoints.get(checkPoints.size() - 1).getIndex()){
                winningPlayer = player;
                log.info("Player " + player.getId() + " won the game!");
            }
        }
    }

    public void npcPlayersSelectCards() throws InvalidSubmittedHandException {
        for (Player player : players.values()) {
            if (player instanceof NPCPlayer) {
                List<MovementCard> npcPlayerCards = playersHands.get(player.getId());

                List<MovementCard> selectedCards = ((NPCPlayer) player).chooseCards(npcPlayerCards);
                submitPlayerHand(player.getId(), selectedCards);
            }
        }
    }

    public boolean isReadyToProcessTurn() {
        return playerStates.values().stream().allMatch(it -> it == PlayerState.HAS_SUBMITTED_CARDS);
    }

    public Player getPlayer(int id) {
        return players.get(id);
    }

}
