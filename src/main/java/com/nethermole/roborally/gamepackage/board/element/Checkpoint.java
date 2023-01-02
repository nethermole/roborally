package com.nethermole.roborally.gamepackage.board.element;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Checkpoint extends Element{

    @Getter
    private int base1index;

    @Override
    public ElementEnum getElementEnum() {
        String elementEnum = "CHECKPOINT" + base1index;
        return ElementEnum.valueOf(elementEnum);
    }
}
