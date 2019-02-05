package model;


import ui.FlappyBird;

import javax.swing.*;
import java.awt.*;

/**
 *  Class for the bird in the Flappy Bird game
 */

public class Bird {

    /**
     *  Declarations
     */
    private Image birdHorizImage;
    private Image image;
    private ImageIcon ii;

    /**
     * Resources
     */
    private void loadImage() {
        ii = new ImageIcon("src/resources/flappyBirdImgHorizSmall.png");
        birdHorizImage = ii.getImage();

        image = birdHorizImage;
    }

    /**
     *  Constants
     */
    private final int birdWidth = 30;
    private final int birdHeight = 20;
    private final int gravity = 1;
    private final int jumpSpeed = 12;
    private final int terminalVelocity = 15;


    /**
     *  Variables
     */
    private static int x;
    private static int y;
    private static int dy;

    /**
     *  Constructor
     */
    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        dy = 0;
        loadImage();
    }

    /**
     *  Getters
     */
    public int getX() {return x;}
    public int getY() {return y;}
    public int getYVel() {return dy;}
    public int getWidth() {return birdWidth;}
    public int getHeight() {return birdHeight;}

    /**
     *  Setters
     */
    public void setX(int x)   { this.x = x; }
    public void setY(int y)   { this.y = y; }
    public void setDY(int dy) { this.dy = dy; }

    // MODIFIES: this
    // EFFECTS: increases y value by dy, increases dy by gravity if under terminal velocity
    public void fall() {
        y += dy;
        if (dy < terminalVelocity) {
            dy += gravity;
        }
    }

    public void drawBird(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    // MODIFIES: this
    // EFFECTS: changes positive downward velocity into negative upward velocity
    public void jump() {
        dy = -jumpSpeed;
    }


    // EFFECTS: produces true when bird touching ground
    public boolean hitGround() {
        int floorHeight = FlappyBird.flappyBird.getFloorHeight();
        int windowHeight = FlappyBird.flappyBird.getWindowHeight();
        return y >= windowHeight - floorHeight - birdHeight;
    }

    // MODIFIES: this
    // EFFECTS: changes positive downward velocity into negative upward velocity
    public void birdDies() {
        int floorHeight = FlappyBird.flappyBird.getFloorHeight();
        int windowHeight = FlappyBird.flappyBird.getWindowHeight();
        y = windowHeight - floorHeight - birdHeight;
    }

}
