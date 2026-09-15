# Boundary Value Analysis — GradeBook

## 1. `letterGrade(score)`

Boundaries: the 0/100 domain edges, plus the four grade cut-offs
(59/60, 69/70, 79/80, 89/90).

| Boundary          | value-1 | value | value+1 | expected @ value-1 | expected @ value | expected @ value+1 |
| ------------------ | ------- | ----- | ------- | -------------------- | ------------------ | --------------------- |
| Domain low (0)      | -1      | 0     | 1       | throws                | `"F"`               | `"F"`                  |
| F/D cut-off (60)    | 59      | 60    | 61      | `"F"`                 | `"D"`               | `"D"`                  |
| D/C cut-off (70)    | 69      | 70    | 71      | `"D"`                 | `"C"`               | `"C"`                  |
| C/B cut-off (80)    | 79      | 80    | 81      | `"C"`                 | `"B"`               | `"B"`                  |
| B/A cut-off (90)    | 89      | 90    | 91      | `"B"`                 | `"A"`               | `"A"`                  |
| Domain high (100)   | 99      | 100   | 101     | `"A"`                 | `"A"`               | throws                 |

18 distinct values in total, matching the domain edges (-1, 0, 1, 99, 100,
101) plus the four cut-offs' surrounding values.

## 2. Roster score-count rule (1-6 scores valid)

| Boundary        | value-1 | value | value+1 | expected @ value-1 | expected @ value | expected @ value+1 |
| ---------------- | ------- | ----- | ------- | -------------------- | ------------------ | --------------------- |
| Min count (1)     | 0       | 1     | 2       | throws                | accepted            | accepted               |
| Max count (6)     | 5       | 6     | 7       | accepted              | accepted            | throws                 |

## 3. Name length rule (max 50 characters, non-empty required)

| Boundary          | value-1 | value | value+1 | expected @ value-1 | expected @ value | expected @ value+1 |
| ------------------ | ------- | ----- | ------- | -------------------- | ------------------ | --------------------- |
| Min length (1)      | n/a (no negative length) | 0   | 1       | n/a                   | throws              | accepted               |
| Max length (50)     | 49      | 50    | 51      | accepted              | accepted            | throws                 |

The min-length boundary only has two meaningful sides (0 and 1) since a
length of -1 doesn't exist — Task 4 asks for exactly these five lengths
(0, 1, 49, 50, 51), which this table covers.

## 4. Result of this pass

Every boundary above was already handled correctly by the `>=`-based
cut-offs and the `count < 1 || count > 6` / `length() > 50` checks written
in Labs 4-5 — see `tests/` run output in the accompanying test files. This
pass surfaced **zero new off-by-one defects**: no boundary flipped to the
wrong side of its cut-off. That's a legitimate outcome of BVA (it confirms
correctness rather than assuming it), but it means there is no organically
discovered defect from this specific pass to file as a GitHub Issue per
the lab's "at least one filed-and-fixed defect" checklist item — that item
should be raised with the instructor rather than satisfied by inventing
one.
