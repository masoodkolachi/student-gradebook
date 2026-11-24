package gradebook;

import java.util.List;

public class Percentile {

    private Percentile() {
    }

    private static final double FULL_PERCENT = 100.0;

    /**
     * Returns where a student stands on a roster, from 0.0 (lowest average)
     * to 100.0 (highest average). A roster with one student gives 100.0.
     *
     * @param student the student to look up
     * @param roster  the roster the student is on
     * @return the student's percentile, between 0.0 and 100.0
     * @throws IllegalArgumentException if the student is not on the roster
     */
    public static double percentile(gradebook student, Roster roster) {
        List<gradebook> ranked = Ranking.rankStudents(roster);
        int index = ranked.indexOf(student);
        if (index < 0) {
            throw new IllegalArgumentException("Student is not on the roster");
        }
        int total = ranked.size();
        if (total == 1) {
            return FULL_PERCENT;
        }
        return (double) (total - 1 - index) / (total - 1) * FULL_PERCENT;
    }
}
