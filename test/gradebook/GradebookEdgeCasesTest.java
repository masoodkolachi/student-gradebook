package gradebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lab 11 - tests for error paths in gradebook that no earlier test reached:
 * a duplicate roll number, a student with no scores, and a null name.
 */
class GradebookEdgeCasesTest {

    @Test
    void duplicateRollNumber_isRejected() {
        new gradebook("First", "EDGE-1");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new gradebook("Second", "EDGE-1"));
        assertTrue(thrown.getMessage().contains("EDGE-1"));
    }

    @Test
    void average_withNoScores_isZero() {
        gradebook student = new gradebook("Empty", "EDGE-2");
        assertEquals(0.0, student.average(), 0.0001);
    }

    @Test
    void validateName_null_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(null));
    }
}
