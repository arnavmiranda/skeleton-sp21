package gh2;

import deque.*;
// TODO: maybe more imports

public class Drum {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = 1.0; // energy decay factor
    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public Drum(double frequency) {

        buffer = new LinkedListDeque<Double>();
        double dCapacity = SR / frequency;
        int capacity = (int) Math.round(dCapacity);

        for(int i = 0; i< capacity * 2; i++) {
            buffer.addLast(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int capacity = buffer.size();
        while (!buffer.isEmpty()) {
            buffer.removeFirst();
        }
        for (int i = 0; i < capacity * 2; i++) {
            double r = Math.random() - 0.5;
            buffer.addFirst(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double first = buffer.removeFirst();
        double second = buffer.get(0);
        double average = (first + second) / 2;
        double newDouble = average * DECAY;
        double rand = Math.random();
        if(rand > 0.5) newDouble*= -1;
        buffer.addLast(newDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
