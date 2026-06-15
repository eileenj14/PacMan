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
    private int highScore = 0;
    private int lives = 3;

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
    private int[] distances = new int[4];

    private int ghostsEatenCount = 0;
    private ArrayList<Point> foods = new ArrayList<>();
    private ArrayList<Point> energizers = new ArrayList<>();

    private BufferedImage background;
    private BufferedImage food;
    private BufferedImage energizer;
    private BufferedImage[][] pacman = new BufferedImage[3][4];
    private BufferedImage[][] ghosts = new BufferedImage[4][3];

    private int gameState = 0;
    private int count = 0;
    private Timer timer = new Timer(300, this);
    private boolean[] pressedKeys = new boolean[128];

    public DisplayPanel() {
        try {
            background = ImageIO.read(new File("src/level_sprites/background.png"));
            food = ImageIO.read(new File("src/level_sprites/food.png"));
            energizer = ImageIO.read(new File("src/level_sprites/energizer.png"));
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
            ghosts[0][0] = ImageIO.read(new File("src/character_sprites/blinkyRight.png"));
            ghosts[0][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
            ghosts[1][0] = ImageIO.read(new File("src/character_sprites/pinkyRight.png"));
            ghosts[1][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
            ghosts[2][0] = ImageIO.read(new File("src/character_sprites/inkyRight.png"));
            ghosts[2][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
            ghosts[3][0] = ImageIO.read(new File("src/character_sprites/clydeRight.png"));
            ghosts[3][1] = ImageIO.read(new File("src/character_sprites/eatenGhost.png"));
        } catch(IOException e) {}

        for(int x = 70; x <= 825; x += 30) {
            for(int y = 105; y <= 1000; y += 30) {
                if(y < 360 || y > 690) {
                    if(isGreyTile(x, y)) foods.add(new Point(x, y));
                } else {
                    if(x == 220 || x == 670) foods.add(new Point(x, y));
                }
            }
        }

        energizers.add(new Point(60, 180));
        energizers.add(new Point(60, 785));
        energizers.add(new Point(815, 180));
        energizers.add(new Point(815, 785));

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, null);

        for(Point f : foods) g.drawImage(food, f.x, f.y, null);
        for(Point e : energizers) g.drawImage(energizer, e.x, e.y, null);

        g.drawImage(pacman[pacmanFrameCount][pacmanDirection], pacmanX, pacmanY, null);
        g.drawImage(ghosts[0][blinkyState], blinkyX, blinkyY, null);
        g.drawImage(ghosts[1][pinkyState], pinkyX, pinkyY, null);
        g.drawImage(ghosts[2][inkyState], inkyX, inkyY, null);
        g.drawImage(ghosts[3][clydeState], clydeX, clydeY, null);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("High Score: " + highScore, 30, 30);
        g.drawString("Lives Left: " + lives, 30, 60);

        if(gameState == 0) {
            g.setColor(Color.YELLOW);
            g.drawString("Hold down SPACE to start / resume the game!", 510, 60);
        } else if(gameState == 1) {
            g.drawString("Ghosts are scattering! Don't get caught!", 510, 60);
        } else if(gameState == 2) {
            g.drawString("Ghosts are chasing! Don't get caught!", 510, 60);
        } else if(gameState == 3) {
            g.setColor(Color.BLUE);
            g.drawString("Ghosts are frightened! Eat the ghosts!", 510, 60);
        } else if(gameState == 4) {
            g.setColor(Color.YELLOW);
            if(lives <= 0) g.drawString("You lost! Better luck next time! :(", 510, 60);
            else g.drawString("You win! :)", 510, 60);

            timer.stop();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

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
            if(isGreyTile(pacmanX + 15, pacmanY - 15)) {
                pacmanY -= 30;
                pacmanDirection = 0;
            }
        } else if(pressedKeys[KeyEvent.VK_S] || pressedKeys[KeyEvent.VK_DOWN]) {
            if(isGreyTile(pacmanX + 15, pacmanY + 45)) {
                pacmanY += 30;
                pacmanDirection = 1;
            }
        } else if(pressedKeys[KeyEvent.VK_A] || pressedKeys[KeyEvent.VK_LEFT]) {
            if(isGreyTile(pacmanX - 15, pacmanY + 15)) {
                pacmanX -= 30;
                if(pacmanX < 30) pacmanX = 840;
                pacmanDirection = 2;
            }
        } else if(pressedKeys[KeyEvent.VK_D] || pressedKeys[KeyEvent.VK_RIGHT]) {
            if(isGreyTile(pacmanX + 45, pacmanY + 15)) {
                pacmanX += 30;
                if(pacmanX > 840) pacmanX = 30;
                pacmanDirection = 3;
            }
        }
    }

    private void moveGhostsChase() {
        int nextMove;
        if(blinkyState == 0) {
            nextMove = calculateNextMoveIsNotGrey(blinkyX, blinkyY, pacmanX, pacmanY, blinkyPrevDirection);
        } else {
            nextMove = calculateNextMoveIsBlack(blinkyX, blinkyY, 390, 510, blinkyPrevDirection);
        }
        moveBlinky(nextMove);

        if(pinkyState == 0) {
            if(pacmanDirection == 0) {
                nextMove = calculateNextMoveIsNotGrey(pinkyX, pinkyY, pacmanX, pacmanY - 120, pinkyPrevDirection);
            } else if(pacmanDirection == 1) {
                nextMove = calculateNextMoveIsNotGrey(pinkyX, pinkyY, pacmanX, pacmanY + 120, pinkyPrevDirection);
            } else if(pacmanDirection == 2) {
                nextMove = calculateNextMoveIsNotGrey(pinkyX, pinkyY, pacmanX - 120, pacmanY, pinkyPrevDirection);
            } else if(pacmanDirection == 3) {
                nextMove = calculateNextMoveIsNotGrey(pinkyX, pinkyY, pacmanX + 120, pacmanY, pinkyPrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(pinkyX, pinkyY, 420, 510, pinkyPrevDirection);
        }
        movePinky(nextMove);

        if(inkyState == 0) {
            if(pacmanDirection == 0) {
                nextMove = calculateNextMoveIsNotGrey(inkyX, inkyY,
                        calculateInkyChaseTargetX(0), calculateInkyChaseTargetY(-60), inkyPrevDirection);
            } else if(pacmanDirection == 1) {
                nextMove = calculateNextMoveIsNotGrey(inkyX, inkyY,
                        calculateInkyChaseTargetX(0), calculateInkyChaseTargetY(60), inkyPrevDirection);
            } else if(pacmanDirection == 2) {
                nextMove = calculateNextMoveIsNotGrey(inkyX, inkyY,
                        calculateInkyChaseTargetX(-60), calculateInkyChaseTargetY(0), inkyPrevDirection);
            } else if(pacmanDirection == 3) {
                nextMove = calculateNextMoveIsNotGrey(inkyX, inkyY,
                        calculateInkyChaseTargetX(60), calculateInkyChaseTargetY(0), inkyPrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(inkyX, inkyY, 450, 510, inkyPrevDirection);
        }
        moveInky(nextMove);

        if(clydeState == 0) {
            if(Math.sqrt(Math.pow(clydeX - pacmanX, 2) + Math.pow(clydeY - pacmanY, 2)) > 240) {
                nextMove = calculateNextMoveIsNotGrey(clydeX, clydeY, pacmanX, pacmanY, clydePrevDirection);
            } else {
                nextMove = calculateNextMoveIsBlack(clydeX, clydeY, 0, 1082, clydePrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(clydeX, clydeY, 480, 510, clydePrevDirection);
        }
        moveClyde(nextMove);
    }

    private void moveGhostsScatter() {
        int nextMove;
        if(blinkyState == 0) {
            if(isInsideSpawn(blinkyX, blinkyY)) {
                nextMove = calculateNextMoveIsBlack(blinkyX, blinkyY, 420, 420, blinkyPrevDirection);
            } else {
                nextMove = calculateNextMoveIsBlack(blinkyX, blinkyY, 870, 0, blinkyPrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(blinkyX, blinkyY, 390, 510, blinkyPrevDirection);
        }
        moveBlinky(nextMove);

        if(pinkyState == 0) {
            if(isInsideSpawn(pinkyX, pinkyY)) {
                nextMove = calculateNextMoveIsBlack(pinkyX, pinkyY, 420, 420, pinkyPrevDirection);
            } else {
                nextMove = calculateNextMoveIsBlack(pinkyX, pinkyY, 0, 0, pinkyPrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(pinkyX, pinkyY, 420, 510, pinkyPrevDirection);
        }
        movePinky(nextMove);

        if(inkyState == 0) {
            if(isInsideSpawn(inkyX, inkyY)) {
                nextMove = calculateNextMoveIsBlack(inkyX, inkyY, 420, 420, inkyPrevDirection);
            } else {
                nextMove = calculateNextMoveIsBlack(inkyX, inkyY, 870, 1082, inkyPrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(inkyX, inkyY, 450, 510, inkyPrevDirection);
        }
        moveInky(nextMove);

        if(clydeState == 0) {
            if(isInsideSpawn(clydeX, clydeY)) {
                nextMove = calculateNextMoveIsBlack(clydeX, clydeY, 420, 420, clydePrevDirection);
            } else {
                nextMove = calculateNextMoveIsBlack(clydeX, clydeY, 0, 1082, clydePrevDirection);
            }
        } else {
            nextMove = calculateNextMoveIsBlack(clydeX, clydeY, 480, 510, clydePrevDirection);
        }
        moveClyde(nextMove);
    }

    private void moveGhostsFrightened() {
        int nextMove;
        if(blinkyState == 0) {
            nextMove = calculateNextMoveIsNotGreyRandom(blinkyX, blinkyY, blinkyPrevDirection);
        } else {
            nextMove = calculateNextMoveIsBlack(blinkyX, blinkyY, 390, 510, blinkyPrevDirection);
        }
        moveBlinky(nextMove);

        if(pinkyState == 0) {
            nextMove = calculateNextMoveIsNotGreyRandom(pinkyX, pinkyY, pinkyPrevDirection);
        } else {
            nextMove = calculateNextMoveIsBlack(pinkyX, pinkyY, 420, 510,  pinkyPrevDirection);
        }
        movePinky(nextMove);

        if(inkyState == 0) {
            nextMove = calculateNextMoveIsNotGreyRandom(inkyX, inkyY, inkyPrevDirection);
        } else {
            nextMove = calculateNextMoveIsBlack(inkyX, inkyY, 450, 510, inkyPrevDirection);
        }
        moveInky(nextMove);

        if(clydeState == 0) {
            nextMove = calculateNextMoveIsNotGreyRandom(clydeX, clydeY, clydePrevDirection);
        } else {
            nextMove = calculateNextMoveIsBlack(clydeX, clydeY, 480, 510, clydePrevDirection);
        }
        moveClyde(nextMove);
    }

    private void moveBlinky(int nextMove) {
        if(nextMove == 0) {
            blinkyY -= 30;
            blinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            blinkyY += 30;
            blinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            blinkyX -= 30;
            if(blinkyX < 30) blinkyX = 840;
            blinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            blinkyX += 30;
            if(blinkyX > 840) blinkyX = 30;
            blinkyPrevDirection = 2;
        }
    }

    private void movePinky(int nextMove) {
        if(nextMove == 0) {
            pinkyY -= 30;
            pinkyPrevDirection = 1;
        } else if(nextMove == 1) {
            pinkyY += 30;
            pinkyPrevDirection = 0;
        } else if(nextMove == 2) {
            pinkyX -= 30;
            if(pinkyX < 30) pinkyX = 840;
            pinkyPrevDirection = 3;
        } else if(nextMove == 3) {
            pinkyX += 30;
            if(pinkyX > 840) pinkyX = 30;
            pinkyPrevDirection = 2;
        }
    }

    private void moveInky(int nextMove) {
        if(nextMove == 0) {
            inkyY -= 30;
            inkyPrevDirection = 1;
        } else if(nextMove == 1) {
            inkyY += 30;
            inkyPrevDirection = 0;
        } else if(nextMove == 2) {
            inkyX -= 30;
            if(inkyX < 30) inkyX = 840;
            inkyPrevDirection = 3;
        } else if(nextMove == 3) {
            inkyX += 30;
            if(inkyX > 840) inkyX = 30;
            inkyPrevDirection = 2;
        }
    }

    private void moveClyde(int nextMove) {
        if(nextMove == 0) {
            clydeY -= 30;
            clydePrevDirection = 1;
        } else if(nextMove == 1) {
            clydeY += 30;
            clydePrevDirection = 0;
        } else if(nextMove == 2) {
            clydeX -= 30;
            if(clydeX < 30) clydeX = 840;
            clydePrevDirection = 3;
        } else if(nextMove == 3) {
            clydeX += 30;
            if(clydeX > 840) clydeX = 30;
            clydePrevDirection = 2;
        }
    }

    private int calculateNextMoveIsBlack(int currX, int currY, int targetX, int targetY, int prevDirection) {
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
        for(int i = 1; i < distances.length; i++) {
            if(distances[i] < minVal) {
                minIdx = i;
                minVal = distances[i];
            }
        }
        return minIdx;
    }

    private int calculateNextMoveIsNotGrey(int currX, int currY, int targetX, int targetY, int prevDirection) {
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
        for(int i = 1; i < distances.length; i++) {
            if(distances[i] < minVal) {
                minIdx = i;
                minVal = distances[i];
            }
        }
        return minIdx;
    }

    private int calculateNextMoveIsNotGreyRandom(int currX, int currY, int prevDirection) {
        if(!isGreyTile(currX + 15, currY - 15) || prevDirection == 0) distances[0] = Integer.MAX_VALUE;
        else distances[0] = 0;
        if(!isGreyTile(currX + 15, currY + 45) || prevDirection == 1) distances[1] = Integer.MAX_VALUE;
        else distances[1] = 0;
        if(!isGreyTile(currX - 15, currY + 15) || prevDirection == 2) distances[2] = Integer.MAX_VALUE;
        else distances[2] = 0;
        if(!isGreyTile(currX + 45, currY + 15) || prevDirection == 3) distances[3] = Integer.MAX_VALUE;
        else distances[3] = 0;
        int randIdx = (int) (Math.random() * 4);
        while(distances[randIdx] == Integer.MAX_VALUE) {
            randIdx = (randIdx + 1) % 4;
        }
        return randIdx;
    }

    private int calculateInkyChaseTargetX(int offset) {
        int differenceX = Math.abs(blinkyX - (pacmanX + offset));
        if(blinkyX < pacmanX) {
            return (pacmanX + offset) + differenceX;
        } else {
            return (pacmanX + offset) - differenceX;
        }
    }

    private int calculateInkyChaseTargetY(int offset) {
        int differenceY = Math.abs(blinkyY - (pacmanY + offset));
        if(blinkyY < pacmanY) {
            return (pacmanY + offset) + differenceY;
        } else {
            return (pacmanY + offset) - differenceY;
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

    private boolean isBlackTile(int x, int y) {
        int clr = background.getRGB(x, y);
        Color color = new Color(clr, true);
        boolean r = color.getRed() == 0;
        boolean g = color.getGreen() == 0;
        boolean b = color.getBlue() == 0;
        return r && g && b;
    }

    private boolean isInsideSpawn(int currX, int currY) {
        boolean x = currX >= 390 - 60 && currX <= 480 + 60;
        boolean y = currY >= 510 - 60 && currY <= 510 + 60;
        return x && y;
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

    private void checkPacmanGhostsCollision() {
        Rectangle pacmanRect = pacManRectangle();
        Rectangle blinkyRect = blinkyRectangle();
        Rectangle pinkyRect = pinkyRectangle();
        Rectangle inkyRect = inkyRectangle();
        Rectangle clydeRect = clydeRectangle();
        if((pacmanRect.intersects(blinkyRect) && blinkyState == 0) || (pacmanRect.intersects(pinkyRect) && pinkyState == 0) ||
                (pacmanRect.intersects(inkyRect) && inkyState == 0) || (pacmanRect.intersects(clydeRect)) && clydeState == 0) {
            gameState = 0;
            count = 0;
            lives--;
        }
    }

    private void checkPacmanGhostsCollisionFrightened() {
        Rectangle pacmanRect = pacManRectangle();
        Rectangle blinkyRect = blinkyRectangle();
        Rectangle pinkyRect = pinkyRectangle();
        Rectangle inkyRect = inkyRectangle();
        Rectangle clydeRect = clydeRectangle();
        if(pacmanRect.intersects(blinkyRect) && blinkyState == 0) {
            ghostsEatenCount++;
            highScore += (int) (Math.pow(2, ghostsEatenCount) * 100);
            blinkyState = 1;
        }
        if(pacmanRect.intersects(pinkyRect) && pinkyState == 0) {
            ghostsEatenCount++;
            highScore += (int) (Math.pow(2, ghostsEatenCount) * 100);
            pinkyState = 1;
        }
        if(pacmanRect.intersects(inkyRect) && inkyState == 0) {
            ghostsEatenCount++;
            highScore += (int) (Math.pow(2, ghostsEatenCount) * 100);
            inkyState = 1;
        }
        if(pacmanRect.intersects(clydeRect) && clydeState == 0) {
            ghostsEatenCount++;
            highScore += (int) (Math.pow(2, ghostsEatenCount) * 100);
            clydeState = 1;
        }
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
                gameState = 3;
                count = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(lives <= 0 || (foods.isEmpty() && energizers.isEmpty())) gameState = 4;

        if(gameState == 0) {
            if(pressedKeys[KeyEvent.VK_SPACE]) gameState = 1;

            pacmanX = 420;
            pacmanY = 600;
            pacmanDirection = 3;
            pacmanFrameCount = 1;
            blinkyX = 390;
            blinkyY = 510;
            blinkyPrevDirection = 1;
            blinkyState = 0;
            pinkyX = 420;
            pinkyY = 510;
            pinkyPrevDirection = 1;
            pinkyState = 0;
            inkyX = 450;
            inkyY = 510;
            inkyPrevDirection = 1;
            inkyState = 0;
            clydeX = 480;
            clydeY = 510;
            clydePrevDirection = 1;
            clydeState = 0;
        } else if(gameState == 3) {
            count += 3;

            if(count == 60) {
                gameState = 1;
                count = 0;
                ghostsEatenCount = 0;
            }

            movePacMan();
            pacmanFrameCount = (pacmanFrameCount + 1) % 3;
            moveGhostsFrightened();

            if(isInsideSpawn(blinkyX, blinkyY)) blinkyState = 0;
            if(isInsideSpawn(pinkyX, pinkyY)) pinkyState = 0;
            if(isInsideSpawn(inkyX, inkyY)) inkyState = 0;
            if(isInsideSpawn(clydeX, clydeY)) clydeState = 0;

            checkPacmanGhostsCollisionFrightened();
            checkPacManFoodCollision();
            checkPacManEnergizerCollision();
        } else if(gameState != 4) {
            count += 3;

            if(gameState == 1 && count == 60) {
                gameState = 2;
                count = 0;
            }
            if(gameState == 2 && count == 240) {
                gameState = 1;
                count = 0;
            }

            movePacMan();
            pacmanFrameCount = (pacmanFrameCount + 1) % 3;
            if(gameState == 1) moveGhostsScatter();
            else moveGhostsChase();

            if(isInsideSpawn(blinkyX, blinkyY)) blinkyState = 0;
            if(isInsideSpawn(pinkyX, pinkyY)) pinkyState = 0;
            if(isInsideSpawn(inkyX, inkyY)) inkyState = 0;
            if(isInsideSpawn(clydeX, clydeY)) clydeState = 0;

            checkPacmanGhostsCollision();
            checkPacManFoodCollision();
            checkPacManEnergizerCollision();
        }

        repaint();
    }
}
