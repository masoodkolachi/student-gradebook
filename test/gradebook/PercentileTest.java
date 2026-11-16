package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PercentileTest {

    private static final AtomicInteger NEXT_ROLL = new AtomicInteger(1);
    private Roster roster;

    @BeforeEach
    void setUp() {
        roster = new Roster();
    }

    private gradebook student(String name, double score) {
        gradebook g = new gradebook(name, "PC-" + NEXT_ROLL.getAndIncrement());
        g.addScore(score);
        return g;
    }

    @Test
    void singleStudentRoster_is100() {
        gradebook ali = student("Ali", 80);
        roster.addStudent(ali);
        assertEquals(100.0, Percentile.percentile(ali, roster), 0.0001);
    }

    @Test
    void topStudent_is100() {
        gradebook high = student("High", 90);
        gradebook low = student("Low", 60);
        roster.addStudent(high);
        roster.addStudent(low);
        assertEquals(100.0, Percentile.percentile(high, roster), 0.0001);
    }

    @Test
    void bottomStudent_is0() {
        gradebook high = student("High", 90);
        gradebook low = student("Low", 60);
        roster.addStudent(high);
        roster.addStudent(low);
        assertEquals(0.0, Percentile.percentile(low, roster), 0.0001);
    }

    @Test
    void middleOfThree_is50() {
        gradebook a = student("A", 90);
        gradebook b = student("B", 75);
        gradebook c = student("C", 60);
        roster.addStudent(a);
        roster.addStudent(b);
        roster.addStudent(c);
        assertEquals(50.0, Percentile.percentile(b, roster), 0.0001);
    }

    @Test
    void studentNotOnRoster_throws() {
        gradebook stranger = student("Stranger", 70);
        assertThrows(IllegalArgumentException.class, () -> Percentile.percentile(stranger, roster));
    }
}
