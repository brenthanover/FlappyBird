package model;

import javax.swing.*;
import java.awt.*;

public class Background {

    /**
     *  Constants
     */
    private int x = 0;
    private int y = -92;

    /**
     *  Declarations
     */
    private Image image;
    private ImageIcon ii;

    /**
     * Resources
     */
    private void loadImage() {
        ii = new ImageIcon("src/resources/flappyBirdBackground.png");
        image = ii.getImage();
    }

    /**
     *  Constructor
     */
    public Background() {
        loadImage();
    }

    //EFFECTS: renders background
    public void drawBackground(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}
