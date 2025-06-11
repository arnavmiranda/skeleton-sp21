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

    public static int[] quickFind;
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

        drawRooms();

        while(!allRoomsConnected()) {
            connectRooms();
        }
        ter.renderFrame(tiles);
        return tiles;
    }

    public void drawRooms() {
        int c = 0;
        int num = determineNumberOfRooms(HEIGHT, WIDTH);
        while (num > 0) {

            Position p = new Position();
            Room room = new Room(p);
            room.height = random.nextInt(HEIGHT / 3);
            room.depth = random.nextInt(HEIGHT / 3);
            room.left = random.nextInt(WIDTH / 3);
            room.right = random.nextInt(WIDTH / 3);

            fitRoom(room, tiles);
            if (roomOverlap(room, tiles)) {
                continue;
            }
            room.putFloor(tiles);
            room.putWall(tiles);
            num = num - 1;
            roomMap.put(c++, room);
        }
        quickFind = new int[roomMap.size()];
        for(int i = 0; i < quickFind.length; i++) {
            quickFind[i] = i;
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
            if(a == b) return;
            Room first = roomMap.get(a);
            Room second = roomMap.get(b);
            for(Position start : first.entryPoints()) {
                for(Position end: second.entryPoints()) {
                    if(straightPathPossible(start, end)) {
                        makeStraightPath(start, end);
                        combine(a,b);
                        return;
                    } else {
                        if(LpathPossible(start, end)) {
                            makeLpath(start, end);
                            combine(a,b);
                            return;
                        }
                    }
                }
            }
        }



    public int roomCount() {
        return roomMap.size();
    }
    public boolean allRoomsConnected() {
        int v = quickFind[0];
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
        return area / 500;
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

        public LinkedList<Position> entryPoints() {

            int xRange = Math.min(left, right);
            int xOffset = xRange > 0 ? random.nextInt(xRange * 2 + 1) - xRange : 0;
            Position upperEntrance = node.shift(xOffset, height + 1);
            Position lowerEntrance = node.shift(xOffset, -(depth + 1));

            int yRange = Math.min(height, depth);
            int yOffset = yRange > 0 ? random.nextInt(yRange * 2 + 1) - yRange : 0;
            Position rightEntrance = node.shift(right + 1, yOffset);
            Position leftEntrance = node.shift(-(left + 1), yOffset);

            LinkedList<Position> L = new LinkedList<>();
            if(node.y() + height + 3 < HEIGHT) {
                L.add(upperEntrance);
            }
            if(node.y() - depth - 3 > 0) {
                L.add(lowerEntrance);
            }
            if(node.x() + right + 3 > WIDTH) {
                L.add(rightEntrance);
            }
            if(node.x() - left - 3 > 0) {
                L.add(leftEntrance);
            }
            if(L.isEmpty()) return null;

            for(int i = 0; i < 2 * L.size(); i++) {
                int id = random.nextInt(L.size());
                Position temp = L.get(id);
                L.remove(id);
                L.addLast(temp);
            }
            return L;
        }
    }

    public String wallDirection(Position p, Room room) throws Exception {
        Position c = room.node;
        int dx = p.x() - c.x();
        int dy = p.y() - c.y();
        if (Math.abs(dx) == room.right + 1 && dx > 0) {
            return "right";
        }
        if (Math.abs(dx) == room.left + 1 && dx < 0) {
            return "left";
        }
        if (Math.abs(dy) == room.height + 1 && dy > 0) {
            return "up";
        }
        if (Math.abs(dy) == room.depth + 1 && dy < 0) {
            return "down";
        }
        else throw new Exception("if-else error on method: wallDirection");
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
        Position up = b.y() > a.y() ? b : a;
        Position down = b.y() > a.y() ? a : b;
        for(int x = down.x() - 1; x <= down.x() + 1; x++) {
            for(int y = down.y() + 1; y < up.y(); y++) {
                if(tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeVerticalPath(Position a, Position b) {
        Position up = b.y() > a.y() ? b : a;
        Position down = b.y() > a.y() ? a : b;
        TETile wall = TETile.colorVariant(Tileset.WALL, 50, 50, 50, random);
        for(int y = down.y() + 1; y < up.y(); y++) {
            tiles[a.x() - 1][y] = wall;
            tiles[a.x()][y] = Tileset.FLOOR;
            tiles[a.x() + 1][y] = wall;
        }
    }

    private boolean checkHorizontalPath(Position a, Position b) {
        Position right = b.x() > a.x() ? b : a;
        Position left = b.x() > a.x() ? a : b;
        for(int y = left.y() - 1; y <= left.y() + 1; y++) {
            for(int x = left.x() + 1; x < right.x(); x++) {
                if(tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeHorizontalPath(Position a, Position b) {
        Position right = b.x() > a.x() ? b : a;
        Position left = b.x() > a.x() ? a : b;
        TETile wall = TETile.colorVariant(Tileset.WALL, 50, 50, 50, random);
        for(int x = left.x() + 1; x < right.x(); x++) {
            tiles[x][a.y() + 1] = wall;
            tiles[x][a.y()] = Tileset.FLOOR;
            tiles[x][a.y() - 1] = wall;
        }
    }

    public void makeStraightPath(Position a, Position b) {
        if(checkHorizontalPath(a, b)) {
            makeHorizontalPath(a, b);
        } else {
            if(checkVerticalPath(a, b)) {
                makeVerticalPath(a, b);
            }
        }
    }

    public boolean LpathPossible(Position a, Position b) {
        Position guess1 = new Position(a.x(), b.y());
        Position guess2 = new Position(b.x(), a.y());

        if (straightPathPossible(a, guess1) && straightPathPossible(guess1, b)) {
            return true;
        }
        return straightPathPossible(a, guess2) && straightPathPossible(guess2, b);
    }

    public void makeLpath(Position a, Position b) {
        Position guess1 = new Position(a.x(), b.y());
        Position guess2 = new Position(b.x(), a.y());
        Position corner;
        if (straightPathPossible(a, guess1) && straightPathPossible(guess1, b)) {
            corner = guess1;
        } else {
            if (straightPathPossible(a, guess2) && straightPathPossible(guess2, b)) {
                corner = guess2;
            }
            else return;
        }
        makeStraightPath(a, corner);
        makeStraightPath(b, corner);
    }
}
