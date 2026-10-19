# Unit Testing Notes - Lab 7

These notes cover how the test suite is configured and run, what the two
output modes look like on a passing and a failing run, and when to use each.
The project is Java with JUnit 5, so the pytest tools named in the lab handout
have been swapped for their JUnit equivalents (see the table at the end).

## 1. Running the suite

Everything is configured in `junit-console.args` in the repo root:

```
--class-path=out
--scan-class-path
--include-classname=.*Test
```

- `--class-path=out` points the runner at the compiled classes.
- `--scan-class-path` makes the runner discover the tests itself, so there is
  no list of test classes to keep up to date.
- `--include-classname=.*Test` is the naming rule: a class is a test class if
  its name ends in `Test`.

The code has to be compiled before every run, because the runner only runs
what is already in `out/`. It never recompiles.

```
javac -cp lib/junit-platform-console-standalone.jar -d out src/gradebook/*.java test/gradebook/*.java
java -jar lib/junit-platform-console-standalone.jar "@junit-console.args" --details=tree
```

(The quotes around `@junit-console.args` are needed in PowerShell, which
otherwise treats a bare `@` as something else.)

## 2. Fixture scopes used in the tests

| Scope | JUnit tool | Used in | What it holds |
| ----- | ---------- | ------- | ------------- |
| Function (per test) | `@BeforeEach` | `RosterTest`, `RosterAverageTest`, `RosterSaveTest`, `GradebookAddScoreTest` | A fresh `Roster`, student or fake file for every test |
| Module (once per file) | `@BeforeAll` on a static method | `ValidateNameTest` | The 49, 50 and 51 character test names, built once |

Anything a test can change (a roster, a list, a fake file) goes in a
function-scoped fixture, so one test can never affect another. Only data that
no test modifies, and that is wasteful to rebuild, belongs in a once-per-file
fixture. JUnit has no separate "module" scope like pytest, so `@BeforeAll` is
the closest match.

## 3. Verbose output versus short output

JUnit has no exact copy of pytest's `--tb=short`. The closest pair is
`--details=tree` (every test listed, like `pytest -v`) and `--details=summary`
(totals and failures only). All four runs below were done on the same code,
once with everything passing and once with one test deliberately broken.

The runner prints a "Thanks for using JUnit" banner and a deprecation warning
around each run. Those are left out below.

### 3.1 Passing run, verbose (`--details=tree`)

