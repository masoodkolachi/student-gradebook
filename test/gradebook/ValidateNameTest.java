package gradebook;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 5 (EP) and Lab 6 (BVA) tests for gradebook.validateName(), refactored
 * in Lab 7 Task 1 to build the long test names once in a shared fixture.
 *
 * Fixture scope note (module / class scope):
 * JUnit has no "module" scope like pytest. @BeforeAll on a static method
 * runs once for the whole test class, and since each class here lives in one
 * file, that is the closest match to pytest's scope="module". Use it for
 * setup that is slow or wasteful to repeat AND that no test will modify:
 * a database connection, a loaded file, a big read-only dataset. Strings are
 * immutable, so sharing them between tests is safe. (Building three short
 * strings is not really "expensive" - it is here to show the pattern.)
 * Anything a test can change belongs in a @BeforeEach fixture instead.
 */
class ValidateNameTest {

    private static String name49;
    private static String name50;
    private static String name51;

    @BeforeAll
    static void buildLongNames() {
        name49 = "A".repeat(49);
        name50 = "A".repeat(50);
        name51 = "A".repeat(51);
    }

    // --- Lab 5, Task 4: EP tests ---

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
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(name51));
    }

    @Test
    void testNameWithDigitsRejected() {
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName("Ali123"));
    }

    @Test
    void testNameWithHyphenAccepted() {
        assertDoesNotThrow(() -> gradebook.validateName("Anne-Marie"));
    }

    // --- Lab 6, Task 4: boundary tests around the min and max length ---

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
        assertDoesNotThrow(() -> gradebook.validateName(name49));
    }

    @Test
    void testBoundary_length50_accepted() {
        assertDoesNotThrow(() -> gradebook.validateName(name50));
    }

    @Test
    void testBoundary_length51_throws() {
        assertThrows(IllegalArgumentException.class, () -> gradebook.validateName(name51));
    }
}
