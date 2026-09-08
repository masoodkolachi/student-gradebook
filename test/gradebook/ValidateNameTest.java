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
}
