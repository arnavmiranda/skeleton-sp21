package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class HyponymHandler extends NgordnetQueryHandler {
    private WordNet wn;
    private NGramMap map;

    public class MaxHeapComparator implements Comparator<countWord> {
        @Override
        public int compare(countWord o1, countWord o2) {
            return o2.count - o1.count;
        }
    }


    public HyponymHandler(WordNet wn, NGramMap map) {
        this.wn = wn;
        this.map = map;
    }

    private int totalCount(TimeSeries ts, int start, int end) {
        int sum = 0;
        for(int year = start; year <= end; year++) {
            if(!ts.containsKey(year)) {
                continue;
            }
            sum+= ts.get(year);
        }
        return sum;
    }

    public class countWord {
        String name;
        int count;
        countWord(int num, String word) {
            name = word;
            count = num;
        }
    }
    private List<String> topK(List<String> L, int k, int start,int end) {
        LinkedList<String> finalList = new LinkedList<>();
        PriorityQueue<countWord> maxHeap = new PriorityQueue<>(new MaxHeapComparator());

        int count;
        countWord node;
        for(String word : L) {
            count = totalCount(map.weightHistory(word), start, end);
            node = new countWord(count, word);
            maxHeap.add(node);
        }
        for(int i = 0; i < k; i++) {
            finalList.addLast(maxHeap.poll().name);
        }
        return finalList;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();

        LinkedList<String> llist = wn.stringHyponyms(words);
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();

        if(k == 0) {
            return llist.toString();
        }
        return topK(llist, k, startYear, endYear).toString();
    }
}
