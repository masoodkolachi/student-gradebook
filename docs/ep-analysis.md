# Equivalence Partitioning Analysis — GradeBook

## 1. `letterGrade(score)` — worked example, for reference

| Class          | Range     | Representative | Expected     |
| -------------- | --------- | --------------- | ------------ |
| Invalid-low    | < 0       | -10             | throws       |
| F              | 0-59      | 45              | `"F"`        |
| D              | 60-69     | 65              | `"D"`        |
| C              | 70-79     | 75              | `"C"`        |
| B              | 80-89     | 85              | `"B"`        |
| A              | 90-100    | 95              | `"A"`        |
| Invalid-high   | > 100     | 150             | throws       |

## 2. Scores-per-student count (`Roster.addStudent`)

Business rule: a student must have between 1 and 6 scores to be finalised
onto the roster.

| Class          | Range     | Representative | Expected     |
| -------------- | --------- | --------------- | ------------ |
| Invalid-low    | 0 scores  | 0               | throws       |
| Valid          | 1-6       | 3               | accepted     |
| Invalid-high   | 7+        | 8               | throws       |

## 3. Student name field (`validateName`)

Business rule: non-empty string, max 50 characters, letters/spaces/hyphens
only.

| Class                        | Example              | Expected     |
| ----------------------------- | -------------------- | ------------ |
| Valid typical name             | `"Ali Raza"`          | accepted     |
| Valid name with hyphen         | `"Anne-Marie"`        | accepted     |
| Empty string                   | `""`                  | throws       |
| Over-length string (>50 chars) | `"A" * 51`            | throws       |
| Contains digits/symbols        | `"Ali123"`            | throws       |

Five classes are listed here (one more than the 4 the lab requires) because
the rule explicitly allows hyphens, so a hyphenated name is its own useful
"valid" class distinct from a plain space-separated name — it's the kind of
case EP is meant to surface on its own.

## 4. Limitation of Equivalence Partitioning

EP picks one representative value per class, so it's built to test whether
the system treats a whole range consistently — it is not built to catch
off-by-one errors sitting exactly on a class boundary. For example, EP's
representative for the F class might be 45 and for D might be 65, but
neither of those tells you anything about whether `letterGrade(59)` and
`letterGrade(60)` are correctly on opposite sides of the F/D line, or
whether `Roster.addStudent` correctly accepts exactly 6 scores and rejects
exactly 7. A bug that only shows up at 59 vs. 60, or at 6 vs. 7, can slip
straight through an EP-only test suite. That gap is exactly what Boundary
Value Analysis in Lab 6 is for — it deliberately targets the edges EP
glosses over.

## 5. Test run summary

Run with:
```
java -jar lib/junit-platform-console-standalone.jar --class-path out --scan-class-path
```

Output:
```
.
+-- JUnit Jupiter [OK]
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
| | +-- testRejectsStudentWithEightScores() [OK]
| | +-- testRejectsStudentWithZeroScores() [OK]
| | '-- testAcceptsStudentWithThreeScores() [OK]
| '-- ValidateNameTest [OK]
|   +-- testNameWithHyphenAccepted() [OK]
|   +-- testOverLengthNameRejected() [OK]
|   +-- testValidTypicalName() [OK]
|   +-- testEmptyNameRejected() [OK]
|   '-- testNameWithDigitsRejected() [OK]
+-- JUnit Vintage [OK]
'-- JUnit Platform Suite [OK]

Test run finished after 455 ms
[         8 containers found      ]
[         0 containers skipped    ]
[         8 containers started    ]
[         0 containers aborted    ]
[         8 containers successful ]
[         0 containers failed     ]
[        15 tests found           ]
[         0 tests skipped         ]
[        15 tests started         ]
[         0 tests aborted         ]
[        15 tests successful      ]
[         0 tests failed          ]
```

All 15 tests across the three EP-derived test classes passed on the first
run, with zero failures.
