package com.nethermole.roborally.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidPlayerStateException extends Exception {

    int playerId;
    String initialState;
    String attemptedAction;

    @Override
    public String getMessage(){
        return "--- InvalidPlayerStateException ---\n" +
                "PlayerId:\t" + playerId+"\n" +
                "Initial State:\t" + initialState +"\n" +
                "Attempted Action:\t\"" + attemptedAction + "\"";
    }
}
