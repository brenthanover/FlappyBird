package parsers;

import model.Bird;
import model.Pipe;
import ui.FlappyBird;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class SaveGame {

    // REQUIRES: active FlappyBird game
    // MODIFIES; text file saveFile
    // EFFECTS: saves current game variables as text file, each separated by '.'
    public static void saveGame(String fileName, FlappyBird flappyBird, Bird bird, ArrayList<Pipe> pipeList) {
        String gameData = "";

        // saves pieces of game data as a string, each separated by a '.'
        gameData += flappyBird.getScore() + ".";
        gameData += flappyBird.getHighScore() + ".";
        gameData += bird.getX() + ".";
        gameData += bird.getY() + ".";
        gameData += bird.getYVel() + ".";
        for (Pipe pipe : pipeList) {
            gameData += pipe.getX() + ".";
            gameData += pipe.getY() + ".";
        }
        System.out.println(gameData);

        // write the game data to file
        try {
            File file = new File(fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(gameData);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
