package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RankStudentsTest {

    // gradebook rejects duplicate roll numbers across the whole test run,
    // so every student built here gets a roll number nobody else has used.
    private static final AtomicInteger NEXT_ROLL = new AtomicInteger(1);

    private Roster roster;

    @BeforeEach
    void setUp() {
        roster = new Roster();
    }

    private gradebook student(String name, double... scores) {
        gradebook g = new gradebook(name, "RK-" + NEXT_ROLL.getAndIncrement());
        for (double score : scores) {
            g.addScore(score);
        }
        return g;
    }

    @Test
    void testRankStudents_emptyRoster_returnsEmptyList() {
        assertEquals(List.of(), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_singleStudent_returnsThatStudent() {
        gradebook ali = student("Ali", 80);
        roster.addStudent(ali);
        assertEquals(List.of(ali), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_distinctAverages_sortedHighestFirst() {
        gradebook low = student("Low", 60);
        gradebook high = student("High", 90);
        gradebook mid = student("Mid", 75);
        roster.addStudent(low);
        roster.addStudent(high);
        roster.addStudent(mid);
        assertEquals(List.of(high, mid, low), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_equalAverages_brokenAlphabeticallyByName() {
        gradebook zara = student("Zara", 80);
        gradebook ali = student("Ali", 80);
        roster.addStudent(zara);
        roster.addStudent(ali);
        assertEquals(List.of(ali, zara), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_namesDifferingInCase_orderedIgnoringCase() {
        gradebook bilal = student("Bilal", 80);
        gradebook ali = student("ali", 80);
        roster.addStudent(bilal);
        roster.addStudent(ali);
        assertEquals(List.of(ali, bilal), Ranking.rankStudents(roster));
    }
}