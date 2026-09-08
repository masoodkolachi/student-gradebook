package gradebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a class roster of finalized students. A student may only be
 * finalised onto the roster if they have between 1 and 6 scores recorded
 * — this enforces the business rule from Lab 5, Task 1/3.
 */
public class Roster {

    private final List<gradebook> students = new ArrayList<>();

    /**
     * Adds a student to the roster, enforcing the 1-6 scores-per-student rule.
     *
     * @param student the student to finalise onto the roster
     * @throws IllegalArgumentException if the student has 0 or more than 6 scores
     */
    public void addStudent(gradebook student) {
        int count = student.getScoreCount();
        if (count < 1 || count > 6) {
            throw new IllegalArgumentException(
                    "A student must have between 1 and 6 scores to be added "
                            + "to the roster; had " + count);
        }
        students.add(student);
    }

    public int size() {
        return students.size();
    }
}
