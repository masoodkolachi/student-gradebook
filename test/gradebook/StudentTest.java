import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lab 11 - tests for the Student demo class. Student has no package
 * statement, so this test class has none either.
 */
class StudentTest {

    private static final double DELTA = 0.0001;

    @Test
    void studentAverage_returnsMeanOfMarks() {
        Student student = new Student("Ali", new int[] {80, 90, 100});
        assertEquals(90.0, student.studentAverage(), DELTA);
    }

    @Test
    void studentAverage_singleMark_returnsThatMark() {
        Student student = new Student("Sara", new int[] {73});
        assertEquals(73.0, student.studentAverage(), DELTA);
    }

    @Test
    void main_printsEachStudentAndTheOverallAverage() {
        PrintStream original = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            Student.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String output = captured.toString();
        assertTrue(output.contains("zunair Average: 83.6"));
        assertTrue(output.contains("Mariam Average: 84.0"));
        assertTrue(output.contains("Overall Average:"));
    }
}
