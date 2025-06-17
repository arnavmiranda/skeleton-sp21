package byow.Core;

import byow.TileEngine.*;

public class Game {
    public static void main(String args[]) {
        Engine engine = new Engine();
        engine.interactWithInputString(args[0]);
    }
}
