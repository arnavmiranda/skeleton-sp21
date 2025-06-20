package byow.Core;

import java.awt.*;
import java.io.*;
import java.util.*;
import byow.TileEngine.*;
import edu.princeton.cs.introcs.StdDraw;

public class Engine {
    private static TERenderer ter = new TERenderer();
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;
    private static int seed;
    private static Random random;
    private static TETile[][] tiles;
    private static final int SAFETY_FACTOR = 3;
    private static final int HEIGHT_FACTOR = HEIGHT / 5;
    private static final int WIDTH_FACTOR = WIDTH / 10;
    private static TETile FLOOR_TILE;
    private static TETile WALL_TILE;
    private static TETile TRAVELLED_PATH_TILE;
    private static int[] quickFind;
    private static TreeMap<Integer, Room> roomMap = new TreeMap<>();
    private static int counter;
    private static Player player;
    private static final File saveFile = new File("save.txt");
    private String instructions = "";
    private static String tracePath = "";


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        startScreen();
    }

    private void startScreen() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(50 * 16, 50 * 16);
        StdDraw.setScale(0, 50);
        StdDraw.clear(Color.white);
        StdDraw.setFont(new Font("idk", Font.BOLD, 100));
        StdDraw.text(25, HEIGHT - 10, "THE GAME");
        StdDraw.show();
        StdDraw.pause(1000);

        StdDraw.setFont(new Font("idk", Font.CENTER_BASELINE, 30));
        StdDraw.text(25, 20, "(N)ew game");
        StdDraw.text(25, 15, "(L)oad game");
        StdDraw.text(25, 10, "(Q)uit game");
        StdDraw.show();
        char letter;
        while(!StdDraw.hasNextKeyTyped()) {
        }
        letter = StdDraw.nextKeyTyped();
        StdDraw.clear(Color.white);
        switch(letter) {
            case 'N':
            case 'n':
                StdDraw.clear();
                StdDraw.text(25, 35, "enter seed:");
                StdDraw.text(25, 30, "(only positive numbers)");
                StdDraw.text(25, 25, "(type X to enter)");
                StdDraw.show();
                String display = "";
                StringBuilder sb = new StringBuilder();
                sb.append('n');
                outer:
                while(true) {
                    while (StdDraw.hasNextKeyTyped()) {
                        StdDraw.clear();
                        letter = StdDraw.nextKeyTyped();
                        if (letter == 'X' || letter == 'x') break outer;
                        if (letter >= '0' && letter <= '9') {
                            display += letter;
                            StdDraw.text(25,25,display);
                            StdDraw.show();
                            sb.append(letter);
                        }
                    }
                }
                StdDraw.clear();
                sb.append("s");
                String seed = sb.toString();
                playGame(seed);
                break;

            case 'L' :
            case 'l' :
                String path = loadGame();
                if(path.isBlank()) {
                    break;
                }
                StdDraw.clear();
                playGame(path);
                break;
            case 'q' :
            case 'Q' :
                break;
        }
    }

    private void playGame(String path) {
        ter.initialize(WIDTH, HEIGHT + 3, 0, 0);
        tiles = interactWithInputString(path);
        tracePath = path;
        while(true) {
            if(StdDraw.hasNextKeyTyped()) {
                char chr = Character.toLowerCase(StdDraw.nextKeyTyped());
                if(chr == 'q') {
                    quitGame();
                    startScreen();
                }
                player.move(chr);
                if("wasd".contains(Character.toString(chr))) {
                    tracePath += chr;
                }
            }
            ter.renderFrame(tiles);
            drawUX();
            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    private void drawUX() {
        StdDraw.setPenColor(Color.white);
        int width = tiles.length;
        int height = tiles[0].length;
        StdDraw.line(0, HEIGHT + 1, WIDTH, HEIGHT + 1);
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x > width - 1) return;
        if (y > height - 1) return;
        String text = tiles[x][y].description();
        StdDraw.textLeft(2, HEIGHT + 2, text);
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
        processInput(input);
        if(!input.toLowerCase().startsWith("l")) {
            tracePath = "";
            tiles = new TETile[WIDTH][HEIGHT];
            fillWorldWithNothing(tiles);
            random = new Random(seed);
            FLOOR_TILE = TETile.colorVariant(Tileset.FLOOR, 100, 100, 100, random);
            WALL_TILE = TETile.colorVariant(Tileset.WALL, 100, 100, 100, random);
            TRAVELLED_PATH_TILE = TETile.colorVariant(Tileset.FLOOR, 100, 100, 100, random);
            counter = 0;
            drawRooms();
            initializeDS();
            counter = 0;
            while (!allRoomsConnected()) {
                connectRooms();
                if (counter > 10000) break;
            }
            initializePlayer();
        }
        processInstructions(instructions);
        return tiles;
    }

    private void processInput(String inp) {
        String input = inp.toLowerCase();

        if(input.startsWith("n")) {
            seed = extractSeed(input);
        }
        if(input.startsWith("l")) {
            String line = loadGame();
            input = line + input.substring(1);
            processInput(input);
        }
        instructions = extractInstructions(input);
        tracePath += instructions;
    }

    private String extractInstructions(String input) {
        StringBuilder sb = new StringBuilder();

        if(input.startsWith("n")) {
            int index = input.indexOf("s");
            for (int i = index + 1; i < input.length(); i++) {
                sb.append(input.charAt(i));
            }
        }
        if(input.startsWith("l")) {
            for(int i = 1; i < input.length(); i++) {
                sb.append(input.charAt(i));
            }
        }
        return sb.toString();
    }

    private void initializePlayer() {
        int x;
        int y;
        Position p;
        while(true) {
            x = random.nextInt(WIDTH - 1);
            y = random.nextInt(HEIGHT - 1);
            if(tiles[x][y].character() == Tileset.FLOOR.character()) {
                tiles[x][y] = Tileset.AVATAR;
                p = new Position(x, y);
                player = new Player(p);
                break;
            }
        }
    }

    private int determineNumberOfRooms(int height, int width) {
        int area = height * width;
        return area / 400;
    }

    private void initializeDS() {
        quickFind = new int[roomMap.size()];
        for(int i = 0; i < quickFind.length; i++) {
            quickFind[i] = i;
        }
    }

    private void drawRooms() {
        int c = 0;
        int num = determineNumberOfRooms(HEIGHT, WIDTH);
        while (num > 0) {
            if(counter > 10000) {
                break;
            }
            //default constructor generates random position WITHIN the grid (not boundary)
            Position p = new Position();
            Room room = new Room(p);
            room.height = 2 + random.nextInt(HEIGHT_FACTOR);
            room.depth = 2 + random.nextInt(HEIGHT_FACTOR);
            room.left = 2 + random.nextInt(WIDTH_FACTOR);
            room.right = 2 + random.nextInt(WIDTH_FACTOR);

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

    private void combine(int a, int b) {
        int root = Math.min(quickFind[a], quickFind[b]);
        for(int i = 0; i < quickFind.length; i++) {
            if(quickFind[i] == quickFind[a] || quickFind[i] == quickFind[b]) {
                quickFind[i] = root;
            }
        }
    }

    private void connectRooms() {
        int a = random.nextInt(roomCount());
        int b = random.nextInt(roomCount());
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
        if(dx == 0 && dy == 1) {
            joinAdjacent(a, b);
            return;
        }
        if(dy == 0 && dx == 1) {
            joinAdjacent(a, b);
            return;
        }
        if(dx == 0) {
            makeVerticalPath(a, b);
            return;
        }
        if(dy == 0) {
            makeHorizontalPath(a, b);
        }
    }
    private void joinAdjacent(Position a, Position b) {
        tiles[a.x()][a.y()] = FLOOR_TILE;
        tiles[b.x()][a.y()] = FLOOR_TILE;
    }

    private void makeHorizontalPath(Position a, Position b) {
        Position right = b.x() > a.x() ? b : a;
        Position left = b.x() > a.x() ? a : b;

        //
        // TETile hall = TETile.colorVariant(Tileset.FLOOR, 100, 100, 100, random);
        TETile hall = FLOOR_TILE;
        tiles[left.x()][left.y()] = hall;
        tiles[right.x()][right.y()] = hall;

        TETile wall = WALL_TILE;
        //wall = TETile.colorVariant(wall, HALLWAY_COLOR, HALLWAY_COLOR, HALLWAY_COLOR, random);
        for(int x = left.x() + 1; x < right.x(); x++) {
            tiles[x][a.y() + 1] = wall;
            tiles[x][a.y()] = hall;
            tiles[x][a.y() - 1] = wall;
        }
    }

    private void makeVerticalPath(Position a, Position b) {
        Position up = b.y() > a.y() ? b : a;
        Position down = b.y() > a.y() ? a : b;

        //TETile hall = TETile.colorVariant(Tileset.FLOOR, 100, 100, 100, random);
        TETile hall = FLOOR_TILE;
        tiles[down.x()][down.y()] = hall;
        tiles[up.x()][up.y()] = hall;

        TETile wall = WALL_TILE;
        //wall = TETile.colorVariant(wall, HALLWAY_COLOR, HALLWAY_COLOR, HALLWAY_COLOR, random);

        for(int y = down.y() + 1; y < up.y(); y++) {
            tiles[a.x() - 1][y] = wall;
            tiles[a.x()][y] = hall;
            tiles[a.x() + 1][y] = wall;
        }
    }

    private int roomCount() {
        return roomMap.size();
    }

    private boolean allRoomsConnected() {
        int v = quickFind[random.nextInt(quickFind.length)];
        for(int i : quickFind) {
            if(i != v) {
                return false;
            }
        }
        return true;
    }

    private int extractSeed(String string) {
        int index = string.indexOf('s');
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < index; i++) {
            sb.append(string.charAt(i));
        }
        String val = sb.toString();
        return Integer.parseInt(val);
    }

    private void processInstructions(String instructions) {
        if (instructions.isBlank()) {
            return;
        }
        int qi = instructions.indexOf("q");
        for (int i = 0; i < instructions.length(); i++) {
            if(i == qi) {
                quitGame();
                break;
            }
            char letter = instructions.charAt(i);
            player.move(letter);
        }
    }

    private void quitGame() {
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(saveFile, false));
            bf.write('n');
            bf.write(Integer.toString(seed));
            bf.write('s');
            for(int i = 0; i < tracePath.length(); i++) {
                bf.write(tracePath.charAt(i));
            }
            tracePath = "";
            bf.flush();
            bf.close();
        } catch (IOException e) {
            throw new RuntimeException("WRITEioexception");
        }
    }

    private String loadGame() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(saveFile));
            String line = br.readLine();
            if(!line.isBlank()) {
                br.close();
                return line;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("READfilenotfound");
        } catch (IOException e) {
            throw new RuntimeException("READIOexception");
        }
        return "";
    }

    private void fillWorldWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void fitRoom(Room room, TETile[][] tiles) {
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

    private boolean roomOverlap(Room room, TETile[][] tiles) {
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

    private static class Position {
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

    private static class Player {
        public Position pos;

        public Player(Position p) {
            pos = p;
        }

        public void move(char c) {
            int x = pos.x();
            int y = pos.y();

            switch(c) {
                case 'W' :
                case 'w' :
                    if(tiles[x][y + 1].character() == Tileset.FLOOR.character()) {
                        pos = new Position(x, y + 1);
                        tiles[x][y] = TRAVELLED_PATH_TILE;
                        tiles[x][y + 1] = Tileset.AVATAR;
                    }
                    break;
                case 'A' :
                case 'a' :
                    if(tiles[x - 1][y].character() == Tileset.FLOOR.character()) {
                        pos = new Position(x - 1, y);
                        tiles[x][y] = TRAVELLED_PATH_TILE;
                        tiles[x - 1][y] = Tileset.AVATAR;
                    }
                    break;
                case 'S' :
                case 's' :
                    if(tiles[x][y - 1].character() == Tileset.FLOOR.character()) {
                        pos = new Position(x, y - 1);
                        tiles[x][y] = TRAVELLED_PATH_TILE;
                        tiles[x][y - 1] = Tileset.AVATAR;
                    }
                    break;
                case 'D' :
                case 'd' :
                    if(tiles[x + 1][y].character() == Tileset.FLOOR.character()) {
                        pos = new Position(x + 1, y);
                        tiles[x][y] = TRAVELLED_PATH_TILE;
                        tiles[x + 1][y] = Tileset.AVATAR;
                    }
                    break;
                default: break;
            }

        }
    }

    private static class Room {
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

            TETile tile = FLOOR_TILE;
            for (int x = xstart; x <= xend; x++) {
                for (int y = ystart; y <= yend; y++) {
                    tiles[x][y] = tile;
                    //tiles[x][y] = TETile.colorVariant(tile, 1000, 1000, 1000, random);
                }
            }
        }

        public void putWall(TETile[][] tiles) {

            int xstart = node.x() - left - 1;
            int ystart = node.y() - depth - 1;
            int xend = node.x() + right + 1;
            int yend = node.y() + height + 1;

            //int color = HALLWAY_COLOR * 2;
            //TETile tile = TETile.colorVariant(Tileset.WALL, color, color, color, random);
            TETile tile = WALL_TILE;
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

    private boolean straightPathPossible(Position a, Position b) {
        int dx = b.x() - a.x();
        int dy = b.y() - a.y();
        if (dx == 0 && dy == 1) {
            return true;
        }
        if (dy == 0 && dx == 1) {
            return true;
        }
        if (dx == 0) {
            return checkVerticalPath(a, b);
        }
        if (dy == 0) {
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

    private Position LpathPossible(Position a, Position b) {
        Position corner1 = new Position(a.x(), b.y());
        Position corner2 = new Position(b.x(), a.y());

        if(tiles[corner1.x()][corner1.y()].character() == Tileset.NOTHING.character()) {
            if (straightPathPossible(a, corner1) && straightPathPossible(corner1, b)) {
                return corner1;
            }
        }
        if(tiles[corner2.x()][corner2.y()].character() == Tileset.NOTHING.character()) {
            if (straightPathPossible(a, corner2) && straightPathPossible(corner2, b)) {
                return corner2;
            }
        }
        return null;
    }

    private void changeCornerTile(int x, int y, TETile wall) {

        if(tiles[x][y].character() == Tileset.NOTHING.character()) {
            tiles[x][y] = wall;
        }
        if(tiles[x][y].character() == Tileset.FLOOR.character()) {
            tiles[x][y] = wall;
        }
    }

    private void makeLpath(Position a, Position b, Position corner) {
        TETile wall = WALL_TILE;
        //wall = TETile.colorVariant(wall, HALLWAY_COLOR, HALLWAY_COLOR, HALLWAY_COLOR, random);
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