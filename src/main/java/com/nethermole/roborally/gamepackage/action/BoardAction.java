package com.nethermole.roborally.gamepackage.action;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class BoardAction {
    private String type;

    protected BoardAction(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
