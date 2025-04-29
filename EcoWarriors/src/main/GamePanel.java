package main;

import entity.Player;
import object.OBJ_Key;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //Screen Settings.
    final int originalTileSize = 16; //16 x 16 tile
    final int scale = 4;

    public final int tileSize = originalTileSize * scale; //48 x 48 tile
    public final int maxScreenCol = 16; //change only this one!
    public final int maxScreenRow = 12; //change only this one!
    public final int screenWidth = tileSize * maxScreenCol; //Resolution(Horizontally): 768
    public final int screenHeight = tileSize * maxScreenRow; //Resolution(Vertically): 576

    //World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 120;

    TileManager tileM = new TileManager(this);

    KeyHandler keyH = new KeyHandler();

    Thread gameThread;

    public CollisionChecker cChecker = new CollisionChecker(this);

    public AssetSetter aSetter = new AssetSetter(this);

    public Player player = new Player(this, keyH);

    public SuperObject[]obj = new SuperObject[10];

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while(gameThread != null){

            currentTime = System.nanoTime();

            delta = delta + (currentTime - lastTime) / drawInterval;

            timer = timer + (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1){
                // Updates: Updates information such as character positions.
                update();
                //Draw: Draws screen with the updates information.
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        //Tile
        tileM.draw(g2);

        //Object
        for (int i = 0; i < obj.length; i++){
            if (obj[i] != null){
                obj[i].draw(g2, this);
            }
        }

        //Player
        player.draw(g2);

        g2.dispose();
    }
}
