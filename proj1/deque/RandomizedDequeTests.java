package deque;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

public class
RandomizedDequeTests {

    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> buggy = new ArrayDeque<>();

        int N = 100000;
        int choice;
        for (int i = 0; i < N; i++) {
            choice = StdRandom.uniform(0, 6);
            if (choice == 0) {
                //addLast
                int randVal = StdRandom.uniform(0, 10);
                buggy.addLast(randVal);
                System.out.println(i + ") addLast: " + randVal);
                continue;
            }
            if (choice == 1) {
                //addFirst
                int randVal = StdRandom.uniform(0, 10);
                buggy.addFirst(randVal);
                System.out.println(i + ") addFirst: " + randVal);
                continue;
            }
            if (choice == 2) {
                //get
                if(buggy.size() == 0) continue;
                int randVal = StdRandom.uniform(0, buggy.size());
                System.out.println(i + ") get index: " + randVal + ": " + buggy.get(randVal));
                continue;
            }
            if (choice == 3) {
                //removeLast
                if(buggy.size() == 0) continue;
                System.out.println(i + ") removeLast: " + buggy.removeLast());
                continue;
            }
            if (choice == 4) {
                //removeFirst
                if(buggy.size() == 0) continue;
                System.out.println(i + ") removeFirst: " + buggy.removeFirst());
                continue;
            }
            if (choice == 5) {
                //size
                int size = buggy.size();
                System.out.println(i + ") size: " + size);
            } 
        }
    }
}
