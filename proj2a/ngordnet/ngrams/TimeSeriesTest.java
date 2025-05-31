package ngordnet.ngrams;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/** Unit Tests for the TimeSeries class.
 *  @author Josh Hug
 */
public class TimeSeriesTest {
    @Test
    public void testFromSpec() {
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 100.0);
        catPopulation.put(1992, 1000.0);
        catPopulation.put(1994, 2000.0);

        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
        dogPopulation.put(1995, 500.0);
        dogPopulation.put(1991, 500.0);
        dogPopulation.put(1992, 500.0);


        TimeSeries totalPopulation = catPopulation.dividedBy(dogPopulation);
        // expected: 1991: 0.2,
        //           1992: 2.0
        //           1994: 5.0
        //           1995: N/A

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(1991, 1992, 1994));

        assertThat(totalPopulation.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>
                (Arrays.asList(0.2, 2.0, 5.0));

        for (int i = 0; i < expectedTotal.size(); i += 1) {
            assertThat(totalPopulation.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    //TODO: make a unit test for the dividedby method
} 