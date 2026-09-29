package gradebook;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Holds a class roster of finalized students. A student may only be
 * finalised onto the roster if they have between 1 and 6 scores recorded
 * — this enforces the business rule from Lab 5, Task 1/3.
 */
public class Roster {

    /**
     * How the roster opens a file for writing. The default opens a real
     * file; tests pass in a fake one so nothing touches the disk. This is
     * the Java stand-in for mocking Python's built-in open().
     */
    @FunctionalInterface
    public interface WriterFactory {
        Writer open(String path) throws IOException;
    }

    private final List<gradebook> students = new ArrayList<>();
    private final WriterFactory writerFactory;

    public Roster() {
        this(path -> new FileWriter(path, StandardCharsets.UTF_8));
    }

    public Roster(WriterFactory writerFactory) {
        this.writerFactory = writerFactory;
    }

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

    /**
     * Average of all students' averages (each student counts once, no
     * matter how many scores they have).
     *
     * @return the class average, or 0.0 if the roster is empty
     */
    public double classAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (gradebook s : students) {
            sum += s.average();
        }
        return sum / students.size();
    }

    /**
     * Writes one line per student to the given file, in the form
     * {@code rollNo,name,scoreCount,average} with the average to two
     * decimal places.
     *
     * @param path where to write the file
     * @throws GradeBookIOException if the file cannot be opened or written
     */
    public void saveToFile(String path) {
        try (Writer writer = writerFactory.open(path)) {
            for (gradebook s : students) {
                writer.write(s.getRollNo() + "," + s.getName() + ","
                        + s.getScoreCount() + ","
                        + String.format(Locale.ROOT, "%.2f", s.average()) + "\n");
            }
        } catch (IOException e) {
            throw new GradeBookIOException("Could not save roster to " + path, e);
        }
    }
}
