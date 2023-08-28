package com.nethermole.roborally;

import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.gamepackage.player.PlayerState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.nethermole.roborally.gamepackage.player.PlayerState.CHOOSING_MOVEMENT_CARDS;
import static com.nethermole.roborally.gamepackage.player.PlayerState.HAS_SUBMITTED_CARDS;
import static com.nethermole.roborally.gamepackage.player.PlayerState.NEEDS_TO_GET_HAND;
import static com.nethermole.roborally.gamepackage.player.PlayerState.NO_INTERACTION_YET;

public class PlayerStatusManager {

    private final Map<Integer, PlayerState> playerStates;

    private final static Logger log = LogManager.getLogger(PlayerStatusManager.class);

    public PlayerStatusManager(){
        playerStates = new HashMap<>();
    }

    public void addPlayer(int playerId){
        if(playerStates.containsKey(playerId)){
            log.warn("PlayerStatusManager already contains id:" + playerId + ". No player added");
        } else{
            log.trace("Putting playerState (" + playerId + ", " + NO_INTERACTION_YET + ")");
            playerStates.put(playerId, NO_INTERACTION_YET);
        }
    }

    public PlayerState getPlayerState(int playerId){
        return playerStates.get(playerId);
    }

    public void playerGetsHand(int playerId) throws InvalidPlayerStateException {
        log.trace("playerGetsHand(" + playerId + ") ");

        Set<PlayerState> validInitialStates = new HashSet<>();
        validInitialStates.add(NO_INTERACTION_YET);
        validInitialStates.add(NEEDS_TO_GET_HAND);
        validInitialStates.add(CHOOSING_MOVEMENT_CARDS);

        PlayerState playerState = playerStates.get(playerId);
        if(validInitialStates.contains(playerState)){
            playerStates.put(playerId, CHOOSING_MOVEMENT_CARDS);
        } else {
            log.info((new InvalidPlayerStateException(playerId, playerState.name(), "player gets hand")).getMessage());
            return;
        }
    }

    public void playerSubmitsHand(int playerId) throws InvalidPlayerStateException {
        log.trace("playerSubmitsHand(" + playerId + ") ");

        Set<PlayerState> validInitialStates = new HashSet<>();
        validInitialStates.add(CHOOSING_MOVEMENT_CARDS);

        PlayerState playerState = playerStates.get(playerId);
        if(validInitialStates.contains(playerState)){
            playerStates.put(playerId, HAS_SUBMITTED_CARDS);
        } else {
            throw new InvalidPlayerStateException(playerId, playerState.name(), "player gets hand");
        }
    }

    public void setupForNextTurn(){
        playerStates.entrySet().forEach(entry -> entry.setValue(NEEDS_TO_GET_HAND));
    }

    public boolean allPlayersHaveSubmittedHands() {
        return playerStates.values().stream().allMatch(it -> it == PlayerState.HAS_SUBMITTED_CARDS);
    }


}
