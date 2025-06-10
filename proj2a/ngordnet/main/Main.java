package ngordnet.main;

import ngordnet.browser.NgordnetServer;
import ngordnet.ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();
        String wordFile = "./data/ngrams/top_49887_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        
        hns.startUp();

        HistoryTextHandler text = new HistoryTextHandler(ngm);
        HistoryHandler history = new HistoryHandler(ngm);
        HyponymHandler hyponym = new HyponymHandler(wn, ngm);

        hns.register("history", history);
        hns.register("historytext", text);
        hns.register("hyponyms", hyponym);
    }
}
