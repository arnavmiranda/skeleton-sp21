package byow.Core;

import byow.TileEngine.*;
import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 128;
    public static final int HEIGHT = 64;
    public static int seed;
    public static Random random;
    public static TETile[][] tiles;

    public static TreeMap<Integer, Room> roomMap = new TreeMap<>();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww"). The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        ter.initialize(WIDTH, HEIGHT);
        tiles = new TETile[WIDTH][HEIGHT];
        fillWorldWithNothing(tiles);

        seed = extractSeed(input);
        random = new Random(seed);

        int c = 0;
        int num = determineNumberOfRooms(HEIGHT, WIDTH);
        while(num > 0) {

            Position p = new Position();
            Room room = new Room(p);
            room.height = random.nextInt(HEIGHT / 4);
            room.depth = random.nextInt(HEIGHT / 4);
            room.left = random.nextInt(WIDTH / 4);
            room.right = random.nextInt(WIDTH / 4);

            fitRoom(room, tiles);
            if(roomOverlap(room, tiles)) {
                continue;
            }
            room.putFloor(tiles);
            room.putWall(tiles);
            num = num - 1;
            roomMap.put(c++, room);
        }
        ter.renderFrame(tiles);
        return tiles;
    }

    public int extractSeed(String inp) {
        String val = inp.toLowerCase();
        val = val.replace('n', ' ').replace('s', ' ');
        val = val.trim();
        return Integer.parseInt(val);
    }

    public void fillWorldWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void fitRoom(Room room, TETile[][] tiles) {
        //needs to leave 1 gap for the wall
        int height = tiles[0].length - 1;
        int width = tiles.length - 1;

        int x = room.node.x();
        int y = room.node.y();

        int h = room.height;
        int d = room.depth;
        int l = room.left;
        int r = room.right;

        if (y + h > height - 1) {
            room.height = height - 1 - y;
        }
        if (y - d < 1) {
            room.depth = y - 1;
        }
        if (x + r > width - 1) {
            room.right = width - 1 - x;
        }
        if (x - l < 1) {
            room.left = x - 1;
        }
    }

    public boolean roomOverlap(Room room, TETile[][] tiles) {
        //adding 1 to all boundaries to ensure wall checking is done as well

        int xstart = room.node.x() - room.left - 1;
        int ystart = room.node.y() - room.depth - 1;
        int xend = room.node.x() + room.right + 1;
        int yend = room.node.y() + room.height + 1;

        for(int x = xstart; x <= xend; x++) {
            for(int y = ystart; y <= yend; y++) {
                if(!tiles[x][y].equals(Tileset.NOTHING)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int determineNumberOfRooms(int height, int width) {
        int area = height * width;
        return area / 1000;
    }


    public static class Position {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Position() {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            this.x = x;
            this.y = y;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }

        public Position shift(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }
    }

    public static class Room {
        public Position node;
        public int height = 0, depth = 0, left = 0, right = 0;

        Room(Position p) {
            node = p;
        }

        public void putFloor(TETile[][] tiles) {
            int xstart = node.x() - left;
            int ystart = node.y() - depth;
            int xend = node.x() + right;
            int yend = node.y() + height;

            TETile tile = Tileset.FLOOR;
            for (int x = xstart; x <= xend; x++) {
                for (int y = ystart; y <= yend; y++) {
                    tiles[x][y] = TETile.colorVariant(tile, 200, 200, 200, random);
                }
            }
        }

        public void putWall(TETile[][] tiles) {

            int xstart = node.x() - left - 1;
            int ystart = node.y() - depth - 1;
            int xend = node.x() + right + 1;
            int yend = node.y() + height + 1;

            TETile tile = TETile.colorVariant(Tileset.WALL, 50, 50, 50, random);
            for (int x = xstart; x <= xend; x++) {
                tiles[x][ystart] = tile;
                tiles[x][yend] = tile;
            }
            for(int y = ystart; y <= yend; y++) {
                tiles[xstart][y] = tile;
                tiles[xend][y] = tile;
            }
        }
    }
}
