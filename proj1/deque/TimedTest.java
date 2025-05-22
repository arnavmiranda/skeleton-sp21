package deque;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.ArrayList;

public class TimedTest {
    private static void printTimingTable(ArrayList<Integer> Ns, ArrayList<Double> times, ArrayList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static final int M = 10000;

    public static double calculateAddTime(int N, Deque<Integer> items){
        Stopwatch sw = new Stopwatch();
        for(int i = 0; i<N; i++) {
            items.addLast(0);
        }
        return sw.elapsedTime();
    }

    static ArrayList<Double> times;
    static ArrayList<Integer> opcounts;
    static ArrayList<Integer> Ns;

    public static void startDrawing(Deque<Integer> d) {
        for (int i = 1000; i <= 128000000; i *= 2) {
            double t = calculateAddTime(i, d);
            times.addLast(t);
            Ns.addLast(i);
            opcounts.addLast(M);
        }
        printTimingTable(Ns, times, Ns);
    }
    public static void resetTable() {
        times = new ArrayList<>();
        opcounts = new ArrayList<>();
        Ns = new ArrayList<>();
    }
    public static void main(String[] args) {

        resetTable();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        System.out.println("TIME TABLE FOR LINKED LIST DEQUE:");
        startDrawing(lld);
        resetTable();
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        System.out.println("TIME TABLE FOR ARRAY DEQUE:");
        startDrawing(ad);

    }
}
