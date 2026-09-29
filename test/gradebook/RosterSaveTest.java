package gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lab 7, Task 3 - tests for Roster.saveToFile() with the file system
 * replaced by a fake, so no real file is ever written.
 *
 * The lab uses pytest-mock to patch Python's built-in open(). Java has no
 * built-in open() to patch, so Roster takes its "open a file" step as a
 * WriterFactory. Here the factory hands back an in-memory writer that
 * records everything written to it (or throws, to simulate a failure).
 */
class RosterSaveTest {

    private static final AtomicInteger NEXT_ROLL = new AtomicInteger(1);
    private static final String PATH = "no/such/folder/roster.csv";

    /** In-memory stand-in for a file: captures what was written and whether it was closed. */
    private static class RecordingWriter extends StringWriter {
        boolean closed = false;

        @Override
        public void close() throws IOException {
            closed = true;
            super.close();
        }
    }

    private RecordingWriter fakeFile;
    private List<String> openedPaths;
    private String aliRoll;
    private String saraRoll;

    // Function-scoped fixture: every test gets a fresh fake file and fresh
    // students, so tests cannot affect each other.
    @BeforeEach
    void setUp() {
        fakeFile = new RecordingWriter();
        openedPaths = new ArrayList<>();
        aliRoll = "SV-" + NEXT_ROLL.getAndIncrement();
        saraRoll = "SV-" + NEXT_ROLL.getAndIncrement();
    }

    private Roster rosterWithFakeFile() {
        return new Roster(path -> {
            openedPaths.add(path);
            return fakeFile;
        });
    }

    private void addAliAndSara(Roster roster) {
        gradebook ali = new gradebook("Ali", aliRoll);
        ali.addScore(80);
        ali.addScore(90);
        gradebook sara = new gradebook("Sara", saraRoll);
        sara.addScore(70);
        roster.addStudent(ali);
        roster.addStudent(sara);
    }

    @Test
    void testSaveToFile_writesExpectedContent_withoutTouchingDisk() {
        // Arrange
        Roster roster = rosterWithFakeFile();
        addAliAndSara(roster);
        String expected = aliRoll + ",Ali,2,85.00\n" + saraRoll + ",Sara,1,70.00\n";
        // Act
        roster.saveToFile(PATH);
        // Assert
        assertEquals(List.of(PATH), openedPaths);
        assertEquals(expected, fakeFile.toString());
        assertTrue(fakeFile.closed, "writer should be closed after saving");
        assertFalse(new File(PATH).exists(), "no real file should have been created");
    }

    @Test
    void testSaveToFile_emptyRoster_writesNothing() {
        // Arrange
        Roster roster = rosterWithFakeFile();
        // Act
        roster.saveToFile(PATH);
        // Assert
        assertEquals("", fakeFile.toString());
        assertTrue(fakeFile.closed);
    }

    @Test
    void testSaveToFile_openFails_throwsGradeBookIOException() {
        // Arrange: opening the file always fails, like a full disk or bad path
        IOException diskProblem = new IOException("disk full");
        Roster roster = new Roster(path -> {
            throw diskProblem;
        });
        addAliAndSara(roster);
        // Act
        GradeBookIOException thrown =
                assertThrows(GradeBookIOException.class, () -> roster.saveToFile(PATH));
        // Assert: a clear message, and the original problem kept as the cause
        assertTrue(thrown.getMessage().contains(PATH));
        assertInstanceOf(IOException.class, thrown.getCause());
        assertEquals("disk full", thrown.getCause().getMessage());
    }
}
