package com.nethermole.roborally.view;

import com.nethermole.roborally.game.Game;

public abstract class AbstractView {

    public abstract Game getGame();
    public abstract void startViewing();
    public abstract void stopViewing();

}
