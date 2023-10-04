package com.nethermole.roborally.view;

import com.nethermole.roborally.gamepackage.Game;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.board.element.Element;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.TurnRateLimiterBot;
import lombok.Getter;
import lombok.Setter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo: update to updateCache then drawCache pattern
public class BasicView extends AbstractView implements Runnable{

    int durationMins;

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() < startTime + durationMins*60*1000){
            if(System.currentTimeMillis() % 100 == 0){
                this.getWindow().getCanvas().repaint();
            }
        }
    }

    @Getter
    private Window window;

    @Setter
    private Board board;

    public BasicView(int duration){
        this.durationMins = duration;
        window = new Window();
    }

    public class PixelCanvas extends JPanel {
        private Map<Integer,Map<Integer, Color>> colorCache = new HashMap();
        private List<Player> players;

        public PixelCanvas() {
            players = new java.util.ArrayList<>();
        }

        public void addPlayer(Player player) {
            players.add(player);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if(board != null){
                drawCheckpoints(graphics);
                for (Player player : players) {
                    drawPlayer(graphics, player);
                }
                //drawCache(graphics);
            }
        }

        private void drawCache(Graphics graphics){
            for(Map.Entry<Integer, Map<Integer, Color>> entryX : colorCache.entrySet()){
                int x = entryX.getKey();
                for(Map.Entry<Integer, Color> entryY : entryX.getValue().entrySet()){
                    int y = entryY.getKey();
                    Color color = entryY.getValue();

                    graphics.setColor(color);
                    graphics.fillRect(x-1, y-1, 3, 3);
                }
            }
        }
        
        private void drawCheckpoints(Graphics graphics){
            graphics.setColor(Color.BLACK);

            boolean doneWithCheckpoints = false;
            for(int i = 1; doneWithCheckpoints == false; i++){
                Position position = board.getPositionOfCheckpoint(i);
                if(position == null){
                    doneWithCheckpoints = true;
                } else{
                    graphics.fillRect(position.getX()-2, position.getY()-2, 5, 5);
                }
            }
        }

        private void drawPlayer(Graphics graphics, Player player){
            Position position = player.getPosition();
            Color playerColor = new Color(player.getColor().getRed(), player.getColor().getGreen(), player.getColor().getBlue());

            if(player.getPosition() != null){
                graphics.setColor(playerColor);
                if(player instanceof TurnRateLimiterBot){
                    graphics.setColor(Color.black);
                }
                graphics.fillRect(player.getPosition().getX()-1, player.getPosition().getY()-1, 3, 3);
            }

            Map<Integer, Color> colorRow = colorCache.getOrDefault(player.getPosition().getX(), new HashMap<>());
            colorRow.put(player.getPosition().getY(), playerColor);
            colorCache.put(player.getPosition().getX(), colorRow);
        }
    }

    public class Window extends JFrame {

        @Getter
        private PixelCanvas canvas;

        public Window() {
            canvas = new PixelCanvas();
            add(canvas);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Game Area");
            setSize(1600, 1200);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
