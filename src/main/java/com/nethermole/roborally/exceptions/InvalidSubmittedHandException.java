package com.nethermole.roborally.exceptions;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InvalidSubmittedHandException extends Exception {
    List<MovementCard> handDealt;
    List<MovementCard> handSubmitted;
    Player player;

    @Override
    public String getMessage(){
        return "--- InvalidSubmittedHandException ---\n" +
                "Player:\t"+player.getName()+"\n" +
                "Dealt:\t"+handDealt.toString()+"\n" +
                "Submitted:\t"+handSubmitted.toString();
    }
}
