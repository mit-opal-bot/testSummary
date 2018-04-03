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
    def warningActions = currentBuild.rawBuild.getActions(WarningsResultAction.class)
    // result = warningActions[0].getResult()
    // println result
    // println result.getNumberOfNewWarnings()
    // println result.getNumberOfFixedWarnings()
    // println result.getNumberOfHighPriorityWarnings()
    // println result.getNumberOfNormalPriorityWarnings()
    // println result.getNumberOfLowPriorityWarnings()
    // println high + normal + low

    // info = [
    //     newWarnings: result.getNumberOfNewWarnings(),
    //     fixedWarnings: result.getNumberOfFixedWarnings(),
    //     high: result.getNumberOfHighPriorityWarnings(),
    //     normal: result.getNumberOfNormalPriorityWarnings(),
    //     low: result.getNumberOfLowPriorityWarnings(),
    //     total: high + normal + low,
    //     description: ''
    // ]

    // switch (info) {
    //     case { it.newWarnings == 0 && it.fixedWarnings == 0 }:
    //         info.description = "PyLint found no new or fixed issues."
    //         break;
    //     case info.fixedWarnings == 0:
    //         info.description = "PyLint found ${info.newWarnings} new issues."
    //         break;
    //     case info.newWarnings == 0:
    //         info.description = "PyLint found ${info.fixedWarnings} fixed issues."
    //         break;
    //     default:
    //         info.description = "PyLint found ${info.newWarnings} new and ${info.fixedWarnings} fixed issues."
    //         break;
    // }

    return "info"
}
