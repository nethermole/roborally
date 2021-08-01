package com.nethermole.roborally.gamepackage.board.element;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Wrench extends Element {

    int healAmount;

    public Wrench(){
        healAmount = 1;
    }

    @Override
    public ElementEnum getElementEnum() {
        return ElementEnum.valueOf("WRENCH_"+healAmount);
    }
}
