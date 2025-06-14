package byow.Core;

import byow.TileEngine.*;

import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 120;
    public static final int HEIGHT = 60;
    public static int seed;
    public static Random random;
    public static TETile[][] tiles;
    public static final int SAFETY_FACTOR = 3;
    public static final int HEIGHT_FACTOR = HEIGHT / 5;
    public static final int WIDTH_FACTOR = WIDTH / 10;
    public static final int HALLWAY_COLOR = 50;
    public static int[] quickFind;
    public static TreeMap<Integer, Room> roomMap = new TreeMap<>();
    private static int counter;

    public int determineNumberOfRooms(int height, int width) {
        int area = height * width;
            return area / 100;
    }

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

        counter = 0;
        drawRooms();
        initializeDS();

        counter = 0;
        while (!allRoomsConnected()) {
            if(counter > 10000) break;
            connectRooms();
        }
        ter.renderFrame(tiles);
        return tiles;
    }


    private void initializeDS() {
        quickFind = new int[roomMap.size()];
        for(int i = 0; i < quickFind.length; i++) {
            quickFind[i] = i;
        }
    }

    public void drawRooms() {
        int c = 0;
        int num = determineNumberOfRooms(HEIGHT, WIDTH);
        while (num > 0) {

            if(counter > 10000) {
                break;
            }
            //default constructor generates random position WITHIN the grid (not boundary)
            Position p = new Position();
            Room room = new Room(p);
            room.height = 1 + random.nextInt(HEIGHT_FACTOR);
            room.depth = 1 + random.nextInt(HEIGHT_FACTOR);
            room.left = 1 + random.nextInt(WIDTH_FACTOR);
            room.right = 1 + random.nextInt(WIDTH_FACTOR);

            fitRoom(room, tiles);
            if (roomOverlap(room, tiles)) {
                counter++;
                continue;
            }
            room.putFloor(tiles);
            room.putWall(tiles);
            num = num - 1;
            roomMap.put(c++, room);
            counter = 0;
        }
    }

    public void combine(int a, int b) {
        int root = Math.min(quickFind[a], quickFind[b]);
        for(int i = 0; i < quickFind.length; i++) {
            if(quickFind[i] == quickFind[a] || quickFind[i] == quickFind[b]) {
                quickFind[i] = root;
            }
        }
    }

    public void connectRooms() {
        int a = random.nextInt(roomCount());
        int b = random.nextInt(roomCount());
        System.out.println(a + " " + b);
        if (quickFind[a] == quickFind[b]) {
            return;
        }

        Room first = roomMap.get(a);
        Room second = roomMap.get(b);

        for (Position start : first.entryPoints()) {
            for (Position end : second.entryPoints()) {
                if (straightPathPossible(start, end)) {
                    makeStraightPath(start, end);
                    combine(a, b);
                    counter = 0;
                    return;
                } else {
                    boolean adjust = random.nextBoolean();
                    if (adjust) return;
                    adjust = random.nextBoolean();
                    if (adjust) return;
                    Position corner = LpathPossible(start, end);
                    if (corner != null) {
                        makeLpath(start, end, corner);
                        combine(a, b);
                        counter = 0;
                        return;
                    }
                }
            }
        }
        counter++;
    }

    private void makeStraightPath(Position a, Position b) {
        int dx = b.x() - a.x();
        int dy = b.y() - a.y();
        if(dx == 0) {
            makeVerticalPath(a, b);
            return;
        }
        if(dy == 0) {
            makeHorizontalPath(a, b);
        }
    }

    private void makeHorizontalPath(Position a, Position b) {
        Position right = b.x() > a.x() ? b : a;
        Position left = b.x() > a.x() ? a : b;

        TETile hall = TETile.colorVariant(Tileset.FLOOR, 100, 100, 100, random);
        tiles[left.x()][left.y()] = hall;
        tiles[right.x()][right.y()] = hall;

        TETile wall = Tileset.WALL;
        wall = TETile.colorVariant(wall, HALLWAY_COLOR, HALLWAY_COLOR, HALLWAY_COLOR, random);
        for(int x = left.x() + 1; x < right.x(); x++) {
            tiles[x][a.y() + 1] = wall;
            tiles[x][a.y()] = Tileset.FLOOR;
            tiles[x][a.y() - 1] = wall;
        }
    }

    private void makeVerticalPath(Position a, Position b) {
        Position up = b.y() > a.y() ? b : a;
        Position down = b.y() > a.y() ? a : b;

        TETile hall = TETile.colorVariant(Tileset.FLOOR, 100, 100, 100, random);
        tiles[down.x()][down.y()] = hall;
        tiles[up.x()][up.y()] = hall;

        TETile wall = Tileset.WALL;
        wall = TETile.colorVariant(wall, HALLWAY_COLOR, HALLWAY_COLOR, HALLWAY_COLOR, random);

        for(int y = down.y() + 1; y < up.y(); y++) {
            tiles[a.x() - 1][y] = wall;
            tiles[a.x()][y] = hall;
            tiles[a.x() + 1][y] = wall;
        }
    }

    public int roomCount() {
        return roomMap.size();
    }

    public boolean allRoomsConnected() {
        int v = quickFind[random.nextInt(quickFind.length)];
        for(int i : quickFind) {
            if(i != v) {
                return false;
            }
        }
        return true;
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

        for(int x = xstart; x <= xend + 1; x++) {
            for(int y = ystart; y <= yend + 1; y++) {
                if(!tiles[Math.min(x, WIDTH - 1)][Math.min(y, HEIGHT - 1)].equals(Tileset.NOTHING)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class Position {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Position() {
            int a = 0, b = 0;
            while(a == 0 || a == 1) {
                a = random.nextInt(WIDTH - 1);
            }
            while(b == 0 || b == 1) {
                b = random.nextInt(HEIGHT - 1);
            }
            x = a;
            y = b;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
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
                    tiles[x][y] = TETile.colorVariant(tile, 1000, 1000, 1000, random);
                }
            }
        }

        public void putWall(TETile[][] tiles) {

            int xstart = node.x() - left - 1;
            int ystart = node.y() - depth - 1;
            int xend = node.x() + right + 1;
            int yend = node.y() + height + 1;

            int color = HALLWAY_COLOR * 2;
            TETile tile = TETile.colorVariant(Tileset.WALL, color, color, color, random);
            for (int x = xstart; x <= xend; x++) {
                tiles[x][ystart] = tile;
                tiles[x][yend] = tile;
            }
            for(int y = ystart; y <= yend; y++) {
                tiles[xstart][y] = tile;
                tiles[xend][y] = tile;
            }
        }

        private int horizontalRange() {
            int x;
            while (true) {
                x = random.nextInt(node.x() + right);
                if (x > node.x() - left) {
                    return x;
                }
            }
        }
        private int verticalRange() {
            int y;
            while (true) {
                y = random.nextInt(node.y() + height);
                if (y > node.y() - depth) {
                    return y;
                }
            }
        }
        public LinkedList<Position> entryPoints() {
            LinkedList<Position> L = new LinkedList<>();
            Position upperEntrance = new Position(horizontalRange(), node.y() + height + 1);
            Position lowerEntrance = new Position(horizontalRange(), node.y() - depth - 1);
            Position leftEntrance = new Position(node.x() - left - 1, verticalRange());
            Position rightEntrance = new Position(node.x() + right + 1, verticalRange());

            if(node.y() + height + SAFETY_FACTOR < HEIGHT) {
                L.add(upperEntrance);
            }
            if(node.y() - depth - SAFETY_FACTOR > 0) {
                L.add(lowerEntrance);
            }
            if(node.x() + right + SAFETY_FACTOR < WIDTH) {
                L.add(rightEntrance);
            }
            if(node.x() - left - SAFETY_FACTOR > 0) {
                L.add(leftEntrance);
            }

            for(int i = 0; i < 2 * L.size(); i++) {
                int id = random.nextInt(L.size());
                Position temp = L.get(id);
                L.remove(id);
                L.addLast(temp);
            }
            return L;
        }
    }

    public boolean straightPathPossible(Position a, Position b) {
        int dx = b.x() - a.x();
        int dy = b.y() - a.y();
        if(dx == 0) {
            return checkVerticalPath(a, b);
        }
        if(dy == 0) {
            return checkHorizontalPath(a, b);
        }
        return false;
    }

    private boolean checkVerticalPath(Position a, Position b) {
        //entrance node should be the wall entrance
        Position up = b.y() > a.y() ? b : a;
        Position down = b.y() > a.y() ? a : b;
        if(down.x() >= tiles.length - 1 || down.x() < 1) {
            return false;
        }
        for(int x = down.x() - 1; x <= down.x() + 1; x++) {
            for(int y = down.y() + 1; y < up.y(); y++) {
                if(tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkHorizontalPath(Position a, Position b) {
        Position right = b.x() > a.x() ? b : a;
        Position left = b.x() > a.x() ? a : b;
        if(left.y() + 1 >= HEIGHT || left.y() - 1 < 0) {
            return false;
        }
        for(int y = left.y() - 1; y <= left.y() + 1; y++) {
            for(int x = left.x() + 1; x < right.x(); x++) {
                if(tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    public Position LpathPossible(Position a, Position b) {
        Position guess1 = new Position(a.x(), b.y());
        Position guess2 = new Position(b.x(), a.y());

        if (straightPathPossible(a, guess1) && straightPathPossible(guess1, b)) {
            return guess1;
        }
        if (straightPathPossible(a, guess2) && straightPathPossible(guess2, b)) {
            return guess2;
        }
        return null;
    }

    private void changeCornerTile(int x, int y, TETile wall) {

        if(tiles[x][y] == Tileset.NOTHING) {
            tiles[x][y] = wall;
        }
        if(tiles[x][y] == Tileset.FLOOR) {
            tiles[x][y] = wall;
        }
    }

    public void makeLpath(Position a, Position b, Position corner) {
        TETile wall = Tileset.WALL;
        wall = TETile.colorVariant(wall, HALLWAY_COLOR, HALLWAY_COLOR, HALLWAY_COLOR, random);
        for(int x = corner.x() - 1; x <= corner.x() + 1; x++) {
            changeCornerTile(x, corner.y() + 1, wall);
            changeCornerTile(x, corner.y() - 1, wall);
            if(x == corner.x()) continue;
            changeCornerTile(x, corner.y(), wall);
        }

        makeStraightPath(a, corner);
        makeStraightPath(b, corner);
    }
}