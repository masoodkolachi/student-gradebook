# Coverage Notes - GradeBook (Lab 11)

Tool: JaCoCo 0.8.12 (the Java equivalent of coverage.py / pytest-cov), run
with `coverage.ps1`. It measures line coverage and branch coverage for the
classes in `src/gradebook/`. Tests are the JUnit 5 suite from Labs 5 to 9.

## 1. Baseline (before any new tests)

| Measure | Result |
| ------- | ------ |
| Tests run | 64, all passing |
| Line coverage | 78.3% (72 of 92 lines) |
| Branch coverage | 83.3% (35 of 42 branches) |

Per class:

| Class | Lines | Branches |
| ----- | ----- | -------- |
| Student | 0.0% (0 of 17) | 0 of 4 |
| Ranking | 85.7% (6 of 7) | none |
| gradebook | 94.4% (34 of 36) | 25 of 28 |
| Roster | 100% (30 of 30) | 10 of 10 |
| GradeBookIOException | 100% (2 of 2) | none |

## 2. The three weakest classes and what was red

**Student (0%).** None of the class is tested. `studentAverage()` and the
whole `main` method (the loop over ten students, the printing, the overall
average) never ran. It is a small demo class from the start of the project and
had no test file at all.

**Ranking (85.7%).** The only red line is the class declaration. `Ranking`
only has static methods, but Java still creates a public default constructor
that nobody calls.

**gradebook (94.4%).** Two lines and three branches were red:
- the `throw` for a duplicate roll number (the `!registeredRollNumbers.add(...)`
  branch was never true);
- the `return 0.0` in `average()` for a student with no scores;
- the `name == null` branch in `validateName()`.

## 3. How the gaps were closed

| Gap | What I did |
| --- | ---------- |
| Student, all of it | New `StudentTest`: the average of three marks, the average of one mark, and a test that runs `main` and checks its printed output (an individual average and the overall line). |
| Duplicate roll number | New test in `GradebookEdgeCasesTest` that creates a second student with the same roll number and checks the exception and that its message names the roll number. |
| Average with no scores | Same file: a student with no scores returns 0.0. |
| Null name | Same file: `validateName(null)` throws `IllegalArgumentException`. |
| Ranking constructor | I did not write a test that calls `new Ranking()`, because that would only chase the number. Instead I made the constructor private, which is the normal way to write a class of static methods. JaCoCo ignores a private empty constructor. |

I re-ran coverage after each change and checked that the red lines turned
green before moving on.

## 4. Final result

| Measure | Baseline | Final |
| ------- | -------- | ----- |
| Tests | 64 | 70 |
| Line coverage | 78.3% | 100% (91 of 91) |
| Branch coverage | 83.3% | 100% (42 of 42) |

## 5. Coverage versus quality: a coverage trap

The test `testRejectsStudentWithZeroScores` in `RosterTest` used to check only
that `addStudent` throws an `IllegalArgumentException`. It reached 100% of the
lines in the rejection path, so the coverage report was happy, but it did not
check what state the roster was left in. A faulty version that added the
student first and threw afterwards would still pass it, and the roster would
silently contain a student who should have been refused.

I strengthened it with one more assertion, `assertEquals(0, roster.size())`,
so the test now checks the outcome and not only that an exception happened.
The coverage number did not change, which is the point: coverage tells you
which code ran, not whether the tests would notice if it was wrong.

The same idea applies to `StudentTest`. Calling `Student.main(...)` alone would
have covered every line of the method. The test also checks what `main` prints.

## 6. Enforced threshold

The Java projects here have no `pytest.ini`, so the rule lives in
`coverage.ps1`. It exits with code 1 if any test fails or if line coverage is
below 85%. I chose 85% because it is the figure in the lab handout, and it
leaves a safety margin below the 100% reached now.

To check it works, I temporarily removed `StudentTest`. Line coverage dropped
to 81.3% (74 of 91 lines), the script printed `FAILED`, and the exit code was
1. After restoring the file it passed again with exit code 0.
