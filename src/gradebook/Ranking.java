package gradebook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ranking {

    private static final Comparator<gradebook> BY_AVERAGE_DESC_THEN_NAME =
            Comparator.comparingDouble(gradebook::average).reversed()
                    .thenComparing(gradebook::getName, String.CASE_INSENSITIVE_ORDER);

    /**
     * Ranks the students on a roster from best to worst.
     * <p>
     * Students are ordered by average score, highest first. Students with
     * the same average are ordered alphabetically by name, ignoring letter
     * case. The roster itself is not changed.
     *
     * @param roster the roster to rank
     * @return a new list of the roster's students in rank order
     */
    public static List<gradebook> rankStudents(Roster roster) {
        List<gradebook> ranked = new ArrayList<>(roster.getStudents());
        ranked.sort(BY_AVERAGE_DESC_THEN_NAME);
        return ranked;
    }
}