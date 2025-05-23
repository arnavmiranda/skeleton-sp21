package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {

    static double calculateFrequency(int i) {
        double term = (i - 24) / 12;
        term = Math.pow(2.0, term);
        term = term * 440;
        return term;
    }

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        GuitarString[] array = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            double freq = calculateFrequency(i);
            array[i] = new GuitarString(freq);
        }


        while (true) {
            double sample = 0;
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index != -1) {
                    GuitarString string = array[index];
                    string.pluck();
                }
            }
            for(int i = 0; i < 37; i++) {
                sample += array[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++) {
                    array[i].tic();
                }
            }

        }
    }


