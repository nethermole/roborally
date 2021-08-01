package com.nethermole.roborally.view;

import com.nethermole.roborally.gamepackage.Game;

public abstract class AbstractView {

    public abstract Game getGame();

    public abstract void startViewing();

    public abstract void stopViewing();

}
