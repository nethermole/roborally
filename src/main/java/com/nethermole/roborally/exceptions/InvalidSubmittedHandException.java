package com.nethermole.roborally.exceptions;

import com.nethermole.roborally.gamepackage.Game;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InvalidSubmittedHandException extends Exception {
    List<MovementCard> handDealt;
    List<MovementCard> handSubmitted;
    Integer playerId;

    Game game;

    @Override
    public String getMessage(){
        return "--- InvalidSubmittedHandException ---\n" +
                "Player:\t" + game.getPlayer(playerId).getDisplayName()+"\n" +
                "Dealt:\t" + handDealt.toString()+"\n" +
                "Submitted:\t" + handSubmitted.toString();
    }
}
