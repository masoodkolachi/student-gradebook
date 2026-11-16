# Code Review Checklist - GradeBook

Used for every pull request in the student-gradebook project. Each item is a
yes/no question. If the answer is "no" or "not sure", leave a comment on the
exact line and say what you would change.

## 1. Correctness

- [ ] Does the code do what the PR description and the requirement say, for normal inputs?
- [ ] Are the edge cases handled: empty roster, a roster with one student, tied averages, a score of 0 and a score of 100?
- [ ] Are comparisons and arithmetic right at the boundaries (`<` vs `<=`, off-by-one in an index, division by zero)?
- [ ] Is bad input rejected with a clear exception and message instead of being ignored or producing a wrong number?

## 2. Tests

- [ ] Does every new method or changed behaviour have tests, including at least one edge case?
- [ ] Do the tests check the behaviour the requirement asks for, instead of just repeating whatever the code returns?
- [ ] Is each test independent (fresh fixture, unique roll number) so it passes in any order?
- [ ] Does the whole suite still pass after a fresh build, not only the new tests?

## 3. Readability

- [ ] Do names say what things are or do (no `x`, `tmp`, `data`, `result2`)?
- [ ] Is each method short and focused on one job, without deep nesting?
- [ ] Does every public method have a Javadoc comment covering what it does, its parameters, its return value and the exceptions it throws?
- [ ] Are there any magic numbers or leftover commented-out code that should be a named constant or deleted?

## 4. Design

- [ ] Does the code live in the right class (ranking logic in `Ranking`, storage in `Roster`, one student's data in `gradebook`)?
- [ ] Is any logic copied from somewhere else when an existing method could be reused?
- [ ] Does it avoid changing its inputs (for example, sorting the roster's own list) and avoid changing the public behaviour of existing methods?

## 5. Risk

- [ ] Could this break existing callers or earlier tests (backwards compatibility)?
- [ ] Is there any needlessly slow work, such as recalculating an average inside a loop over every student?
- [ ] Are inputs validated, exceptions never swallowed silently, and no passwords, tokens or file paths hard-coded?

## How to review with this list

1. Read the PR description first, so you know what the change is meant to do.
2. Go through the diff once per category, not once overall. You catch more that way.
3. Comment on the line, say what is wrong and why, and suggest a fix. Use a suggestion block when the fix is a single line.
4. Submit the review as Approve, Comment or Request changes, with a short summary at the top.

## Lessons learned

The checklist item that caught the most valuable issue was "Are comparisons and arithmetic right at the boundaries". It found that percentile divided by n instead of n - 1, so the top student could never reach 100 and a one-student roster returned 0. The tests would not have caught it because none existed for those cases.

One item I would add for next time: "Does any lookup (such as indexOf) handle the not-found case?" The first version returned a wrong number when the student was not on the roster.
