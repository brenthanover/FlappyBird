package parsers;

import ui.FlappyBird;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class LoadGame {

    // EFFECTS: converts text file in the style 'int.' (repeated) to
    //          integer array list of integers separated by '.' in text file
    public  static ArrayList<Integer> decodeSaveGame(String saveData) {
        ArrayList<Integer> decoded = new ArrayList<>();
        String dataString = "";

        for (char c : saveData.toCharArray()) {
            if (c == '.') {
                decoded.add(Integer.parseInt(dataString));
                dataString = "";
            } else {
                dataString += c;
            }
        }

        return decoded;
    }

    // EFFECTS: takes array list and decodes it to produce FlappyBird game state
    public static FlappyBird decodeArray(ArrayList<Integer> decodedData) {
        int score = decodedData.get(0);
        int highScore = decodedData.get(1);
        int birdX = decodedData.get(2);
        int birdY = decodedData.get(3);
        int birdDY = decodedData.get(4);

        // initialize loadedFlappyBird and set parameters
        FlappyBird loadedFlappyBird = new FlappyBird(highScore);
        loadedFlappyBird.setScore(score);
        loadedFlappyBird.newBird(birdX, birdY, birdDY);

        // add pipes to pipeList in loadedFlappyBird
        for (int i = 5; i < decodedData.size(); i+= 2) {
            loadedFlappyBird.addPipe(decodedData.get(i), decodedData.get(i++));
        }

        return loadedFlappyBird;
    }

    // EFFECTS: loads saved data from text file and produces FlappyBird game state
    public static FlappyBird loadGame(String fileName) {
        FlappyBird loadedFlappyBird = new FlappyBird(0);
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String data = br.readLine();
            ArrayList<Integer> decodedData = decodeSaveGame(data);
            loadedFlappyBird = decodeArray(decodedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedFlappyBird;
    }
}
