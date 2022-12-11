package com.nethermole.roborally.gamepackage.deck.movement;

public enum Movement {

    MOVE1("M1"),
    MOVE2("M2"),
    MOVE3("M3"),
    TURN_RIGHT("TR"),
    TURN_LEFT("TL"),
    UTURN("UT"),
    BACKUP("BU");

    String string;

    Movement(String label){
        string = label;
    }
}
