package com.nethermole.roborally.game;

import lombok.Getter;

public abstract class Player {

    public int id;

    @Getter
    public Robot robot;

    public abstract void giveCards();

    public abstract void getCards();
}
