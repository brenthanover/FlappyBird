package model;

import ui.FlappyBird;

import java.awt.*;

public class Pipe {

    /**
     *  Constants
     */
    private final int pipeWidth = 60;
    private final int pipeGap = 120;
    private final int scrollSpeed = 5;
    int windowHeight = FlappyBird.flappyBird.getWindowHeight();

    /**
     *  Variables
     */
    private int x;
    private int y;

    /**
     *  Declarations
     */
    public static Pipe pipe;

    /**
     *  Constructor
     */
    public Pipe(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *  Getters
     */
    public int getX() {return x;}
    public int getY() {return y;}
    public int getPipeWidth() {return pipeWidth;}
    public int getPipeGap() {return pipeGap;}

    // MODIFIES: this
    // EFFECTS: reduces x by scrollSpeed
    public void scroll() {
        x -= scrollSpeed;
    }

    // REQUIRES: bird
    // MODIFIES: this
    // EFFECTS: produce true if bird intersects pipe
    public boolean hitsSide(Bird bird) {
        int by = bird.getY();
        int bx = bird.getX();

        boolean topPipe = (-100 <= by && by <= y);
        boolean bottomPipe = (y + pipeGap - bird.getHeight() <= by) && (by <= windowHeight);
        boolean xLocation = x >= bx && x <= bx + bird.getWidth();

        return xLocation && (topPipe || bottomPipe);
    }

    // REQUIRES: bird
    // MODIFIES: this
    // EFFECTS: produce true if bird intersects pipe
    public boolean hitsTopPipe(Bird bird) {
        int by = bird.getY();
        int bx = bird.getX();

        boolean topPipe = by + bird.getHeight() >= y && by <= y;
        boolean xLocation = bx <= x + pipeWidth && bx >= x - bird.getWidth();

        return xLocation && topPipe;
    }

    // REQUIRES: bird
    // MODIFIES: this
    // EFFECTS: produce true if bird intersects pipe
    public boolean hitsBottomPipe(Bird bird) {
        int by = bird.getY();
        int bx = bird.getX();

        boolean bottomPipe = by + bird.getHeight() >= y + pipeGap && by <= y + pipeGap;
        boolean xLocation = bx <= x + pipeWidth && bx >= x - bird.getWidth();

        return xLocation && bottomPipe;
    }

    // EFFECTS: produces true when pipe is out of frame
    public boolean outOfBounds() {
        return x <= -pipeWidth;
    }

    // EFFECTS: renders pipe on screen
    public void drawPipe(Graphics g, int windowHeight) {
        g.setColor(Color.green.darker());
        g.fillRect(x, 0, pipeWidth, y);
        int distToSecond = y + pipeGap;
        g.setColor(Color.green.darker());
        g.fillRect(x, distToSecond, pipeWidth, windowHeight - distToSecond - 100);
    }

    public boolean glidePipe(Bird bird) {
        int by = bird.getY();
        int bx = bird.getX();

        boolean bottomPipe = by + bird.getHeight() >= y + pipeGap && by <= y + pipeGap;
        boolean xLocation = bx <= x + pipeWidth && bx >= x;

        return xLocation && bottomPipe;
    }
}
