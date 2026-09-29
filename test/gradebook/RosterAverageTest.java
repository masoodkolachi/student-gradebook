package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Lab 7, Task 2 - tests for Roster.classAverage(), using fixtures and the
 * Arrange-Act-Assert pattern.
 *
 * classAverage() is the average of each student's own average, so a student
 * with 5 scores counts the same as a student with 1 score.
 */
class RosterAverageTest {

    private static final double DELTA = 0.0001;

    // Roll numbers must be unique across the whole test run.
    private static final AtomicInteger NEXT_ROLL = new AtomicInteger(1);

    private Roster emptyRoster;
    private Roster populatedRoster;

    @BeforeEach
    void setUp() {
        emptyRoster = new Roster();

        // Ali averages 85, Sara averages 70.
        populatedRoster = new Roster();
        populatedRoster.addStudent(student("Ali", 80, 90));
        populatedRoster.addStudent(student("Sara", 70));
    }

    private gradebook student(String name, double... scores) {
        gradebook g = new gradebook(name, "RA-" + NEXT_ROLL.getAndIncrement());
        for (double s : scores) {
            g.addScore(s);
        }
        return g;
    }

    @Test
    void testClassAverage_emptyRoster_returnsZero() {
        // Act
        double result = emptyRoster.classAverage();
        // Assert
        assertEquals(0.0, result, DELTA);
    }

    @Test
    void testClassAverage_singleStudent_equalsThatStudentsAverage() {
        // Arrange
        emptyRoster.addStudent(student("Ali", 80, 90));
        // Act
        double result = emptyRoster.classAverage();
        // Assert
        assertEquals(85.0, result, DELTA);
    }

    @Test
    void testClassAverage_multipleStudents_averagesTheAverages() {
        // Act: (85 + 70) / 2
        double result = populatedRoster.classAverage();
        // Assert
        assertEquals(77.5, result, DELTA);
    }

    @Test
    void testClassAverage_isNotTheSameAsAverageOfAllScores() {
        // Arrange: one student with a single 100, one with five 0s.
        // Average of averages = (100 + 0) / 2 = 50.
        // Average of all six scores would be 100 / 6 = 16.67.
        emptyRoster.addStudent(student("Top", 100));
        emptyRoster.addStudent(student("Low", 0, 0, 0, 0, 0));
        // Act
        double result = emptyRoster.classAverage();
        // Assert
        assertEquals(50.0, result, DELTA);
    }
}
