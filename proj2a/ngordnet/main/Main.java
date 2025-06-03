package ngordnet.main;

import ngordnet.browser.NgordnetServer;
import ngordnet.ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        //NGramMap ngm = new NGramMap(wordFile, countFile);

        String synsetFile = "./data/wordnet/synsets16.txt";
        String hyponymFile = "./data/wordnet/hyponyms16.txt";
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        
        hns.startUp();
        //hns.register("history", new HistoryHandler(ngm));
        //hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymHandler(wn));
    }
}
