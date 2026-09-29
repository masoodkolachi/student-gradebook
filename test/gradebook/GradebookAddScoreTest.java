package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Lab 7, Task 4 - one consolidated, parametrized test for
 * gradebook.addScore().
 *
 * These 8 cases replace what would otherwise be 8 near-identical test
 * methods (they started life as manual test cases TC-003, TC-004, TC-005
 * and TC-011 in docs/test-cases.md, plus a few more edge values). Each case
 * carries a description string as its first argument; JUnit prints it as
 * the test name, which is the equivalent of pytest's ids=.
 */
class GradebookAddScoreTest {

    private static final double DELTA = 0.0001;
    private static final AtomicInteger NEXT_ROLL = new AtomicInteger(1);

    private gradebook student;

    // Function-scoped fixture: a fresh student with a unique roll number
    // for every case.
    @BeforeEach
    void setUp() {
        student = new gradebook("Test Student", "AS-" + NEXT_ROLL.getAndIncrement());
    }

    // description, score, accepted?
    static Stream<Arguments> addScoreCases() {
        return Stream.of(
                arguments("zero is accepted (lower boundary)", 0.0, true),
                arguments("typical whole-number score is accepted", 85.0, true),
                arguments("typical decimal score is accepted", 85.5, true),
                arguments("maximum score 100 is accepted", 100.0, true),
                arguments("tiny positive score is accepted", 0.01, true),
                arguments("just below zero is rejected", -0.01, false),
                arguments("small negative score is rejected", -5.0, false),
                arguments("large negative score is rejected", -1000.0, false)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("addScoreCases")
    void addScore_sweep(String description, double score, boolean accepted) {
        if (accepted) {
            // Act
            student.addScore(score);
            // Assert: the score was stored, and shows up in the average
            assertEquals(1, student.getScoreCount());
            assertEquals(score, student.average(), DELTA);
        } else {
            // Act + Assert: rejected, and nothing was stored
            assertThrows(IllegalArgumentException.class, () -> student.addScore(score));
            assertEquals(0, student.getScoreCount());
        }
    }
}
