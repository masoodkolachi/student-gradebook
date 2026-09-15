package gradebook;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 6, Task 2 — full Boundary Value Analysis suite for
 * gradebook.letterGrade(score). Covers the domain edges (0, 100) and all
 * four grade cut-offs (60, 70, 80, 90), each with its value-1/value/value+1
 * neighbours, per docs/bva-analysis.md.
 */
class LetterGradeBvaTest {

    // 16 values that should return a grade without throwing.
    @ParameterizedTest
    @CsvSource({
            "0, F",
            "1, F",
            "59, F",
            "60, D",
            "61, D",
            "69, D",
            "70, C",
            "71, C",
            "79, C",
            "80, B",
            "81, B",
            "89, B",
            "90, A",
            "91, A",
            "99, A",
            "100, A"
    })
    void testBoundaryValuesReturnCorrectGrade(double score, String expected) {
        assertEquals(expected, gradebook.letterGrade(score));
    }

    // The two domain-edge values that should be rejected.
    @ParameterizedTest
    @ValueSource(doubles = {-1, 101})
    void testDomainEdgeValuesThrow(double score) {
        assertThrows(IllegalArgumentException.class, () -> gradebook.letterGrade(score));
    }
}
