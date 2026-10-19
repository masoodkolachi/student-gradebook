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
}