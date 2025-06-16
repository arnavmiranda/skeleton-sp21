package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /**
     * The width of the window of this game.
     */
    private int width;
    /**
     * The height of the window of this game.
     */
    private int height;
    /**
     * The current round the user is on.
     */
    private int round;
    /**
     * The Random object used to randomly generate Strings.
     */
    private Random rand;
    /**
     * Whether or not the game is over.
     */
    private boolean gameOver;
    /**
     * Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'.
     */
    private boolean playerTurn;
    /**
     * The characters we generate random Strings from.
     */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /**
     * Encouraging phrases. Used in the last section of the spec, 'Helpful UI'.
     */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder string = new StringBuilder();
        int randomInt;
        for (int i = 0; i < n; i++) {
            randomInt = rand.nextInt(CHARACTERS.length);
            string.append(CHARACTERS[randomInt]);
        }
        return string.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear();

        if(playerTurn) {
            StdDraw.line(0, height - 3, width, height - 3);
            StdDraw.text(4, height - 2, "Round: " + round);
        }
        if(!s.isBlank()) {
            StdDraw.text((double) width / 2, (double) height / 2, s);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(Character.toString(letters.charAt(i)));
            StdDraw.pause(1000);

            StdDraw.clear();
            StdDraw.show();
            StdDraw.pause(500);
        }
        playerTurn = true;
        drawFrame("");
    }

    public String solicitNCharsInput(int n) {

        String word = "";
        int count = 0;
        while (count < n) {
            while (StdDraw.hasNextKeyTyped()) {
                word += Character.toString(StdDraw.nextKeyTyped());
                count++;
                drawFrame(word);
            }
        }
        return word;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        String string;
        String guess;
        round = 2;
        gameOver = false;
        playerTurn = false;

        while (!gameOver) {
            drawFrame("Round: " + round);
            StdDraw.pause(1000);

            StdDraw.clear();
            StdDraw.show();
            StdDraw.pause(1000);

            string = generateRandomString(round);
            flashSequence(string);

            guess = solicitNCharsInput(round);
            playerTurn = false;

            if (guess.equals(string)) {
                round++;
            } else {
                drawFrame("GAME OVER");
                StdDraw.pause(1000);

                drawFrame("You made it to: ROUND " + round);
                StdDraw.pause(1000);
                StdDraw.clear();
                StdDraw.show();
                gameOver = true;
            }
        }
    }
}
