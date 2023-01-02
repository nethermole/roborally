package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.GameConstants;
import com.nethermole.roborally.PlayerHandProcessor;
import com.nethermole.roborally.PlayerStatusManager;
import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.deck.GameState;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.deck.movement.MovementDeck;
import com.nethermole.roborally.gamepackage.player.Player;
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
    private PlayerStatusManager playerStatusManager;

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
        playerStatusManager = new PlayerStatusManager();

        currentTurn = 0;
        maxTurn = 5000;

        gameState = GameState.STARTING;
        movementDeck = new MovementDeck(random);
        this.players = players;

        for (Player player : players.values()) {
            playerStatusManager.addPlayer(player.getId());
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

    public List<MovementCard> getHand(int playerId) throws InvalidPlayerStateException {
        //todo: guard against this better
        for(int timeoutSeconds = 5; gameState == GameState.PROCESSING_TURN || gameState == GameState.TURN_PREPARATION; timeoutSeconds--){
            try {
                Thread.sleep(1000);
            } catch(Exception e){
                log.error("sleep() exception... how tho");
            }
            if(timeoutSeconds == 0){
                log.warn("timed out getting hand for playerId: " + playerId);
                return null;
            }
        };

        playerStatusManager.playerGetsHand(playerId);

        return playersHands.get(playerId);
    }

    public void submitPlayerHand(int playerId, List<MovementCard> movementCardList) throws InvalidSubmittedHandException, InvalidPlayerStateException {
        log.info("PlayerId " + playerId + " submitted hand: " + movementCardList + " for turn " + currentTurn);

        List<MovementCard> originalPlayerHandCopy = new ArrayList<>(playersHands.get(playerId));
        for(MovementCard movementCard : movementCardList){
            boolean playerWasDealtSubmittedCard = originalPlayerHandCopy.remove(movementCard);
            if(!playerWasDealtSubmittedCard){
                throw new InvalidSubmittedHandException(playersHands.get(playerId), movementCardList, playerId, this);
            }
        }

        playerStatusManager.playerSubmitsHand(playerId);

        playersHands.put(playerId, new ArrayList<>());
        playerSubmittedHands.put(playerId, movementCardList);
    }

    public boolean isReadyToProcessTurn(){
        return winningPlayer == null && playerStatusManager.readyToProcessTurn();
    }

    public void processTurn() {
        gameState = GameState.PROCESSING_TURN;

        PlayerHandProcessor playerHandProcessor = new PlayerHandProcessor();
        List<List<MovementCard>> organizedPlayerHands = playerHandProcessor.submitHands(playerSubmittedHands);
        log.info("Processing turn: " + currentTurn);
        for (int i = 0; i < GameConstants.PHASES_PER_TURN; i++) {
            log.trace("Processing turn: " + currentTurn + ", phase: " + (i+1));
            processPhase(organizedPlayerHands.get(i), playerHandProcessor);
            log.trace("Done processing turn: " + currentTurn + ", phase: " + (i+1));
            checkForWinner();
            if(currentTurn > maxTurn){
                winningPlayer = Player.instance();
            }
            if (winningPlayer != null) {
                log.info("Winning player found");
            }
        }
        log.info("Done processing turn: " + currentTurn);
        incrementCurrentTurn();
    }

    public void incrementCurrentTurn() {
        log.info("Incrementing turn from " + currentTurn + " to " + (currentTurn+1));
        currentTurn++;
    }

    public void setupForNextTurn() {
        if(winningPlayer != null){
            return;
        }

        log.info("Preparing for turn: " + currentTurn);

        this.gameState = GameState.TURN_PREPARATION;
        playerStatusManager.setupForNextTurn();

        movementDeck = new MovementDeck(random);
        distributeCards();
        try{
            npcPlayersSelectCards();
        } catch (InvalidSubmittedHandException e){
            log.error(e.getMessage());
            throw new IllegalStateException("npcPlayersSelectCards() submitted invalidCards");
        } catch (InvalidPlayerStateException e){
            log.error(e.getMessage());
            throw new IllegalStateException("npcPlayersSelectCards() threw InvalidPlayerStateException");
        }

        this.gameState = GameState.DONE_PREPARING;
        log.info("Done preparing for turn: " + currentTurn);
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
            if(player.getMostRecentCheckpointTouched() == checkPoints.size()){
                winningPlayer = player;
                log.info("Player " + player.getId() + " won the game!");
            }
        }
    }

    public void npcPlayersSelectCards() throws InvalidSubmittedHandException, InvalidPlayerStateException {
        log.info("npcPlayersSelectCards()");

        for (Player player : players.values()) {
            if (player instanceof NPCPlayer) {
                playerStatusManager.playerGetsHand(player.getId());
                List<MovementCard> npcPlayerCards = playersHands.get(player.getId());

                List<MovementCard> npcPlayerCardsCopy = new ArrayList<>();
                for(MovementCard movementCard : npcPlayerCards){
                    npcPlayerCardsCopy.add(new MovementCard(movementCard));
                }
                List<MovementCard> selectedCards = null;
                try {
                    selectedCards = ((NPCPlayer) player).chooseCards(npcPlayerCardsCopy, getBoard());
                } catch(Exception e){
                    System.out.println();
                }
                submitPlayerHand(player.getId(), selectedCards);
            }
        }
    }

    public Player getPlayer(int id) {
        return players.get(id);
    }

}
