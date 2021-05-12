package com.nethermole.roborally.game.board.element;


import java.awt.Color;
import java.awt.Graphics;

public class Pit extends Element {

    @Override
    public ElementEnum getElementEnum() {
        return ElementEnum.PIT;
    }

    @Override
    public void drawSelf(int startX, int startY, int gridSize, Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(startX, startY, gridSize, gridSize);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(startX + 2, startY + 2, gridSize - 4, gridSize - 4);
    }

}
