package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class horribleTest {

    @Test
    public void test1() {
        int i = 0;
        for (int j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                System.out.println(i + " " + j);
            }
        }
        System.out.println("i is " + i);
    }
}