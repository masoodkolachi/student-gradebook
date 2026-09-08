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
}
