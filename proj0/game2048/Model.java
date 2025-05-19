package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Arnav Miranda
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */

    /*helper methods: */

    public static boolean tileExists(Tile t){
        if(t==null)
            return false;
        return true;
    }

    public static boolean equalTiles(Tile a, Tile b) {
        if(a.value() == b.value())
            return true;
        return false;
    }

    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        board.setViewingPerspective(side);

        for (int col = 0; col < board.size(); col++) {
            boolean top = false, third = false;

            for (int row = 2; row >= 0; row--) {
                Tile us = board.tile(col, row);
                if (!tileExists(us)) {
                    continue;
                }
                Tile above = board.tile(col, row + 1);
                if (tileExists(above)) {
                    if (equalTiles(above, us)) {
                        if (row == 2 && !top) {
                            board.move(col, row + 1, us);
                            top = true;
                            score += us.value() * 2;
                            changed= true;
                            continue;
                        }
                        if (row == 1 && !third) {
                            board.move(col, row + 1, us);
                            third = true;
                            score += us.value() * 2;
                            changed = true;
                            continue;
                        }
                        if (row == 0) {
                            board.move(col, row + 1, us);
                            score += us.value() * 2;
                            changed = true;
                            continue;
                        }
                    }
                    continue;
                } else if (row == 2) {
                    board.move(col, row + 1, us);
                    changed= true;
                    continue;
                }
                Tile twice_above = board.tile(col, row + 2);

                if (tileExists(twice_above)) {
                    if (equalTiles(twice_above, us)) {
                        if (row == 1 && !top) {
                            board.move(col, row + 2, us);
                            score += us.value() * 2;
                            top = true;
                            changed= true;
                            continue;
                        }
                        if (row == 0 && !third) {
                            board.move(col, row + 2, us);
                            score += us.value() * 2;
                            third = true;
                            changed = true;
                            continue;
                        }
                    }
                    if (row == 1 || row == 0) {
                        board.move(col, row + 1, us);
                        changed = true;
                        continue;
                    }
                }
                else if( row == 1) {
                    board.move(col, 3, us);
                    changed = true;
                    continue;
                }

                Tile thrice_above = board.tile(col, 3);

                if (tileExists(thrice_above)) {

                    if (equalTiles(thrice_above, us)) {
                        if (!top) {
                            board.move(col, 3, us);
                            score += us.value() * 2;
                            changed = true;
                            continue;
                        }
                    }
                    board.move(col, 2, us);
                    changed = true;
                }
                else {
                    board.move(col, 3, us);
                    changed = true;
                }
            }
        }

        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int length = b.size();
        boolean empty = false;

        for(int r = 0; r < length; r++) {
            for(int c = 0; c < length; c++) {
                Tile tile = b.tile(c, r);
                if(tile == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int length = b.size();
        boolean exists = false;
        Tile tile = null;

        for(int c = 0; c< length; c++) {
            for(int r = 0; r< length; r++) {
                tile = b.tile(c, r);

                if(tileExists(tile)){
                    int val = tile.value();
                    if(val == MAX_PIECE) {
                        return true;
                    }

                }


            }
        }
        return false;
    }


    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */

    public static boolean atLeastOneMoveExists(Board b) {
        if(emptySpaceExists(b))
            return true;
        if(adjacentTilesExist(b))
            return true;
        return false;
    }

    /* goes through every tile and runs the method 'checkAdjacent' */
    public static boolean adjacentTilesExist(Board b) {
        int length = b.size();
        Tile temp, test;
        for( int c = 0; c < length; c++) {
            for (int r = 0; r < length; r++) {
                if(checkAdjacent(c,r,b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAdjacent(int c, int r, Board b) {
        int val = b.tile(c, r).value();
        if (c != b.size() - 1 && b.tile(c + 1, r) != null) {
            int up = b.tile(c + 1, r).value();
            if(val == up)
                return true;

        }
        if (c != 0 && b.tile(c - 1, r) != null) {
            int down = b.tile(c - 1, r).value();
            if(val == down)
                return true;
        }
        if (r != b.size() - 1 && b.tile(c, r + 1) != null) {
            int left = b.tile(c, r + 1).value();
            if(val == left)
                return true;
        }
        if (r != 0 && b.tile(c, r - 1) != null) {
            int right = b.tile(c, r - 1).value();
            if(val == right)
                return true;
        }
        return false;
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
