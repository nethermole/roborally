package com.nethermole.roborally.view;

import com.nethermole.roborally.Gamemaster;
import com.nethermole.roborally.game.Game;
import com.nethermole.roborally.game.board.Board;
import com.nethermole.roborally.game.board.Direction;
import com.nethermole.roborally.game.player.Player;
import lombok.Setter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class POCView extends AbstractView implements Runnable{

    private static int windowWidth = 1500;
    private static int windowHeight = 1500;
    private static int topOffset = 50;
    private static int sideOffset = 50;
    private static int gridSize = 100;

    JFrame jFrame;
    DrawingPanel drawingPanel;
    boolean running;

    Gamemaster gamemaster;
    private POCView(Gamemaster gamemaster){
        this.gamemaster = gamemaster;
    }

    @Override
    public Game getGame(){
        return gamemaster.getGame();
    }

    public void startViewing(){
        initializeView();
        running=true;
        while(running){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){}

            drawingPanel.setGridSize(gridSize);
            drawingPanel.repaint();
        }
    }

    public void stopViewing(){
        running = false;
    }


    private void initializeView() {
        jFrame = new JFrame("Wew");
        jFrame.setBounds(sideOffset, topOffset, windowWidth, windowHeight);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingPanel = new DrawingPanel(this.gamemaster, gridSize);
        jFrame.add(drawingPanel);

        jFrame.setVisible(true);
    }

    @Override
    public void run() {
        startViewing();
    }


    public static POCView startPOCView(Gamemaster gamemaster){
        POCView pocView = new POCView(gamemaster);
        Thread thread = new Thread(pocView);
        thread.start();
        return pocView;
    }

    private class DrawingPanel extends JPanel {
        Gamemaster gamemaster;

        @Setter
        private int gridSize;

        @Override
        public void paintComponent(Graphics graphics){
            super.paintComponent(graphics);

            drawGrid(graphics);
            for(Player player : gamemaster.getPlayers()){
                drawRobot(graphics, Color.BLACK, player);
            }
        }

        private void drawGrid(Graphics graphics){
            try {
                Board board = gamemaster.getBoard();
                int squaresX = board.getSquares().length;
                int squaresY = board.getSquares()[0].length;
                for(int x = 0; x<squaresX; x++){
                    for(int y = 0; y<squaresY; y++){
                        graphics.drawRect(x*gridSize, y*gridSize, gridSize, gridSize);
                    }
                }
            } catch(Exception e){
                //log game not started exception
            }
        }

        private void drawRobot(Graphics graphics, Color color, Player player) {
            graphics.setColor(color);
            if(player.hasPosition()) {
                int x = player.getPosition().getX() * gridSize;
                int y = player.getPosition().getY() * gridSize;

                graphics.drawRect(x, y, gridSize, gridSize);
                if (player.getFacing() == Direction.UP) {
                    graphics.drawLine(x + (gridSize / 2), y, x + (gridSize / 2), y + gridSize);
                    graphics.drawLine(x + (gridSize / 2), y, x, y + (gridSize / 2));
                    graphics.drawLine(x + (gridSize / 2), y, x + gridSize, y + (gridSize / 2));
                } else if (player.getFacing() == Direction.RIGHT) {
                    graphics.drawLine(x, y + (gridSize / 2), x + gridSize, y + (gridSize / 2));
                    graphics.drawLine(x + gridSize, y + (gridSize / 2), x + (gridSize / 2), y);
                    graphics.drawLine(x + gridSize, y + (gridSize / 2), x + (gridSize / 2), y + gridSize);
                } else if (player.getFacing() == Direction.DOWN) {
                    graphics.drawLine(x + (gridSize / 2), y, x + (gridSize / 2), y + gridSize);
                    graphics.drawLine(x + (gridSize / 2), y + gridSize, x, y + (gridSize / 2));
                    graphics.drawLine(x + (gridSize / 2), y + gridSize, x + gridSize, y + (gridSize / 2));
                } else if (player.getFacing() == Direction.LEFT) {
                    graphics.drawLine(x, y + (gridSize / 2), x + gridSize, y + (gridSize / 2));
                    graphics.drawLine(x, y + (gridSize / 2), x + (gridSize / 2), y);
                    graphics.drawLine(x, y + (gridSize / 2), x + (gridSize / 2), y + gridSize);
                }
            }

            graphics.drawChars(player.getFacing().toString().toCharArray(), 0, player.getFacing().toString().length(), 100, 100);
        }

        public DrawingPanel(Gamemaster gamemaster, int gridSize){
            this.gamemaster = gamemaster;
            this.gridSize = gridSize;
        }
    }

}
