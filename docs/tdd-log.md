# TDD Log - rankStudents

Feature: `Ranking.rankStudents(roster)` returns the students on a `Roster`
sorted by average score, highest first. Students with the same average are
ordered alphabetically by name, ignoring letter case.

Branch: `feature/rank-students-tdd`
Tests: `test/gradebook/RankStudentsTest.java`
Code: `src/gradebook/Ranking.java`, plus a `getStudents()` method on `Roster`

The Java/JUnit 5 versions of the handout's pytest tools were used. Every Red
step was run and seen to fail before the matching Green step was written.

## Cycles

### Cycle 1 - Empty roster

| Phase | Commit | Message | Result |
| ----- | ------ | ------- | ------ |
| Red | `7c45dab` | test(rank): red - empty roster returns empty list | Test does not compile, because `Ranking` does not exist yet |
| Green | `846db17` | feat(rank): green - stub rankStudents returning empty list | 60 passed, 0 failed |
| Refactor | none | | Nothing to clean up at this size |

### Cycle 2 - Single student

| Phase | Commit | Message | Result |
| ----- | ------ | ------- | ------ |
| Red | `581e0c7` | test(rank): red - single student returns that student | 60 passed, 1 failed (got an empty list) |
| Green | `5b9f6b2` | feat(rank): green - return the roster's students (single student passes) | 61 passed, 0 failed |
| Refactor | `30164b9` | refactor(rank): share a fresh Roster across tests via @BeforeEach | 61 passed, 0 failed |

Green added `Roster.getStudents()`, which returns a read-only copy of the
list, because the ranking code had no way to see the students otherwise.
Refactor removed the repeated `new Roster()` from each test.

### Cycle 3 - Sort by average, highest first

| Phase | Commit | Message | Result |
| ----- | ------ | ------- | ------ |
| Red | `e0fbb76` | test(rank): red - students sorted by average, highest first | 61 passed, 1 failed |
| Green | `ea0e5ce` | feat(rank): green - sort students by average, descending | 62 passed, 0 failed |
| Refactor | `43309b0` | refactor(rank): extract student(name, scores...) test helper | 62 passed, 0 failed |

The test adds the students in the order low, high, mid, so the code cannot
pass by luck. Refactor pulled the repeated "create a student and add scores"
lines into a helper. The helper gives every student a unique roll number,
because `gradebook` rejects duplicate roll numbers.

### Cycle 4 - Tie-breaking

| Phase | Commit | Message | Result |
| ----- | ------ | ------- | ------ |
| Red | `378ed9b` | test(rank): red - equal averages are ordered alphabetically by name | 62 passed, 1 failed |
| Green | `22c5ae7` | feat(rank): green - break average ties by name | 63 passed, 0 failed |
| Refactor | `e572b2a` | refactor(rank): name the sort order as a comparator constant | 63 passed, 0 failed |

The sort order was pulled out into a constant named
`BY_AVERAGE_DESC_THEN_NAME`. The whole suite was re-run after this refactor,
which is the handout's regression check: all 59 tests from Labs 4 to 7 plus
the 4 ranking tests passed.

**Regressions found: none.**

### Cycle 5 - My own edge case: names that differ only in letter case

| Phase | Commit | Message | Result |
| ----- | ------ | ------- | ------ |
| Red | `da713c2` | test(rank): red - tie-break ignores letter case in names | 63 passed, 1 failed |
| Green | `c3684c2` | feat(rank): green - compare names case-insensitively | 64 passed, 0 failed |
| Refactor | `4128757` | refactor(rank): document the ranking rules in Javadoc | 64 passed, 0 failed |

The handout suggests a student with zero scores as the edge case. That cannot
happen in this project: `Roster.addStudent` already rejects any student with
zero scores (the 1 to 6 scores rule from Lab 5), so there is no such student
to rank. I tested names that differ only in letter case instead. Java's
default string order puts every capital letter before every lowercase one, so
"Bilal" would have been ranked ahead of "ali". The fix uses
`String.CASE_INSENSITIVE_ORDER`. This is also the "case-sensitive name
comparison" item from the Lab 4 triage log.

## Final result

14 commits on the branch. Final full-suite run: 64 tests passed, 0 failed
(59 from Labs 4 to 7, 5 for `rankStudents`).

## Reflection

Writing each test first made me decide how the code would be used before I
wrote it. In Cycle 2 the ranking code had no way to see the students on a
roster, which is why `Roster` got a `getStudents()` method that returns a
read-only copy, and if I had written the implementation first I would likely
have just exposed the internal list. The small steps kept the code honest: the
stub that returned an empty list looked pointless, but I never wrote the sort
or the tie-break until a failing test asked for it. For Cycle 5 the handout's
zero-score suggestion turned out to be impossible because `Roster` already
rejects those students, so I tested names that differ only in letter case and
found that plain string comparison would have put "Bilal" ahead of "ali".
Because the suite stayed green through every refactor, tidying up was cheap,
and the full run in Cycle 4 passed with nothing from Labs 4 to 7 breaking.
