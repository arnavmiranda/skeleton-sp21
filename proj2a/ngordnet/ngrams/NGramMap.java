package ngordnet.ngrams;

import edu.princeton.cs.introcs.In;

import java.util.Collection;
import java.util.LinkedList;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    private static final int hashSize = 100000;
    private Bucket[] hashtable = new Bucket[hashSize];

    private class Word {
        String word;
        TimeSeries ts;

        Word(String name) {
            word = name;
            ts = new TimeSeries();
        }
    }

    private class Bucket {
        LinkedList<Word> bucket = new LinkedList<>();
    }

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     *
     * METHOD:
     * 1) create HASHMAP of buckets, with each bucket storing multiple Words: which contain the data of TimeSeries (each one containing the data of one word)
     * 2) sort through csv file, only processing first 3 data entries per row
     *    - store a temp string variable to keep track of which word you're on, and compare through each iteration to see if word switched
     *    - for every word, make unique string hashcode and then reduce it and store the value through the iterations
     *    - store that particular row of data into the word's hash bucket, iterating to find the item corresponding to our word, which then connects to
     *      the TimeSeries of that word (pair-like data structure with car = string of word and cdr = timeseries ?)
     *    - any word's data at any point can be accessed by calculating its hash index, finding the corresponding bucket, iterating through the list to
     *      search for the correct pair's car value which is the string to be compared (use isEqual() method for string) and then iterate through its
     *      cdr time series value and look for the year.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In words = new In(wordsFilename);
        In counts = new In(countsFilename);

        String first;
        int second;
        double third;
        double fourth;

        String temp = "";
        int tempIndex = -1;

        while(!words.isEmpty()) {
            first = words.readString();
            second = words.readInt();
            third = words.readDouble();
            fourth = words.readDouble();

            if(temp.equals(first)) {
                Bucket thatbucket = hashtable[tempIndex];
                Word word = thatbucket.bucket.getLast();
                word.ts.put(second, third);
            } else {
                temp = first;
                tempIndex = hashIndex(first);
                Word word = new Word(first);
                word.ts.put(second, third);

                Bucket thatbucket = hashtable[tempIndex];
                if(thatbucket == null) {
                    thatbucket = new Bucket();
                }
                thatbucket.bucket.addLast(word);
            }

        }



    }


    private int hashIndex(String word) {
        int hash = word.hashCode();
        hash = Math.floorMod(hash, hashSize);
        return hash;
    }


    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        return null;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        return null;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        return null;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        return null;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        return null;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        return null;
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
