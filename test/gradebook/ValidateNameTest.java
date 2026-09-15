package gradebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 5, Task 4 — EP tests for gradebook.validateName(name).
 * Classes covered: valid typical name, empty string, over-length string
 * (>50 chars), string containing digits/symbols. A hyphenated-name case
 * is included as a fifth class since hyphens are explicitly allowed.
 */
class ValidateNameTest {

    @Test
    void testValidTypicalName() {
        assertDoesNotThrow(() -> gradebook.validateName("Ali Raza"));
    }

    @Test
    void testEmptyNameRejected() {
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(""));
    }

    @Test
    void testOverLengthNameRejected() {
        String longName = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(longName));
    }

    @Test
    void testNameWithDigitsRejected() {
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName("Ali123"));
    }

    @Test
    void testNameWithHyphenAccepted() {
        assertDoesNotThrow(() -> gradebook.validateName("Anne-Marie"));
    }

    // --- Lab 6, Task 4: boundary tests around the min (non-empty) and ---
    // --- max (50 char) edges of the name-length rule.                 ---

    @Test
    void testBoundary_lengthZero_throws() {
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(""));
    }

    @Test
    void testBoundary_lengthOne_accepted() {
        assertDoesNotThrow(() -> gradebook.validateName("A"));
    }

    @Test
    void testBoundary_length49_accepted() {
        String name = "A".repeat(49);
        assertDoesNotThrow(() -> gradebook.validateName(name));
    }

    @Test
    void testBoundary_length50_accepted() {
        String name = "A".repeat(50);
        assertDoesNotThrow(() -> gradebook.validateName(name));
    }

    @Test
    void testBoundary_length51_throws() {
        String name = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(name));
    }
}
