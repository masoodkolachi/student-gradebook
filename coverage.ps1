# Builds the project, runs the whole JUnit suite under JaCoCo, writes an HTML
# report and fails (exit code 1) if tests fail or line coverage is too low.
#
# Usage:   powershell -ExecutionPolicy Bypass -File .\coverage.ps1
#          powershell -ExecutionPolicy Bypass -File .\coverage.ps1 -OutDir coverage-before -MinLines 0
param(
    [string]$OutDir = "coverage-html",
    [double]$MinLines = 85
)

$junit = "lib/junit-platform-console-standalone.jar"

Remove-Item -Recurse -Force out-main, out-test, jacoco.exec, $OutDir -ErrorAction SilentlyContinue
New-Item -ItemType Directory out-main, out-test | Out-Null

javac -d out-main src/gradebook/*.java
if ($LASTEXITCODE -ne 0) { exit 1 }
javac -cp "out-main;$junit" -d out-test test/gradebook/*.java
if ($LASTEXITCODE -ne 0) { exit 1 }

java "-javaagent:lib/jacocoagent.jar=destfile=jacoco.exec" -jar $junit "--class-path=out-main" "--class-path=out-test" "--scan-class-path" "--include-classname=.*Test" "--details=summary"
$testsExit = $LASTEXITCODE

java -jar lib/jacococli.jar report jacoco.exec --classfiles out-main --sourcefiles src --html $OutDir --csv coverage.csv
if ($LASTEXITCODE -ne 0) { exit 1 }

$rows = Import-Csv coverage.csv
$lineCovered = ($rows | Measure-Object -Property LINE_COVERED -Sum).Sum
$lineMissed = ($rows | Measure-Object -Property LINE_MISSED -Sum).Sum
$branchCovered = ($rows | Measure-Object -Property BRANCH_COVERED -Sum).Sum
$branchMissed = ($rows | Measure-Object -Property BRANCH_MISSED -Sum).Sum
$linePct = [math]::Round(100 * $lineCovered / ($lineCovered + $lineMissed), 1)
$branchPct = [math]::Round(100 * $branchCovered / ($branchCovered + $branchMissed), 1)

Write-Host ""
Write-Host "Line coverage:   $linePct% ($lineCovered of $($lineCovered + $lineMissed) lines)"
Write-Host "Branch coverage: $branchPct% ($branchCovered of $($branchCovered + $branchMissed) branches)"
Write-Host "HTML report:     $OutDir\index.html"

if ($testsExit -ne 0) {
    Write-Host "FAILED: some tests failed"
    exit 1
}
if ($linePct -lt $MinLines) {
    Write-Host "FAILED: line coverage $linePct% is below the required $MinLines%"
    exit 1
}
Write-Host "OK: line coverage meets the required $MinLines%"
exit 0
