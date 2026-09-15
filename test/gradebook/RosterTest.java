package gradebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 5, Task 3 — EP tests for Roster.addStudent(), covering the 3 classes
 * from Task 1: 0 scores (invalid), 1-6 scores (valid), 7+ scores (invalid).
 * Representative values used: 0, 3, 8.
 */
class RosterTest {

    private gradebook studentWithScores(int count, String rollNo) {
        gradebook g = new gradebook("Test Student", rollNo);
        for (int i = 0; i < count; i++) {
            g.addScore(70);
        }
        return g;
    }

    @Test
    void testRejectsStudentWithZeroScores() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(0, "R-ZERO");
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }

    @Test
    void testAcceptsStudentWithThreeScores() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(3, "R-THREE");
        roster.addStudent(g);
        assertEquals(1, roster.size());
    }

    @Test
    void testRejectsStudentWithEightScores() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(8, "R-EIGHT");
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }

    // --- Lab 6, Task 3: boundary tests around the min (1) and max (6) ---
    // --- edges of the scores-per-student rule.                        ---

    @Test
    void testBoundary_zeroScores_throws() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(0, "BVA-0");
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }

    @Test
    void testBoundary_oneScore_accepted() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(1, "BVA-1");
        roster.addStudent(g);
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_twoScores_accepted() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(2, "BVA-2");
        roster.addStudent(g);
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_fiveScores_accepted() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(5, "BVA-5");
        roster.addStudent(g);
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_sixScores_accepted() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(6, "BVA-6");
        roster.addStudent(g);
        assertEquals(1, roster.size());
    }

    @Test
    void testBoundary_sevenScores_throws() {
        Roster roster = new Roster();
        gradebook g = studentWithScores(7, "BVA-7");
        assertThrows(IllegalArgumentException.class, () -> roster.addStudent(g));
    }
}
