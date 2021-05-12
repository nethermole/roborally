package com.nethermole.roborally.view;

import com.nethermole.roborally.GameLogistics;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.game.Game;
import com.nethermole.roborally.game.board.Board;
import com.nethermole.roborally.game.board.Direction;
import com.nethermole.roborally.game.board.Tile;
import com.nethermole.roborally.game.board.element.Element;
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

    GameLogistics gameLogistics;
    private POCView(GameLogistics gameLogistics){
        this.gameLogistics = gameLogistics;
    }

    @Override
    public Game getGame(){
        return gameLogistics.getGame();
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

        drawingPanel = new DrawingPanel(this.gameLogistics, gridSize);
        jFrame.add(drawingPanel);

        jFrame.setVisible(true);
    }

    @Override
    public void run() {
        startViewing();
    }


    public static POCView startPOCView(GameLogistics gameLogistics){
        POCView pocView = new POCView(gameLogistics);
        Thread thread = new Thread(pocView);
        thread.start();
        return pocView;
    }

    private class DrawingPanel extends JPanel {
        GameLogistics gameLogistics;

        @Setter
        private int gridSize;

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            try {
                drawGrid(graphics);
                drawElements(graphics);
                for(Player player : gameLogistics.getPlayers().values()){
                    drawRobot(graphics, Color.BLACK, player);
                }
            } catch (GameNotStartedException e) {
                e.printStackTrace();
            }

        }

        private void drawElements(Graphics graphics) throws GameNotStartedException {
            Board board = gameLogistics.getBoard();
            Tile[][] tiles = board.getSquares();
            int squaresX = tiles.length;
            int squaresY = tiles[0].length;
            for(int x = 0; x<squaresX; x++){
                for(int y = 0; y<squaresY; y++){
                    Tile tile = tiles[x][y];
                    for(Element element : tile.getElements()){
                        element.drawSelf(x*gridSize, y*gridSize, gridSize, graphics);
                    }
                }
            }
        }

        private void drawGrid(Graphics graphics){
            try {
                Board board = gameLogistics.getBoard();
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

            //to draw text
            //graphics.drawChars(player.getFacing().toString().toCharArray(), 0, player.getFacing().toString().length(), 100, 100);
        }

        public DrawingPanel(GameLogistics gameLogistics, int gridSize){
            this.gameLogistics = gameLogistics;
            this.gridSize = gridSize;
        }
    }

}
