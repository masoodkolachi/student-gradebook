package gradebook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ranking {

    private static final Comparator<gradebook> BY_AVERAGE_DESC_THEN_NAME =
            Comparator.comparingDouble(gradebook::average).reversed()
                    .thenComparing(gradebook::getName);

    public static List<gradebook> rankStudents(Roster roster) {
        List<gradebook> ranked = new ArrayList<>(roster.getStudents());
        ranked.sort(BY_AVERAGE_DESC_THEN_NAME);
        return ranked;
    }
}