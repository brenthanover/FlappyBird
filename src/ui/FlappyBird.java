package ui;

import model.Bird;
import model.Pipe;
import model.Render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird implements ActionListener, KeyListener {

    /**
     * Constants
     */
    private static final int windowWidth  = 600;
    private static final int windowHeight = 800;
    private static final int floorHeight  = 100;
    private static final int pipeSpacing  = 60;

    /**
     * Variables
     */
    private int ticks = 1;
    private int score = 0;
    private boolean gameOver    = false;
    private boolean gameStarted = false;

    /**
     *  Getters
     */
    public int getWindowHeight() { return windowHeight; }
    public int getFloorHeight()  { return floorHeight; }
    public int getWindowWidth()  { return windowWidth; }

    /**
     * Declarations
     */
    private Render render;
    private static Bird bird;
    private ArrayList<Pipe> pipeList;
    public static FlappyBird flappyBird;

    /**
     * Constructor
     */
    private FlappyBird() {
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        render = new Render();

        jFrame.add(render);
        jFrame.setResizable(false);
        jFrame.addKeyListener(this);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(windowWidth, windowHeight);
        jFrame.setTitle("Flappy Bird");
        jFrame.setVisible(true);

        bird = new Bird();
        pipeList = new ArrayList<>();

        timer.start();

    }


    // REQUIRES: gameStarted = true
    // MODIFIES: bird, pipes in pipeList, this
    // EFFECTS: moves and colours bird and pipes
    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;

        // actions for pipes and bird when game is in action
        if (gameStarted) {
            // make bird fall unless it hits ground, then game over
            if (bird.hitGround()) {
                gameOver = true;
                bird.birdDies();
            } else {
                bird.fall();
            }

            // scroll pipes, manage game status based on if bird hits pipes
            for (int i = 0; i < pipeList.size(); i++) {
                Pipe pipe = pipeList.get(i);
                pipe.scroll();
                if (pipe.hitsSide(bird)) {
                    gameOver = true;
                    bird.setX(pipe.getX() - bird.getWidth());
                }
                if (pipe.hitsBottomPipe(bird) || pipe.hitsTopPipe(bird)) {
                    gameOver = true;
                }
                if (passPipe(pipe) && !gameOver) {
                    score++;
                }
            }

            for (int i = 0; i < pipeList.size(); i++) {
                Pipe pipe = pipeList.get(i);
                if (pipe.outOfBounds()) {
                    pipeList.remove(pipe);
                }
            }

            if (ticks % pipeSpacing == 0) {
                flappyBird.addPipe();
            }
        }

        // color game pieces
        render.repaint();
    }

    // REQUIRES: key pressed: spacebar
    // MODIFIES: this
    // EFFECTS: causes bird to jump on spacebar hit
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && !gameOver) {
            gameStarted = true;
            bird.jump();
        }
    }

    // MODIFIES:
    // EFFECTS:
    public void draw(Graphics g) {
        // background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, windowWidth, windowHeight);
        // pipes
        for (int i = 0; i < pipeList.size(); i++) {
            Pipe pipe = pipeList.get(i);
            pipe.drawPipe(g, windowHeight);
        }
        // bird
        bird.drawBird(g);
        // ground
        g.setColor(Color.orange);
        g.fillRect(0, windowHeight - floorHeight, windowWidth, floorHeight);
        // grass
        g.setColor(Color.green);
        g.fillRect(0, windowHeight - floorHeight, windowWidth, 20);

        // texts
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 80));
        // place score at top centre of screen
        g.drawString("" + score, windowWidth/2 - 20, 70);
        // game over message
        if (gameOver) {
            g.drawString("GAME OVER", 50, windowHeight / 2 - 50);
        }
        // beginning message
        if (!gameStarted) {
            g.drawString("PRESS SPACE", 10, windowHeight / 2 - 50);
        }
    }


    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public void addPipe() {
        pipeList.add(new Pipe());
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public boolean passPipe(Pipe pipe) {
        return bird.getX() == pipe.getX() + pipe.getPipeWidth();
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public static void main(String[] args) {
        flappyBird = new FlappyBird();

    }


}
