package gradebook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ranking {

    public static List<gradebook> rankStudents(Roster roster) {
        List<gradebook> ranked = new ArrayList<>(roster.getStudents());
        ranked.sort(Comparator.comparingDouble(gradebook::average).reversed());
        return ranked;
    }
}