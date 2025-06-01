package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;

    HistoryTextHandler() {
        this.map = new NGramMap("./data/ngrams/words_that_start_with_q.csv",
                "./data/ngrams/total_counts.csv");
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        StringBuilder response = new StringBuilder();

        for(String name : words) {
            TimeSeries ts = map.weightHistory(name, startYear, endYear);
            response.append(ts.toString());
            response.append("\n");
        }
        return response.toString();
    }
}
