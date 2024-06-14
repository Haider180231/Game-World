package GameWorld;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String filePath = "res/mansion.txt";
            String imageOutputPath = "res/world.png";
            GameWorld world = MansionParser.parseMansion(filePath);
            world.displayMap(imageOutputPath);  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
