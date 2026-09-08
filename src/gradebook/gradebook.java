package gradebook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class gradebook {
    // Tracks every roll number already registered, across all instances,
    // so two students can't be created with the same roll number.
    // (Fixes the "Duplicate roll numbers allowed" item in docs/triage-log.md.)
    private static final Set<String> registeredRollNumbers = new HashSet<>();

    private String name;
    private String rollNo;
    private List<Double> scores;

    public gradebook(String name, String rollNo) {
        if (!registeredRollNumbers.add(rollNo)) {
            throw new IllegalArgumentException(
                    "Roll number already in use: " + rollNo);
        }
        this.name = name;
        this.rollNo = rollNo;
        this.scores = new ArrayList<>();
    }

    public double average() {
        if (scores.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double s : scores) {
            sum += s;
        }
        return sum / scores.size();
    }

    /**
     * Adds a score to this student's record.
     *
     * @param score the score to add; must be non-negative
     * @throws IllegalArgumentException if score is negative
     */
    public void addScore(double score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        scores.add(score);
    }

    /**
     * @return how many scores this student currently has on record.
     */
    public int getScoreCount() {
        return scores.size();
    }

    /**
     * Maps a numeric score (0-100) to a letter grade.
     * Equivalence classes: Invalid-low (&lt;0), F [0-59], D [60-69],
     * C [70-79], B [80-89], A [90-100], Invalid-high (&gt;100).
     *
     * @param score the score to convert
     * @return the letter grade "A" through "F"
     * @throws IllegalArgumentException if score is outside 0-100
     */
    public static String letterGrade(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 100, got: " + score);
        }
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    /**
     * Validates a student name: non-empty, max 50 characters,
     * letters/spaces/hyphens only.
     *
     * @param name the name to validate
     * @throws IllegalArgumentException if the name breaks any rule above
     */
    public static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Name cannot exceed 50 characters");
        }
        if (!name.matches("[A-Za-z \\-]+")) {
            throw new IllegalArgumentException(
                    "Name may only contain letters, spaces, and hyphens");
        }
    }
}