package com.nethermole.roborally.gamepackage.action;

public abstract class BoardAction {
    private String type;

    protected BoardAction(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
