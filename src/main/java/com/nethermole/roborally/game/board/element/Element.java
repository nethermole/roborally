package com.nethermole.roborally.game.board.element;

import lombok.Getter;

import java.awt.*;

public abstract class Element {
    public abstract ElementEnum getElementEnum();
    public void drawSelf(int startX, int startY, int gridSize, Graphics graphics){}
}
