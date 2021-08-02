package com.nethermole.roborally.gamepackage.board.element;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Checkpoint extends Element{

    int index;

    @Override
    public ElementEnum getElementEnum() {
        String elementEnum = "CHECKPOINT" + index;
        return ElementEnum.valueOf(elementEnum);
    }
}
