package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;
import java.util.*;

public class HyponymHandler extends NgordnetQueryHandler {
    private WordNet wn;

    public HyponymHandler(WordNet wn) {
        this.wn = wn;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();

        String word = words.getFirst();
        LinkedList<String> llist = wn.stringHyponyms(word);
        int startYear = q.startYear();
        int endYear = q.endYear();

        return llist.toString();
    }
}