```
.
+-- JUnit Jupiter [OK]
| +-- GradebookAddScoreTest [OK]
| | '-- addScore_sweep(String, double, boolean) [OK]
| |   +-- zero is accepted (lower boundary) [OK]
| |   +-- typical whole-number score is accepted [OK]
| |   +-- typical decimal score is accepted [OK]
| |   +-- maximum score 100 is accepted [OK]
| |   +-- tiny positive score is accepted [OK]
| |   +-- just below zero is rejected [OK]
| |   +-- small negative score is rejected [OK]
| |   '-- large negative score is rejected [OK]
| +-- LetterGradeBvaTest [OK]
| | +-- testBoundaryValuesReturnCorrectGrade(double, String) [OK]
| | | +-- [1] 0, F [OK]
| | | +-- [2] 1, F [OK]
| | | +-- [3] 59, F [OK]
| | | +-- [4] 60, D [OK]
| | | +-- [5] 61, D [OK]
| | | +-- [6] 69, D [OK]
| | | +-- [7] 70, C [OK]
| | | +-- [8] 71, C [OK]
| | | +-- [9] 79, C [OK]
| | | +-- [10] 80, B [OK]
| | | +-- [11] 81, B [OK]
| | | +-- [12] 89, B [OK]
| | | +-- [13] 90, A [OK]
| | | +-- [14] 91, A [OK]
| | | +-- [15] 99, A [OK]
| | | '-- [16] 100, A [OK]
| | '-- testDomainEdgeValuesThrow(double) [OK]
| |   +-- [1] -1.0 [OK]
| |   '-- [2] 101.0 [OK]
| +-- LetterGradeTest [OK]
| | +-- testLetterGradeValidClasses(double, String) [OK]
| | | +-- [1] 45, F [OK]
| | | +-- [2] 65, D [OK]
| | | +-- [3] 75, C [OK]
| | | +-- [4] 85, B [OK]
| | | '-- [5] 95, A [OK]
| | '-- testLetterGradeInvalidClasses(double) [OK]
| |   +-- [1] -10.0 [OK]
| |   '-- [2] 150.0 [OK]
| +-- RosterTest [OK]
| | +-- testBoundary_sixScores_accepted() [OK]
| | +-- testBoundary_oneScore_accepted() [OK]
| | +-- testBoundary_twoScores_accepted() [OK]
| | +-- testBoundary_zeroScores_throws() [OK]
| | +-- testRejectsStudentWithEightScores() [OK]
| | +-- testBoundary_sevenScores_throws() [OK]
| | +-- testBoundary_fiveScores_accepted() [OK]
| | +-- testRejectsStudentWithZeroScores() [OK]
| | '-- testAcceptsStudentWithThreeScores() [OK]
| +-- RosterSaveTest [OK]
| | +-- testSaveToFile_emptyRoster_writesNothing() [OK]
| | +-- testSaveToFile_openFails_throwsGradeBookIOException() [OK]
| | '-- testSaveToFile_writesExpectedContent_withoutTouchingDisk() [OK]
| +-- ValidateNameTest [OK]
| | +-- testBoundary_length51_throws() [OK]
| | +-- testNameWithHyphenAccepted() [OK]
| | +-- testOverLengthNameRejected() [OK]
| | +-- testValidTypicalName() [OK]
| | +-- testEmptyNameRejected() [OK]
| | +-- testBoundary_length50_accepted() [OK]
| | +-- testBoundary_length49_accepted() [OK]
| | +-- testNameWithDigitsRejected() [OK]
| | +-- testBoundary_lengthZero_throws() [OK]
| | '-- testBoundary_lengthOne_accepted() [OK]
| '-- RosterAverageTest [OK]
|   +-- testClassAverage_singleStudent_equalsThatStudentsAverage() [OK]
|   +-- testClassAverage_multipleStudents_averagesTheAverages() [OK]
|   +-- testClassAverage_isNotTheSameAsAverageOfAllScores() [OK]
|   '-- testClassAverage_emptyRoster_returnsZero() [OK]
+-- JUnit Vintage [OK]
'-- JUnit Platform Suite [OK]

Test run finished after 915 ms
[        15 containers found      ]
[         0 containers skipped    ]
[        15 containers started    ]
[         0 containers aborted    ]
[        15 containers successful ]
[         0 containers failed     ]
[        59 tests found           ]
[         0 tests skipped         ]
[        59 tests started         ]
[         0 tests aborted         ]
[        59 tests successful      ]
[         0 tests failed          ]
```

### 3.2 Passing run, short (`--details=summary`)

```
Test run finished after 683 ms
[        15 containers found      ]
[         0 containers skipped    ]
[        15 containers started    ]
[         0 containers aborted    ]
[        15 containers successful ]
[         0 containers failed     ]
[        59 tests found           ]
[         0 tests skipped         ]
[        59 tests started         ]
[         0 tests aborted         ]
[        59 tests successful      ]
[         0 tests failed          ]
```

### 3.3 Failing run, short (`--details=summary`)

For this run, line 68 of `RosterAverageTest.java` was temporarily changed from
`assertEquals(77.5, result, DELTA)` to `assertEquals(80.0, result, DELTA)` so
that one test would fail. It was changed back afterwards.

