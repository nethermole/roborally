package com.nethermole.roborally.gamepackage.board.element;

import java.awt.Graphics;

public abstract class Element {
    public abstract ElementEnum getElementEnum();

    public void drawSelf(int startX, int startY, int gridSize, Graphics graphics) {
    }
}
