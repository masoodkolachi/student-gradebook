package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RankStudentsTest {

    private Roster roster;

    @BeforeEach
    void setUp() {
        roster = new Roster();
    }

    @Test
    void testRankStudents_emptyRoster_returnsEmptyList() {
        assertEquals(List.of(), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_singleStudent_returnsThatStudent() {
        gradebook ali = new gradebook("Ali", "RK-A");
        ali.addScore(80);
        roster.addStudent(ali);
        assertEquals(List.of(ali), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_distinctAverages_sortedHighestFirst() {
        gradebook low = new gradebook("Low", "RK-B");
        low.addScore(60);
        gradebook high = new gradebook("High", "RK-C");
        high.addScore(90);
        gradebook mid = new gradebook("Mid", "RK-D");
        mid.addScore(75);
        roster.addStudent(low);
        roster.addStudent(high);
        roster.addStudent(mid);
        assertEquals(List.of(high, mid, low), Ranking.rankStudents(roster));
    }
}