package gradebook;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 5, Task 2 — EP tests for gradebook.letterGrade(score).
 * Java/JUnit5 translation of the worked example's test_letter_grade.py.
 *
 * Classes covered: Invalid-low (<0), F [0-59], D [60-69], C [70-79],
 * B [80-89], A [90-100], Invalid-high (>100).
 */
class LetterGradeTest {

    @ParameterizedTest
    @CsvSource({
            "45, F",
            "65, D",
            "75, C",
            "85, B",
            "95, A"
    })
    void testLetterGradeValidClasses(double score, String expected) {
        assertEquals(expected, gradebook.letterGrade(score));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10, 150})
    void testLetterGradeInvalidClasses(double score) {
        assertThrows(IllegalArgumentException.class, () -> gradebook.letterGrade(score));
    }
}
