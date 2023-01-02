package com.nethermole.roborally.exceptions;

public class InvalidPlayerStateException extends Exception {

    @Override
    public String getMessage(){
        return "--- InvalidSubmittedHandException ---\n" +
                "Player:\t" + game.getPlayer(playerId).getName()+"\n" +
                "Dealt:\t" + handDealt.toString()+"\n" +
                "Submitted:\t" + handSubmitted.toString();
    }
}
