package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class
TestBuggyAList {

    @Test
    public void testThreeAddRemove() {
        AListNoResizing works = new AListNoResizing();
        BuggyAList broken = new BuggyAList();

        int x;
        x = 4;
        works.addLast(x);
        broken.addLast(x);
        x = 5;
        works.addLast(x);
        broken.addLast(x);
        x = 6;
        works.addLast(x);
        broken.addLast(x);

        assertEquals("both should return 6",works.removeLast(), broken.removeLast());
        assertEquals("both should return 5",works.removeLast(), broken.removeLast());
        assertEquals("both should return 4",works.removeLast(), broken.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> BL = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                BL.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
            } else if (operationNumber == 2) {
                //getLast
                if(L.size() == 0 || (BL.size() == 0)) continue;
                int lv = L.getLast();
                int blv = BL.getLast();
                assertEquals("both returned values must be same", lv, blv);
            } else if (operationNumber == 3) {
                //removeLast
                if(L.size() == 0 || (BL.size() ==0)) continue;
                int l = L.removeLast();
                int bl = BL.removeLast();
                assertEquals("both values must be equal", l, bl);
            }
        }
    }
}
