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
    private int pacmanDirection;
    private int pacmanFrameCount;
    private BufferedImage background;
    private BufferedImage[][] pacman = new BufferedImage[4][4];
    private BufferedImage food;
    private BufferedImage energizer;
    private ArrayList<Point> foods;
    private ArrayList<Point> energizers;
    private boolean[] pressedKeys;
    private boolean gameStart;
    private boolean gameOver;
    private Timer timer;

    public DisplayPanel() {
        highScore = 0;
        lives = 3;
        pacmanX = 435 - 2;
        pacmanY = 545;
        pacmanDirection = 3;
        pacmanFrameCount = 0;
        foods = new ArrayList<>();
        energizers = new ArrayList<>();
        pressedKeys = new boolean[128];
        gameStart = false;
        gameOver = false;
        timer = new Timer(150, this);
        try {
            background = ImageIO.read(new File("src/background.png"));
        } catch(IOException e) {}
        try {
            pacman[0][0] = ImageIO.read(new File("src/pacmanUp1.png"));
            pacman[0][1] = ImageIO.read(new File("src/pacmanDown1.png"));
            pacman[0][2] = ImageIO.read(new File("src/pacmanLeft1.png"));
            pacman[0][3] = ImageIO.read(new File("src/pacmanRight1.png"));
            pacman[1][0] = ImageIO.read(new File("src/pacmanUp2.png"));
            pacman[1][1] = ImageIO.read(new File("src/pacmanDown2.png"));
            pacman[1][2] = ImageIO.read(new File("src/pacmanLeft2.png"));
            pacman[1][3] = ImageIO.read(new File("src/pacmanRight2.png"));
            pacman[2][0] = ImageIO.read(new File("src/pacmanUp3.png"));
            pacman[2][1] = ImageIO.read(new File("src/pacmanDown3.png"));
            pacman[2][2] = ImageIO.read(new File("src/pacmanLeft3.png"));
            pacman[2][3] = ImageIO.read(new File("src/pacmanRight3.png"));
            pacman[3][0] = ImageIO.read(new File("src/pacman4.png"));
            pacman[3][1] = ImageIO.read(new File("src/pacman4.png"));
            pacman[3][2] = ImageIO.read(new File("src/pacman4.png"));
            pacman[3][3] = ImageIO.read(new File("src/pacman4.png"));
        } catch(IOException e) {}
        try {
            food = ImageIO.read(new File("src/food.png"));
        } catch(IOException e) {}
        try {
            energizer = ImageIO.read(new File("src/energizer.png"));
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

        g.drawImage(background, -2, 0, null);
        g.drawImage(pacman[pacmanFrameCount][pacmanDirection], pacmanX, pacmanY, null);

//        for(Point f : foods) {
//            g.drawImage(food, f.x, f.y, null);
//        }
//
//        for(Point e : energizers) {
//            g.drawImage(energizer, e.x, e.y, null);
//        }

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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    private void movePacMan() {
        if(pressedKeys[KeyEvent.VK_A]) {
            if(isValidMovement(pacmanX - 5, pacmanY + 15, "grey")){
                pacmanX -= 5;
                if(pacmanX < -30) pacmanX = 1050;
                pacmanDirection = 2;
            }
        }
        if(pressedKeys[KeyEvent.VK_D]) {
            if(isValidMovement(pacmanX + 35, pacmanY + 15, "grey")){
                pacmanX += 5;
                if(pacmanX > 1050) pacmanX = -30;
                pacmanDirection = 3;
            }
        }
        if(pressedKeys[KeyEvent.VK_W]) {
            if(isValidMovement(pacmanX + 15, pacmanY - 5, "grey")){
                pacmanY -= 5;
                pacmanDirection = 0;
            }
        }
        if(pressedKeys[KeyEvent.VK_S]) {
            if(isValidMovement(pacmanX + 15, pacmanY + 35, "grey")){
                pacmanY += 5;
                pacmanDirection = 1;
            }
        }
    }

    private boolean isValidMovement(int x, int y, String validColor) {
        int clr = background.getRGB(x, y);
        Color color = new Color(clr, true);
        Color grey = new Color(67, 67, 67); //63
        if(validColor.equals("grey")) return color.equals(grey);
        else return false;
    }

    private Rectangle pacManRectangle() {
        int imageHeight = pacman[0][0].getHeight();
        int imageWidth = pacman[0][0].getWidth();
        Rectangle rect = new Rectangle(pacmanX, pacmanY, imageWidth, imageHeight);
        return rect;
    }

//    private Rectangle foodRectangle(Point point) {
//        int imageHeight = coin.getHeight();
//        int imageWidth = coin.getWidth();
//        Rectangle rect = new Rectangle(point.x, point.y, imageWidth, imageHeight);
//        return rect;
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
        pacmanFrameCount = (pacmanFrameCount + 1) % 4;

        repaint();
    }
}
