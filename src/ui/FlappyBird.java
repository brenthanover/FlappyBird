package ui;

import model.Background;
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
import java.util.Random;

import static parsers.LoadGame.loadGame;
import static parsers.SaveGame.saveGame;

public class FlappyBird implements ActionListener, KeyListener {

    /**
     * Constants
     */
    private static final int windowWidth  = 600;
    private static final int windowHeight = 800;
    private static final int floorHeight  = 100;
    private static final int pipeSpacing  = 60;
    private final int birdXStart = 200;
    private final int birdYStart = 200;
    private final int pipeMinHeight = 350;
    private final int pipeMaxHeight = 0;

    /**
     * Variables
     */
    private int ticks = 1;
    private int score = 0;
    private int highScore = 0;
    private boolean canFall     = true;
    private boolean gameOver    = false;
    private boolean gameStarted = false;

    /**
     *  Getters
     */
    public int getWindowHeight() { return windowHeight; }
    public int getFloorHeight()  { return floorHeight; }
    public int getWindowWidth()  { return windowWidth; }
    public int getHighScore() { return highScore; }
    public int getScore() { return score; }

    /**
     *  Setters
     */
    public void setScore(int s) { score = s; }

    /**
     * Declarations
     */
    private Render render;
    private static Bird bird;
    private static Background background;
    private ArrayList<Pipe> pipeList;
    public static FlappyBird flappyBird;

    /**
     * Constructor
     */
    public FlappyBird(int highScore) {
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

        bird = new Bird(birdXStart, birdYStart);
        background = new Background();
        pipeList = new ArrayList<>();
        this.highScore = highScore;

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
            } else if (canFall) {
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
                if (pipe.glidePipe(bird)) {
                    gameOver = true;
                    canFall = false;
                }
                if (passPipe(pipe)) {
                    canFall = true;
                    if (!gameOver) {
                        score++;
                    }
                }
            }

            // remove pipes that travel out of bounds
            for (int i = 0; i < pipeList.size(); i++) {
                Pipe pipe = pipeList.get(i);
                if (pipe.outOfBounds()) {
                    pipeList.remove(pipe);
                }
            }

            // add new pipe every pipeSpacing ticks
            if (ticks % pipeSpacing == 0) {
                Random rand = new Random();

                int px = windowWidth;
                int py = rand.nextInt(windowHeight - pipeMinHeight) + pipeMaxHeight;
                addPipe(px, py);
            }
        }

        // render game pieces
        render.repaint();
    }

    // REQUIRES: key pressed
    // MODIFIES: this
    // EFFECTS: changes game state based on key pressed
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // jump by pressing spacebar
        if (keyCode == KeyEvent.VK_SPACE && !gameOver && gameStarted) {
            bird.jump();
        }

        // reset game if game is over by pressing Y
        if (keyCode == KeyEvent.VK_Y && gameOver) {
            flappyBird.reset();;
        }

        // pause game when p is pressed
        if (keyCode == KeyEvent.VK_P && gameStarted && !gameOver) {
            gameStarted = false;
        }

        // unpause game with spacebar
        if (keyCode == KeyEvent.VK_SPACE && !gameStarted && !gameOver) {
            gameStarted = true;
        }

        // save game when enter is pressed
        if (keyCode == KeyEvent.VK_ENTER && !gameStarted && !gameOver) {
            saveGame("src/parsers/saveFile", flappyBird, bird, pipeList);
        }

        // save game when enter is pressed
        if (keyCode == KeyEvent.VK_BACK_SPACE && !gameStarted && !gameOver) {
            flappyBird = loadGame("src/parsers/saveFile");
        }
    }

    // EFFECTS: renders all game parts - background, bird, pipes, messages
    public void draw(Graphics g) {
        // background
        background.drawBackground(g);
        // pipes
        for (int i = 0; i < pipeList.size(); i++) {
            Pipe pipe = pipeList.get(i);
            pipe.drawPipe(g, windowHeight);
        }
        // bird
        bird.drawBird(g);

        // texts
        // place high score at top right
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 20));
        g.drawString("HIGH SCORE: " + highScore, windowWidth - 200, 20);
        g.drawString("PRESS P TO PAUSE", 0, 20);
        // place score at top centre of screen
        g.setFont(new Font("Arial", 1, 80));
        g.drawString("" + score, windowWidth/2 - 30, 70);
        // game over message
        if (gameOver) {
            g.setFont(new Font("Arial", 1, 80));
            g.drawString("GAME OVER", 50, windowHeight / 2 - 50);
            g.setFont(new Font("Arial", 1, 20));
            g.drawString("PLAY AGAIN? Y/N", 200, windowHeight/2);
        }
        // beginning message
        if (!gameStarted) {
            g.setFont(new Font("Arial", 1, 80));
            g.drawString("FLAPPY BIRD", 30, windowHeight / 2 - 50);
            g.setFont(new Font("Arial", 1, 20));
            g.drawString("PRESS SPACE TO START", 170, windowHeight/2);
            g.drawString("PRESS ENTER WHEN PAUSED TO SAVE", 100, windowHeight/2+ 40);
            g.drawString("PRESS BACKSPACE WHEN PAUSED TO LOAD", 80, windowHeight/2 + 80);

        }
    }

    // REQUIRES: pipe
    // EFFECTS: returns true if bird successfully passes through a set of pipes
    private boolean passPipe(Pipe pipe) { return bird.getX() == pipe.getX() + pipe.getPipeWidth(); }

    // REQUIRES: gameOver = true
    // MODIFIES: all
    // EFFECTS:  resets game back to beginning, records new high score
    public void reset() {
        if (highScore < score) {
            highScore = score;
        }
        gameStarted = false;
        gameOver = false;
        flappyBird = new FlappyBird(highScore);
    }

    //EFFECTS: adds new pipe to pipeList at x, y values
    public void addPipe(int x, int y) {
        pipeList.add(new Pipe(x, y));
    }

    // EFFECTS: creates new bird at x, y, with velocity dy
    public void newBird(int x, int y, int dy) {
        bird.setX(x);
        bird.setY(y);
        bird.setDY(dy);
    }


    // EFFECTS: main function for FlappyBird
    public static void main(String[] args) {
        flappyBird = new FlappyBird(0);
        String gameData = "";
        gameData += Integer.toString(-10);
        System.out.println(gameData);
    }


}