```
Failures (1):
  JUnit Jupiter:RosterAverageTest:testClassAverage_multipleStudents_averagesTheAverages()
    MethodSource [className = 'gradebook.RosterAverageTest', methodName = 'testClassAverage_multipleStudents_averagesTheAverages', methodParameterTypes = '']
    => org.opentest4j.AssertionFailedError: expected: <80.0> but was: <77.5>
       org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
       org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
       org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
       org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:86)
       org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:81)
       org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1014)
       gradebook.RosterAverageTest.testClassAverage_multipleStudents_averagesTheAverages(RosterAverageTest.java:68)
       java.base/java.lang.reflect.Method.invoke(Method.java:580)
       java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
       java.base/java.util.ArrayList.forEach(ArrayList.java:1597)

Test run finished after 674 ms
[        15 containers found      ]
[         0 containers skipped    ]
[        15 containers started    ]
[         0 containers aborted    ]
[        15 containers successful ]
[         0 containers failed     ]
[        59 tests found           ]
[         0 tests skipped         ]
[        59 tests started         ]
[         0 tests aborted         ]
[        58 tests successful      ]
[         1 tests failed          ]
```

### 3.4 Failing run, verbose (`--details=tree`)

This is an excerpt. Every other test class in the tree is identical to 3.1
(all `[OK]`), so only the failing class is shown, followed by the failure
section and totals.

```
| '-- RosterAverageTest [OK]
|   +-- testClassAverage_singleStudent_equalsThatStudentsAverage() [OK]
|   +-- testClassAverage_multipleStudents_averagesTheAverages() [X] expected: <80.0> but was: <77.5>
|   +-- testClassAverage_isNotTheSameAsAverageOfAllScores() [OK]
|   '-- testClassAverage_emptyRoster_returnsZero() [OK]

Failures (1):
  JUnit Jupiter:RosterAverageTest:testClassAverage_multipleStudents_averagesTheAverages()
    MethodSource [className = 'gradebook.RosterAverageTest', methodName = 'testClassAverage_multipleStudents_averagesTheAverages', methodParameterTypes = '']
    => org.opentest4j.AssertionFailedError: expected: <80.0> but was: <77.5>
       (same ten stack frames as in 3.3)

Test run finished after 697 ms
[        59 tests found           ]
[        58 tests successful      ]
[         1 tests failed          ]
```

## 4. What the comparison showed

- **On a passing run**, verbose output lists all 59 tests by name, while short
  output is just the totals. Both end with the same numbers.
- **On a failing run**, the "Failures" section is identical in both modes:
  the same message, the same ten stack frames, ending at
  `RosterAverageTest.java:68`. This is different from pytest, where
  `--tb=short` really does shorten the trace. JUnit's `--details` only
  controls whether the tree is printed.
- **The one thing verbose adds on a failure** is the `[X]` marker with the
  message right next to the failing test inside the tree, so you can see which
  test failed in the context of its neighbours.
- **Both modes find the failure.** The summary run reported `1 tests failed`
  just as clearly as the tree run did.

## 5. When to use each

**Short (`--details=summary`)** is the everyday choice while developing and
the sensible default for CI logs. A green run is a single screen of totals, and
a red run goes straight to the failure message and trace without 70 lines of
passing tests above it.

**Verbose (`--details=tree`)** is for when the list itself is the evidence:
checking that a new test was actually discovered, confirming which parametrized
cases ran (the descriptions in `GradebookAddScoreTest` only show up here), or
producing output for a report or screenshot. It is also useful when several
tests fail at once and you want to see them against the tests that passed.

## 6. One practical gotcha

The first attempt at the deliberate failure still reported 59 passing tests.
The edit was saved, but the code had not been recompiled, so the runner was
running the old classes in `out/`. After every change to a source or test file,
run the `javac` command again before the test run.

## 7. pytest to JUnit equivalents used in this lab

| Lab handout (pytest) | This project (JUnit 5) |
| -------------------- | ---------------------- |
| `@pytest.fixture` (function scope) | `@BeforeEach` |
| `@pytest.fixture(scope="module")` | `@BeforeAll` on a static method |
| `mocker.patch("builtins.open", ...)` | A `WriterFactory` passed into `Roster`, with a fake writer in the test |
| `@pytest.mark.parametrize(..., ids=[...])` | `@ParameterizedTest(name = "{0}")` with `@MethodSource` |
| `pytest.ini` | `junit-console.args` |
| `pytest -v` / `--tb=short` | `--details=tree` / `--details=summary` |
