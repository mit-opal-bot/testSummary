package edu.mit.jenkins

import hudson.tasks.test.AbstractTestResultAction
import hudson.plugins.warnings.WarningsResultAction
import hudson.plugins.warnings.AggregatedWarningsResultAction
import hudson.model.Actionable

@NonCPS
def getTestSummary() {
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        def total = testResultAction.getTotalCount()
        def failed = testResultAction.getFailCount()
        def skipped = testResultAction.getSkipCount()

        summary = "Test results:\n\t"
        summary = summary + ("Passed: " + (total - failed - skipped))
        summary = summary + (", Failed: " + failed)
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
}

@NonCPS
def gitHubStatusForBuildResult(String inStatus) {
    switch (inStatus) {
        case "SUCCESS":
            return "SUCCESS"
        case ["UNSTABLE", "FAILURE"]:
            return "FAILURE"
        // case ["ERROR", "ABORTED", "NOT_BUILT"]:
        default:
            return "ERROR"
    }
}

@NonCPS
def warningsInfo() {
    def warningAction = currentBuild.rawBuild.getActions(WarningsResultAction.class)
    println "${warningAction.length} ${warningAction}"
    def aggregatedAction = currentBuild.rawBuild.getActions(AggregatedWarningsResultAction.class)
    println "${aggregatedAction.length} ${aggregatedAction}"
}
