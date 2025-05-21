package deque;

import org.junit.Test;

import java.sql.Array;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class DequeTest {

    @Test
    public void addIsEmptySizeTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());

        lld1.addFirst("front");
        ad1.addFirst("front");

        assertEquals(1, ad1.size());
        assertEquals(1, lld1.size());

        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());
        assertFalse("ad1 should now contain 1 item", ad1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());
        ad1.addLast("middle");
        assertEquals(2, ad1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());
        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out both deques: ");
		lld1.printDeque();
        ad1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());
        assertTrue("lld1 should be empty upon initialization", ad1.isEmpty());

        lld1.addFirst(10);
        ad1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());


        lld1.removeFirst();
        ad1.removeFirst();

        // should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        lld1.addFirst(3);
        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        ad1.addFirst(3);
        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);


        size = ad1.size();
        errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

        ArrayDeque<String>  ad1 = new ArrayDeque<>();
        ArrayDeque<Double>  ad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<Boolean>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

         s = ad1.removeFirst();
         d = ad2.removeFirst();
         b = ad3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

         passed1 = false;
         passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 100; i++) {
            lld1.addLast(i);
            ad1.addLast(i);
        }

        for (double i = 0; i < 50; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }

        for (double i = 99; i > 50; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);

        }


    }

}
