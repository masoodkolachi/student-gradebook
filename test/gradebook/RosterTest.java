package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 5 (EP) and Lab 6 (BVA) tests for Roster.addStudent(), refactored in
 * Lab 7 Task 1 to use a shared fixture instead of repeating setup.
 *
 * Fixture scope note (function scope):
 * The @BeforeEach method below runs before every single test, so each test
 * gets a brand-new, empty Roster. Use function scope for anything that a
 * test can change (a roster, a list, a counter). If tests shared one roster,
 * a student added in one test would still be there in the next and the
 * results would depend on the order the tests run in.
 */
class RosterTest {

    // The gradebook class rejects duplicate roll numbers across the whole
    // JVM, so every student created by a fixture needs a roll number nobody
    // has used yet. A counter gives us that without hand-typing "R-ZERO",
    // "BVA-1" and so on in every test.
    private static final AtomicInteger NEXT_ROLL = new AtomicInteger(1);

    private Roster roster;

    @BeforeEach
    void setUp() {
        roster = new Roster();
    }

    private gradebook studentWithScores(int count) {
        String rollNo = "RT-" + NEXT_ROLL.getAndIncrement();
        gradebook g = new gradebook("Test Student", rollNo);
        for (int i = 0; i < count; i++) {
            g.addScore(70);
        }
        return g;
    }

    // --- Lab 5, Task 3: EP tests (0 invalid, 1-6 valid, 7+ invalid) ---

    @Test
    void testRejectsStudentWithZeroScores() {
        gradebook g = studentWithScores(0);
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }

    @Test
    void testAcceptsStudentWithThreeScores() {
        roster.addStudent(studentWithScores(3));
        assertEquals(1, roster.size());
    }

    @Test
    void testRejectsStudentWithEightScores() {
        gradebook g = studentWithScores(8);
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }

    // --- Lab 6, Task 3: boundary tests around the min (1) and max (6) ---

    @Test
    void testBoundary_zeroScores_throws() {
        gradebook g = studentWithScores(0);
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }

    @Test
    void testBoundary_oneScore_accepted() {
        roster.addStudent(studentWithScores(1));
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_twoScores_accepted() {
        roster.addStudent(studentWithScores(2));
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_fiveScores_accepted() {
        roster.addStudent(studentWithScores(5));
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_sixScores_accepted() {
        roster.addStudent(studentWithScores(6));
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_sevenScores_throws() {
        gradebook g = studentWithScores(7);
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }
}
