package com.nethermole.roborally.gamepackage.board.element;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Beacon extends Element {

    @Getter
    private int playerId;

    @Override
    public ElementEnum getElementEnum() {
        String elementEnum = "BEACON";
        if (playerId != -1) {
            elementEnum += playerId;
        }
        return ElementEnum.valueOf(elementEnum);
    }

    public static Beacon startBeacon() {
        Beacon beacon = new Beacon(-1);
        return beacon;
    }
}
