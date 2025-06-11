package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final long SEED = 9;
    private static final Random RANDOM = new Random(SEED);

    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position shift(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }
    }
    private static void fillRow(TETile[][] tiles, Position p, TETile tile, int length) {
        for(int x = 0; x < length; x++) {
            tiles[p.x + x][p.y] = tile;
        }
    }


    private static void addHexagonHelper(TETile[][] tiles, TETile tile, Position pos, int blank, int num) {
        //draw this row
        Position start = pos.shift(blank, 0);
        fillRow(tiles, start, tile, num);

        //draw remaining rows recursively
        if(blank > 0) {
            Position nextRow = pos.shift(0, -1);
            addHexagonHelper(tiles, tile, nextRow, blank - 1, num + 2);
        }

        //draw this row again after shifting
        Position startReflection = pos.shift(blank, -(2 * blank + 1));
        fillRow(tiles, startReflection, tile, num);
    }

    private static void addHexagon(TETile[][] tiles, Position startPos, TETile tile, int size) {
        if(size <= 2) {
            return;
        }
        addHexagonHelper(tiles, tile, startPos, size - 1, size);
    }

    public static void fillWorldWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
    private static TETile randomBiome() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.WATER;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.SAND;
            case 5: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    private static Position getBottomNeighbor(Position p, int size) {
        return p.shift(0, - (2 * size));
    }

    private static void addHexColumn(TETile[][] tiles, Position startPos, int size, int num) {
        if(size < 2) {
            return;
        }
        if(num > 0) {
            TETile tile = TETile.colorVariant(randomBiome(), 50, 50, 50, RANDOM);
            addHexagon(tiles, startPos, tile, size);
            Position bottomPos = getBottomNeighbor(startPos, size);
            addHexColumn(tiles, bottomPos, size, num - 1);
        }
    }

    private static Position getTopRightNeighbor(Position p, int n) {
        return p.shift(2 * n - 1, n);
    }

    private static Position getBottomRightNeighbor(Position p, int n) {
        return p.shift(2 * n - 1, -n);
    }

    private static void drawWorld(TETile[][] tiles, int hexSize, int tesSize, Position p) {

        addHexColumn(tiles, p, hexSize, tesSize);
        for(int i = 1; i < tesSize; i++) {
            p = getTopRightNeighbor(p, hexSize);
            addHexColumn(tiles, p, hexSize, tesSize + i);
        }
        for(int i = tesSize - 2; i >= 0; i--) {
            p = getBottomRightNeighbor(p, hexSize);
            addHexColumn(tiles, p, hexSize, tesSize + i);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        fillWorldWithNothing(tiles);

        int hexSize = 3;
        int tesselation = 3;
        drawWorld(tiles, hexSize, tesselation, new Position(15,40));
        ter.renderFrame(tiles);
    }
}


