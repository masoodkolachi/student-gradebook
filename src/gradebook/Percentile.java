package gradebook;

import java.util.List;

public class Percentile {

    public static double percentile(gradebook s, Roster r) {
        List<gradebook> l = Ranking.rankStudents(r);
        int i = l.indexOf(s);
        int n = l.size();
        return (double) (n - 1 - i) / n * 100;
    }
}
