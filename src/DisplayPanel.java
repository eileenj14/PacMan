import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, ActionListener {
    private int highScore;
    private int lives;
    private int pacmanX;
    private int pacmanY;
    private BufferedImage background;
    private BufferedImage pacman;
    private BufferedImage food;
    private BufferedImage energizer;
    private boolean[] pressedKeys;
    private ArrayList<Point> foods;
    private ArrayList<Point> energizers;
    private boolean gameStart;
    private boolean gameOver;
    private Timer timer;

    public DisplayPanel() {
        highScore = 0;
        lives = 3;
        pacmanX = 450;
        pacmanY = 600;
        pressedKeys = new boolean[128];
        foods = new ArrayList<>();
        energizers = new ArrayList<>();
        gameStart = false;
        gameOver = false;
        timer = new Timer(10, this);
        try {
            background = ImageIO.read(new File("src/background.png"));
        } catch(IOException e) {}
        try {
            pacman = ImageIO.read(new File("src/marioright.png"));
        } catch(IOException e) {}
        try {
            food = ImageIO.read(new File("src/marioright.png"));
        } catch(IOException e) {}
        try {
            energizer = ImageIO.read(new File("src/marioright.png"));
        } catch(IOException e) {}

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, -5, 0, null);
        g.drawImage(pacman, pacmanX, pacmanY, null);

//        if(!gameStart) {
//
//        }else if(gameOver) {
//            g.setFont(new Font("Arial", Font.BOLD, 32));
//            if(highScore >= 10) {
//                g.drawString("YOU WIN!", 350, 240);
//            } else {
//                g.drawString("GAME OVER, YOU LOSE.", 350, 240);
//            }
//        } else {
//            g.drawImage(pacman, pacmanX, pacmanY, null);
//            g.drawImage(luigi, luigiX, luigiY, null);
//            g.drawImage(goomba, (int) goombaX, goombaY, null);
//        }

        for(Point f : foods) {
            g.drawImage(food, f.x, f.y, null);
        }

        for(Point e : energizers) {
            g.drawImage(energizer, e.x, e.y, null);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("High Score: " + highScore, 50, 30);
        g.drawString("Lives Left: " + lives, 50, 60);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            gameStart = !gameStart;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys[keyCode] = true;

        if(keyCode == KeyEvent.VK_W) {
            try {
                pacman = ImageIO.read(new File("src/marioleft.png"));
            } catch(IOException error) {}
        }
        if(keyCode == KeyEvent.VK_S) {
            try {
                pacman = ImageIO.read(new File("src/marioright.png"));
            } catch(IOException error) {}
        }
        if(keyCode == KeyEvent.VK_A) {
            try {
                pacman = ImageIO.read(new File("src/marioleft.png"));
            } catch(IOException error) {}
        }
        if(keyCode == KeyEvent.VK_D) {
            try {
                pacman = ImageIO.read(new File("src/marioright.png"));
            } catch(IOException error) {}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    private void movePacMan() {
        if(pressedKeys[KeyEvent.VK_A]) {
            if(pacmanX - 5 >= 0) pacmanX -= 5;
        }
        if(pressedKeys[KeyEvent.VK_D]) {
            if(pacmanX + 5 <= 900) pacmanX += 5;
        }
        if(pressedKeys[KeyEvent.VK_W]) {
            if(pacmanY - 5 >= 0) pacmanY -= 5;
        }
        if(pressedKeys[KeyEvent.VK_S]) {
            if(pacmanY + 5 <= 435) pacmanY += 5;
        }
    }

//    private Rectangle pacManRectangle() {
//        int imageHeight = pacman.getHeight();
//        int imageWidth = pacman.getWidth();
//        Rectangle rect = new Rectangle(pacmanX, pacmanY, imageWidth, imageHeight);
//        return rect;
//    }
//
//    private Rectangle foodRectangle(Point point) {
//        int imageHeight = coin.getHeight();
//        int imageWidth = coin.getWidth();
//        Rectangle rect = new Rectangle(point.x, point.y, imageWidth, imageHeight);
//        return rect;
//    }
//
//    private boolean checkPacManBlinkyCollision() {
//        Rectangle marioRect = marioRectangle();
//        Rectangle goombaRect = goombaRectangle();
//        return marioRect.intersects(goombaRect);
//    }
//
//    private void checkPacManFoodCollision() {
//        Rectangle marioRect = marioRectangle();
//        for(int i = 0; i < food.size(); i++) {
//            Rectangle coinRect = coinRectangle(food.get(i));
//            if(marioRect.intersects(coinRect)) {
//                highScore++;
//                food.remove(i);
//                i--;
//            }
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePacMan();

        repaint();
    }
}
