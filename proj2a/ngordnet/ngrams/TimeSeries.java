package ngordnet.ngrams;

import net.sf.saxon.functions.Minimax;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if(startYear < MIN_YEAR) {
            startYear = MIN_YEAR;
        }
        if(endYear > MAX_YEAR) {
            endYear = MAX_YEAR;
        }
        for(int year : ts.keySet()) {
            if(year >= startYear && year <= endYear) {
                double value = ts.get(year);
                super.put(year, value);
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        ArrayList<Integer> years = new ArrayList<>();
        for(int year : this.keySet()) {
            years.addLast(year);
        }
        return years;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> data = new ArrayList<>();
        for(int year : this.keySet()) {
            double value = this.get(year);
            data.addLast(value);
        }
        return data;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    private TimeSeries combineTimeSeries(TimeSeries a, TimeSeries b) {
        TimeSeries combined = new TimeSeries();
        List<Integer> year1 = a.years();
        List<Integer> year2 = b.years();

        int start = Math.min(year1.getFirst(), year2.getFirst());
        int end = Math.max(year1.getLast(), year2.getLast());

        for (int year = start; year <= end; year++) {
            if(!a.containsKey(year) && !b.containsKey(year)) {
                continue;
            }
            if (!a.containsKey(year)) {
                combined.put(year, b.get(year));
                continue;
            }
            if(!b.containsKey(year)) {
                combined.put(year, a.get(year));
                continue;
            }
            double sum = a.get(year) + b.get(year);
            combined.put(year, sum);
        }
        return combined;
    }

    public TimeSeries plus(TimeSeries ts) {
        if(this.isEmpty() && ts.isEmpty()) {
            return new TimeSeries();
        }
        if(this.isEmpty()) {

        }
        if(ts.isEmpty()) {
            List<Integer> years = this.years();
            return new TimeSeries(this, years.getFirst(), years.getLast());
        }
        return combineTimeSeries(this, ts);
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */

    private TimeSeries divideTimeSeries(TimeSeries a, TimeSeries b) {
        TimeSeries divided = new TimeSeries();
        List<Integer> year1 = a.years();
        List<Integer> year2 = b.years();

        int start = Math.min(year1.getFirst(), year2.getFirst());
        int end = Math.max(year1.getLast(), year2.getLast());

        for (int year = start; year <= end; year++) {
            if(!a.containsKey(year) && !b.containsKey(year)) {
                continue;
            }
            if (!a.containsKey(year)) {
                continue;
            }
            if(!b.containsKey(year)) {
                throw new IllegalArgumentException();
            }
            double quotient = a.get(year) / b.get(year);
            divided.put(year, quotient);
        }
        return divided;
    }
    public TimeSeries dividedBy(TimeSeries ts) {
        if(this.isEmpty() || ts.isEmpty()) {
            return new TimeSeries();
        }
        return divideTimeSeries(this, ts);
    }
}
