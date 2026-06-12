import org.w3c.dom.css.Rect;

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
    private int blinkyX;
    private int blinkyY;
    private int blinkyPrevDirection;
    private int blinkyState;
    private int pinkyX;
    private int pinkyY;
    private int pinkyPrevDirection;
    private int pinkyState;
    private int inkyX;
    private int inkyY;
    private int inkyPrevDirection;
    private int inkyState;
    private int clydeX;
    private int clydeY;
    private int clydePrevDirection;
    private int clydeState;
    private BufferedImage background;
    private BufferedImage[][] pacman = new BufferedImage[3][4];
    private BufferedImage[][] ghosts = new BufferedImage[4][3];
    private BufferedImage food;
    private BufferedImage energizer;
    private ArrayList<Point> foods;
    private ArrayList<Point> energizers;
    private boolean[] pressedKeys;
    private int gameState;
    private int counter;
    private Timer timer;

    public DisplayPanel() {
        highScore = 0;
        lives = 3;
        pacmanX = 450 - 30;
        pacmanY = 600;
        pacmanDirection = 3;
        pacmanFrameCount = 0;
        blinkyX = 450 - 30;
        blinkyY = 510 - 90;
        blinkyPrevDirection = 1;
        blinkyState = 0;
        pinkyX = 450 - 30;
        pinkyY = 510;
        pinkyPrevDirection = 1;
        pinkyState = 0;
        inkyX = 450 - 90;
        inkyY = 510;
        inkyPrevDirection = 1;
        inkyState = 0;
        clydeX = 450 + 30;
        clydeY = 510;
        clydePrevDirection = 1;
        clydeState = 0;
        foods = new ArrayList<>();
        energizers = new ArrayList<>();
        pressedKeys = new boolean[128];
        gameState = 0;
        counter = 0;
        timer = new Timer(300, this);
        try {
            background = ImageIO.read(new File("src/level_sprites/background.png"));
        } catch(IOException e) {}
        try {
            pacman[0][0] = ImageIO.read(new File("src/character_sprites/pacmanUp1.png"));
            pacman[0][1] = ImageIO.read(new File("src/character_sprites/pacmanDown1.png"));
            pacman[0][2] = ImageIO.read(new File("src/character_sprites/pacmanLeft1.png"));
            pacman[0][3] = ImageIO.read(new File("src/character_sprites/pacmanRight1.png"));
            pacman[1][0] = ImageIO.read(new File("src/character_sprites/pacmanUp2.png"));
            pacman[1][1] = ImageIO.read(new File("src/character_sprites/pacmanDown2.png"));
            pacman[1][2] = ImageIO.read(new File("src/character_sprites/pacmanLeft2.png"));
            pacman[1][3] = ImageIO.read(new File("src/character_sprites/pacmanRight2.png"));
            pacman[2][0] = ImageIO.read(new File("src/character_sprites/pacman3.png"));
            pacman[2][1] = ImageIO.read(new File("src/character_sprites/pacman3.png"));
            pacman[2][2] = ImageIO.read(new File("src/character_sprites/pacman3.png"));
            pacman[2][3] = ImageIO.read(new File("src/character_sprites/pacman3.png"));
        } catch(IOException e) {}
        try {
            ghosts[0][0] = ImageIO.read(new File("src/character_sprites/blinkyRight.png"));
            ghosts[0][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
            ghosts[1][0] = ImageIO.read(new File("src/character_sprites/pinkyRight.png"));
            ghosts[1][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
            ghosts[2][0] = ImageIO.read(new File("src/character_sprites/inkyRight.png"));
            ghosts[2][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
            ghosts[3][0] = ImageIO.read(new File("src/character_sprites/clydeRight.png"));
            ghosts[3][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
        } catch(IOException e) {}
        try {
            food = ImageIO.read(new File("src/level_sprites/food.png"));
            energizer = ImageIO.read(new File("src/level_sprites/energizer.png"));
        } catch(IOException e) {}

        for(int x = 70; x <= 825; x += 30) {
            for(int y = 105; y <= 1000; y += 30) {
                if(y < 360 || y > 690) {
                    if(isGreyTile(x, y)) {
                        foods.add(new Point(x, y));
                    }
                } else {
                    if(x == 220 || x == 670) {
                        foods.add(new Point(x, y));
                    }
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

        for(Point f : foods) {
            g.drawImage(food, f.x, f.y, null);
        }

        for(Point e : energizers) {
            g.drawImage(energizer, e.x, e.y, null);
        }

        g.drawImage(pacman[pacmanFrameCount][pacmanDirection], pacmanX, pacmanY, null);
        g.drawImage(ghosts[0][blinkyState], blinkyX, blinkyY, null);
        g.drawImage(ghosts[1][pinkyState], pinkyX, pinkyY, null);
        g.drawImage(ghosts[2][inkyState], inkyX, inkyY, null);
        g.drawImage(ghosts[3][clydeState], clydeX, clydeY, null);

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
            gameState = 1;
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
        if(pressedKeys[KeyEvent.VK_W] || pressedKeys[KeyEvent.VK_UP]) {
            if(isGreyTile(pacmanX + 15, pacmanY)) {
                pacmanY -= 30;
                pacmanDirection = 0;
            }
        } else if(pressedKeys[KeyEvent.VK_S] || pressedKeys[KeyEvent.VK_DOWN]) {
            if(isGreyTile(pacmanX + 15, pacmanY + 30)) {
                pacmanY += 30;
                pacmanDirection = 1;
            }
        } else if(pressedKeys[KeyEvent.VK_A] || pressedKeys[KeyEvent.VK_LEFT]) {
            if(isGreyTile(pacmanX, pacmanY + 15)) {
                pacmanX -= 30;
                if(pacmanX < 30) pacmanX = 910 - 75;
                pacmanDirection = 2;
            }
        } else if(pressedKeys[KeyEvent.VK_D] || pressedKeys[KeyEvent.VK_RIGHT]) {
            if(isGreyTile(pacmanX + 35, pacmanY + 15)) {
                pacmanX += 30;
                if(pacmanX > 910 - 60) pacmanX = 30;
                pacmanDirection = 3;
            }
        }
    }

    private void moveGhostsChase() {
        int nextMove = calculateNextMoveChase(blinkyX, blinkyY, pacmanX, pacmanY, blinkyPrevDirection);
        if(nextMove == 0) {
            blinkyY -= 30;
            blinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            blinkyY += 30;
            blinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            blinkyX -= 30;
            if(blinkyX < 30) blinkyX = 910 - 75;
            blinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            blinkyX += 30;
            if(blinkyX > 910 - 60) blinkyX = 30;
            blinkyPrevDirection = 2;
        }

        if(pacmanDirection == 0) {
            nextMove = calculateNextMoveChase(pinkyX, pinkyY, pacmanX, pacmanY - 120, pinkyPrevDirection);
        } else if(pacmanDirection == 1) {
            nextMove = calculateNextMoveChase(pinkyX, pinkyY, pacmanX, pacmanY + 120, pinkyPrevDirection);
        } else if(pacmanDirection == 2) {
            nextMove = calculateNextMoveChase(pinkyX, pinkyY, pacmanX - 120, pacmanY, pinkyPrevDirection);
        } else if(pacmanDirection == 3) {
            nextMove = calculateNextMoveChase(pinkyX, pinkyY, pacmanX + 120, pacmanY, pinkyPrevDirection);
        }
        if(nextMove == 0) {
            pinkyY -= 30;
            pinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            pinkyY += 30;
            pinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            pinkyX -= 30;
            if(pinkyX < 30) pinkyX = 910 - 75;
            pinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            pinkyX += 30;
            if(pinkyX > 910 - 60) pinkyX = 30;
            pinkyPrevDirection = 2;
        }

        if(pacmanDirection == 0) {
            nextMove = calculateNextMoveChase(inkyX, inkyY, calculateInkyTargetX(0), calculateInkyTargetY(-60), inkyPrevDirection);
        } else if(pacmanDirection == 1) {
            nextMove = calculateNextMoveChase(inkyX, inkyY, calculateInkyTargetX(0), calculateInkyTargetY(60), inkyPrevDirection);
        } else if(pacmanDirection == 2) {
            nextMove = calculateNextMoveChase(inkyX, inkyY, calculateInkyTargetX(-60), calculateInkyTargetY(0), inkyPrevDirection);
        } else if(pacmanDirection == 3) {
            nextMove = calculateNextMoveChase(inkyX, inkyY, calculateInkyTargetX(60), calculateInkyTargetY(0), inkyPrevDirection);
        }
        if(nextMove == 0) {
            inkyY -= 30;
            inkyPrevDirection = 1;
        } else if(nextMove == 1) {
            inkyY += 30;
            inkyPrevDirection = 0;
        } else if(nextMove == 2) {
            inkyX -= 30;
            if(inkyX < 30) inkyX = 910 - 75;
            inkyPrevDirection = 3;
        } else if(nextMove == 3) {
            inkyX += 30;
            if(inkyX > 910 - 60) inkyX = 30;
            inkyPrevDirection = 2;
        }

        if(Math.sqrt(Math.pow(clydeX - pacmanX, 2) + Math.pow(clydeY - pacmanY, 2)) > 240) {
            nextMove = calculateNextMoveChase(clydeX, clydeY, pacmanX, pacmanY, clydePrevDirection);
        } else {
            nextMove = calculateNextMoveScatter(clydeX, clydeY, 0, 1082, clydePrevDirection);
        }
        if(nextMove == 0) {
            clydeY -= 30;
            clydePrevDirection = 1;
        } else if(nextMove == 1) {
            clydeY += 30;
            clydePrevDirection = 0;
        } else if(nextMove == 2) {
            clydeX -= 30;
            if(clydeX < 30) clydeX = 910 - 75;
            clydePrevDirection = 3;
        } else if(nextMove == 3) {
            clydeX += 30;
            if(clydeX > 910 - 60) clydeX = 30;
            clydePrevDirection = 2;
        }
    }

    private void moveGhostsScatter() {
        int nextMove = calculateNextMoveScatter(blinkyX, blinkyY, 900 - 30, 0, blinkyPrevDirection);
        if(nextMove == 0) {
            blinkyY -= 30;
            blinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            blinkyY += 30;
            blinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            blinkyX -= 30;
            if(blinkyX < 30) blinkyX = 910 - 75;
            blinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            blinkyX += 30;
            if(blinkyX > 910 - 60) blinkyX = 30;
            blinkyPrevDirection = 2;
        }

        nextMove = calculateNextMoveScatter(pinkyX, pinkyY, 0, 0, pinkyPrevDirection);
        if(nextMove == 0) {
            pinkyY -= 30;
            pinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            pinkyY += 30;
            pinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            pinkyX -= 30;
            if(pinkyX < 30) pinkyX = 910 - 75;
            pinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            pinkyX += 30;
            if(pinkyX > 910 - 60) pinkyX = 30;
            pinkyPrevDirection = 2;
        }

        if(counter > 21) nextMove = calculateNextMoveScatter(inkyX, inkyY, 900 - 30, 1082, inkyPrevDirection);
        else nextMove = calculateNextMoveScatter(inkyX, inkyY, 900 - 30, 0, inkyPrevDirection);
        if(nextMove == 0) {
            inkyY -= 30;
            inkyPrevDirection = 1;
        } else if(nextMove == 1) {
            inkyY += 30;
            inkyPrevDirection = 0;
        } else if(nextMove == 2) {
            inkyX -= 30;
            if(inkyX < 30) inkyX = 910 - 75;
            inkyPrevDirection = 3;
        } else if(nextMove == 3) {
            inkyX += 30;
            if(inkyX > 910 - 60) inkyX = 30;
            inkyPrevDirection = 2;
        }

        if(counter > 18) nextMove = calculateNextMoveScatter(clydeX, clydeY, 0, 1082, clydePrevDirection);
        else nextMove = calculateNextMoveScatter(clydeX, clydeY, 0, 0, clydePrevDirection);
        if(nextMove == 0) {
            clydeY -= 30;
            clydePrevDirection = 1;
        } else if(nextMove == 1) {
            clydeY += 30;
            clydePrevDirection = 0;
        } else if(nextMove == 2) {
            clydeX -= 30;
            if(clydeX < 30) clydeX = 910 - 75;
            clydePrevDirection = 3;
        } else if(nextMove == 3) {
            clydeX += 30;
            if(clydeX > 910 - 60) clydeX = 30;
            clydePrevDirection = 2;
        }
    }

    private void moveGhostsFrightened() {
        int nextMove = calculateNextMoveFrightened(blinkyX, blinkyY, blinkyPrevDirection);
        if(nextMove == 0) {
            blinkyY -= 30;
            blinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            blinkyY += 30;
            blinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            blinkyX -= 30;
            if(blinkyX < 30) blinkyX = 910 - 75;
            blinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            blinkyX += 30;
            if(blinkyX > 910 - 60) blinkyX = 30;
            blinkyPrevDirection = 2;
        }

        nextMove = calculateNextMoveFrightened(pinkyX, pinkyY, pinkyPrevDirection);
        if(nextMove == 0) {
            pinkyY -= 30;
            pinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            pinkyY += 30;
            pinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            pinkyX -= 30;
            if(pinkyX < 30) pinkyX = 910 - 75;
            pinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            pinkyX += 30;
            if(pinkyX > 910 - 60) pinkyX = 30;
            pinkyPrevDirection = 2;
        }

        nextMove = calculateNextMoveFrightened(inkyX, inkyY, inkyPrevDirection);
        if(nextMove == 0) {
            inkyY -= 30;
            inkyPrevDirection = 1;
        } else if(nextMove == 1) {
            inkyY += 30;
            inkyPrevDirection = 0;
        } else if(nextMove == 2) {
            inkyX -= 30;
            if(inkyX < 30) inkyX = 910 - 75;
            inkyPrevDirection = 3;
        } else if(nextMove == 3) {
            inkyX += 30;
            if(inkyX > 910 - 60) inkyX = 30;
            inkyPrevDirection = 2;
        }

        nextMove = calculateNextMoveFrightened(clydeX, clydeY, clydePrevDirection);
        if(nextMove == 0) {
            clydeY -= 30;
            clydePrevDirection = 1;
        } else if(nextMove == 1) {
            clydeY += 30;
            clydePrevDirection = 0;
        } else if(nextMove == 2) {
            clydeX -= 30;
            if(clydeX < 30) clydeX = 910 - 75;
            clydePrevDirection = 3;
        } else if(nextMove == 3) {
            clydeX += 30;
            if(clydeX > 910 - 60) clydeX = 30;
            clydePrevDirection = 2;
        }
    }

    private int calculateNextMoveChase(int currX, int currY, int targetX, int targetY, int prevDirection) {
        int[] distances = new int[4];
        if(!isGreyTile(currX + 15, currY - 15) || prevDirection == 0) {
            distances[0] = Integer.MAX_VALUE;
        } else {
            distances[0] = (int) Math.sqrt(Math.pow(currX - targetX, 2) + Math.pow(currY - 30 - targetY, 2));
        }
        if(!isGreyTile(currX + 15, currY + 45) || prevDirection == 1) {
            distances[1] = Integer.MAX_VALUE;
        } else {
            distances[1] = (int) Math.sqrt(Math.pow(currX - targetX, 2) + Math.pow(currY + 30 - targetY, 2));
        }
        if(!isGreyTile(currX - 15, currY + 15) || prevDirection == 2) {
            distances[2] = Integer.MAX_VALUE;
        } else {
            distances[2] = (int) Math.sqrt(Math.pow(currX - 30 - targetX, 2) + Math.pow(currY - targetY, 2));
        }
        if(!isGreyTile(currX + 45, currY + 15) || prevDirection == 3) {
            distances[3] = Integer.MAX_VALUE;
        } else {
            distances[3] = (int) Math.sqrt(Math.pow(currX + 30 - targetX, 2) + Math.pow(currY - targetY, 2));
        }
        int minIdx = 0;
        int minVal = distances[0];
        for(int i = 0; i < distances.length; i++) {
            if(distances[i] < minVal) {
                minIdx = i;
                minVal = distances[i];
            }
        }
        return minIdx;
    }

    private int calculateInkyTargetX(int offset) {
        int differenceX = Math.abs(blinkyX - (pacmanX + offset));
        if(blinkyX < pacmanX) {
            return (pacmanX + offset) + differenceX;
        } else {
            return (pacmanX + offset) - differenceX;
        }
    }

    private int calculateInkyTargetY(int offset) {
        int differenceY = Math.abs(blinkyY - (pacmanY + offset));
        if(blinkyY < pacmanY) {
            return (pacmanY + offset) + differenceY;
        } else {
            return (pacmanY + offset) - differenceY;
        }
    }

    private int calculateNextMoveScatter(int currX, int currY, int targetX, int targetY, int prevDirection) {
        int[] distances = new int[4];
        if(isBlackTile(currX + 15, currY - 15) || prevDirection == 0) {
            distances[0] = Integer.MAX_VALUE;
        } else {
            distances[0] = (int) Math.sqrt(Math.pow(currX - targetX, 2) + Math.pow(currY - 30 - targetY, 2));
        }
        if(isBlackTile(currX + 15, currY + 45) || prevDirection == 1) {
            distances[1] = Integer.MAX_VALUE;
        } else {
            distances[1] = (int) Math.sqrt(Math.pow(currX - targetX, 2) + Math.pow(currY + 30 - targetY, 2));
        }
        if(isBlackTile(currX - 15, currY + 15) || prevDirection == 2) {
            distances[2] = Integer.MAX_VALUE;
        } else {
            distances[2] = (int) Math.sqrt(Math.pow(currX - 30 - targetX, 2) + Math.pow(currY - targetY, 2));
        }
        if(isBlackTile(currX + 45, currY + 15) || prevDirection == 3) {
            distances[3] = Integer.MAX_VALUE;
        } else {
            distances[3] = (int) Math.sqrt(Math.pow(currX + 30 - targetX, 2) + Math.pow(currY - targetY, 2));
        }
        int minIdx = 0;
        int minVal = distances[0];
        for(int i = 0; i < distances.length; i++) {
            if(distances[i] < minVal) {
                minIdx = i;
                minVal = distances[i];
            }
        }
        return minIdx;
    }

    private int calculateNextMoveFrightened(int currX, int currY, int prevDirection) {
        int[] distances = new int[4];
        if(!isGreyTile(currX + 15, currY - 15) || prevDirection == 0) {
            distances[0] = Integer.MAX_VALUE;
        }
        if(!isGreyTile(currX + 15, currY + 45) || prevDirection == 1) {
            distances[1] = Integer.MAX_VALUE;
        }
        if(!isGreyTile(currX - 15, currY + 15) || prevDirection == 2) {
            distances[2] = Integer.MAX_VALUE;
        }
        if(!isGreyTile(currX + 45, currY + 15) || prevDirection == 3) {
            distances[3] = Integer.MAX_VALUE;
        }
        int randIdx = (int) (Math.random() * 4);
        while(distances[randIdx] == Integer.MAX_VALUE) {
            randIdx = (randIdx + 1) % 4;
        }
        return randIdx;
    }

    private boolean isGreyTile(int x, int y) {
        int clr = background.getRGB(x, y);
        Color color = new Color(clr, true);
        boolean r = Math.abs(color.getRed() - 75) <= 10;
        boolean g = Math.abs(color.getGreen() - 75) <= 10;
        boolean b = Math.abs(color.getBlue() - 75) <= 10;
        return r && g && b;
    }

    private boolean isBlackTile(int x, int y) {
        int clr = background.getRGB(x, y);
        Color color = new Color(clr, true);
        boolean r = color.getRed() == 0;
        boolean g = color.getGreen() == 0;
        boolean b = color.getBlue() == 0;
        return r && g && b;
    }

    private Rectangle pacManRectangle() {
        int imageHeight = pacman[0][0].getHeight();
        int imageWidth = pacman[0][0].getWidth();
        return new Rectangle(pacmanX, pacmanY, imageWidth, imageHeight);
    }

    private Rectangle blinkyRectangle() {
        int imageHeight = ghosts[0][0].getHeight();
        int imageWidth = ghosts[0][0].getWidth();
        return new Rectangle(blinkyX, blinkyY, imageWidth, imageHeight);
    }

    private Rectangle pinkyRectangle() {
        int imageHeight = ghosts[0][0].getHeight();
        int imageWidth = ghosts[0][0].getWidth();
        return new Rectangle(pinkyX, pinkyY, imageWidth, imageHeight);
    }

    private Rectangle inkyRectangle() {
        int imageHeight = ghosts[0][0].getHeight();
        int imageWidth = ghosts[0][0].getWidth();
        return new Rectangle(inkyX,inkyY, imageWidth, imageHeight);
    }

    private Rectangle clydeRectangle() {
        int imageHeight = ghosts[0][0].getHeight();
        int imageWidth = ghosts[0][0].getWidth();
        return new Rectangle(clydeX, clydeY, imageWidth, imageHeight);
    }

    private Rectangle foodRectangle(Point point) {
        int imageHeight = food.getHeight();
        int imageWidth = food.getWidth();
        return new Rectangle(point.x, point.y, imageWidth, imageHeight);
    }

    private Rectangle energizerRectangle(Point point) {
        int imageHeight = energizer.getHeight();
        int imageWidth = energizer.getWidth();
        return new Rectangle(point.x, point.y, imageWidth, imageHeight);
    }

    private boolean checkPacmanGhostsCollisionNormal() {
        Rectangle pacmanRect = pacManRectangle();
        Rectangle blinkyRect = blinkyRectangle();
        Rectangle pinkyRect = pinkyRectangle();
        Rectangle inkyRect = inkyRectangle();
        Rectangle clydeRect = clydeRectangle();
        return pacmanRect.intersects(blinkyRect) || pacmanRect.intersects(pinkyRect)
                || pacmanRect.intersects(inkyRect) || pacmanRect.intersects(clydeRect);
    }

    private void checkPacmanBlinkyCollisionFrightened() {}

    private void checkPacmanPinkyCollisionFrightened() {}

    private void checkPacmanInkyCollisionFrightened() {}

    private void checkPacmanClydeCollisionFrightened() {}

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
                gameState = 3;
                i--;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameState != 0) {
            counter += 3;

            if(gameState == 1 && counter == 60) {
                gameState = 2;
                counter = 0;
            }
            if(gameState == 2 && counter == 210) {
                gameState = 1;
                counter = 0;
            }

            movePacMan();
            pacmanFrameCount = (pacmanFrameCount + 1) % 3;

            if(gameState == 1) {
                moveGhostsScatter();
            } else if(gameState == 2) {
                moveGhostsChase();
            }

            if(checkPacmanGhostsCollisionNormal()) {
                gameState = 0;
                counter = 0;
                lives--;
            }

            checkPacManFoodCollision();
            checkPacManEnergizerCollision();

            if(lives == 0 || (foods.isEmpty() && energizers.isEmpty())) {
                gameState = 4;
            }
        } else {
            pacmanX = 450 - 30;
            pacmanY = 600;
            pacmanDirection = 3;
            pacmanFrameCount = 0;
            blinkyX = 450 - 30;
            blinkyY = 510 - 90;
            blinkyPrevDirection = 1;
            blinkyState = 0;
            pinkyX = 450 - 30;
            pinkyY = 510;
            pinkyPrevDirection = 1;
            pinkyState = 0;
            inkyX = 450 - 90;
            inkyY = 510;
            inkyPrevDirection = 1;
            inkyState = 0;
            clydeX = 450 + 30;
            clydeY = 510;
            clydePrevDirection = 1;
            clydeState = 0;
        }

        repaint();
    }
}
