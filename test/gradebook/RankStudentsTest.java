package gradebook;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RankStudentsTest {

    @Test
    void testRankStudents_emptyRoster_returnsEmptyList() {
        Roster roster = new Roster();
        assertEquals(List.of(), Ranking.rankStudents(roster));
    }

    @Test
    void testRankStudents_singleStudent_returnsThatStudent() {
        Roster roster = new Roster();
        gradebook ali = new gradebook("Ali", "RK-A");
        ali.addScore(80);
        roster.addStudent(ali);
        assertEquals(List.of(ali), Ranking.rankStudents(roster));
    }
}