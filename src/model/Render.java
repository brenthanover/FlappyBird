package model;


import ui.FlappyBird;

import javax.swing.*;
import java.awt.*;

public class Render extends JPanel {

    /**
     *  Constructor
     */
    public Render() { }

    // EFFECTS: renders the draw file for FlappyBird
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FlappyBird.flappyBird.draw(g);
    }
}
