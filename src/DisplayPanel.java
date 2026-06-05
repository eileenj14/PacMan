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
        pacmanX = 450 - 15;
        pacmanY = 600 + 1;
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

        for(int x = 70; x <= 825; x += 30) {
            for(int y = 105; y <= 1000; y += 30) {
                if(isGreyTile(x, y)) {
                    foods.add(new Point(x, y));
                }
            }
        }

        energizers.add(new Point(62, 182));
        energizers.add(new Point(62, 785));
        energizers.add(new Point(812, 182));
        energizers.add(new Point(812, 785));

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
            if(isGreyTile(pacmanX, pacmanY + 15)) {
                pacmanX -= 15;
                if(pacmanX <= 0) pacmanX = 910 - 60;
                pacmanDirection = 2;
            }
        }
        if(pressedKeys[KeyEvent.VK_D]) {
            if(isGreyTile(pacmanX + 35, pacmanY + 15)) {
                pacmanX += 15;
                if(pacmanX > 910 - 45) pacmanX = 0;
                pacmanDirection = 3;
            }
        }
        if(pressedKeys[KeyEvent.VK_W]) {
            if(isGreyTile(pacmanX + 15, pacmanY)) {
                pacmanY -= 15;
                pacmanDirection = 0;
            }
        }
        if(pressedKeys[KeyEvent.VK_S]) {
            if(isGreyTile(pacmanX + 15, pacmanY + 30)) {
                pacmanY += 15;
                pacmanDirection = 1;
            }
        }
    }

    private boolean isGreyTile(int x, int y) {
        int clr = background.getRGB(x, y);
        Color color = new Color(clr, true);
        boolean r = Math.abs(color.getRed() - 75) <= 10;
        boolean g = Math.abs(color.getGreen() - 75) <= 10;
        boolean b = Math.abs(color.getBlue() - 75) <= 10;
        return r && g && b;
    }

    private Rectangle pacManRectangle() {
        int imageHeight = pacman[0][0].getHeight();
        int imageWidth = pacman[0][0].getWidth();
        Rectangle rect = new Rectangle(pacmanX, pacmanY, imageWidth, imageHeight);
        return rect;
    }

    private Rectangle foodRectangle(Point point) {
        int imageHeight = food.getHeight();
        int imageWidth = food.getWidth();
        Rectangle rect = new Rectangle(point.x, point.y, imageWidth, imageHeight);
        return rect;
    }

    private Rectangle energizerRectangle(Point point) {
        int imageHeight = energizer.getHeight();
        int imageWidth = energizer.getWidth();
        Rectangle rect = new Rectangle(point.x, point.y, imageWidth, imageHeight);
        return rect;
    }

    private void checkPacManFoodCollision() {
        Rectangle pacmanRect = pacManRectangle();
        for(int i = 0; i < foods.size(); i++) {
            Rectangle foodRect = foodRectangle(foods.get(i));
            if(pacmanRect.intersects(foodRect)) {
                highScore += 10;
                foods.remove(i);
                i--;
            }
        }
    }

    private void checkPacManEnergizerCollision() {
        Rectangle pacmanRect = pacManRectangle();
        for(int i = 0; i < energizers.size(); i++) {
            Rectangle energizerRect = energizerRectangle(energizers.get(i));
            if(pacmanRect.intersects(energizerRect)) {
                highScore += 40;
                energizers.remove(i);
                i--;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePacMan();
        pacmanFrameCount = (pacmanFrameCount + 1) % 4;
        checkPacManFoodCollision();
        checkPacManEnergizerCollision();

        repaint();
    }
}
