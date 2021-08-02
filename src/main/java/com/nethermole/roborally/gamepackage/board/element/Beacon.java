package com.nethermole.roborally.gamepackage.board.element;

import com.nethermole.roborally.gamepackage.board.Position;
import lombok.Getter;

public class Beacon extends Element{

    @Getter
    private Position position;

    @Getter
    private int playerId;

    public Beacon(int playerId){
        if(playerId < 0){
            throw new RuntimeException("Invalid beacon playerId: " + playerId);
        }
    }

    @Override
    public ElementEnum getElementEnum() {
        String elementEnum = "BEACON";
        if(playerId != -1){
            elementEnum += playerId;
        }
        return ElementEnum.valueOf(elementEnum);
    }

    public static Beacon startBeacon(Position startPosition){
        Beacon beacon = new Beacon(0);
        beacon.position = startPosition;
        beacon.playerId = -1;
        return beacon;
    }
}
