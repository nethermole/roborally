package com.nethermole.roborally.gamepackage.board.element;

public abstract class Element {
    public abstract ElementEnum getElementEnum();

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
