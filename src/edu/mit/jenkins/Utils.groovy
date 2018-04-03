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
    def action = currentBuild.rawBuild.getAction(WarningsResultAction.class)
    // Keep repeating action.getResult() because, if you assign it to a
    // variable, builds will fail with
    // java.io.NotSerializableException: org.jenkinsci.plugins.workflow.job.WorkflowRun.

    info = [
        newWarnings: action.getResult().getNumberOfNewWarnings(),
        fixedWarnings: action.getResult().getNumberOfFixedWarnings(),
        high: action.getResult().getNumberOfHighPriorityWarnings(),
        normal: action.getResult().getNumberOfNormalPriorityWarnings(),
        low: action.getResult().getNumberOfLowPriorityWarnings(),
        total: 0,
        diffDescription: '',
        totalDescription: '',
    ]
    info.total = info.high + info.normal + info.low

    switch (info.total) {
        case 0:
            info.totalDescription = "PyLint found no issues."
            break;
        default:
            info.totalDescription = "PyLint found ${info.total} issues."
            break;
    }
    

    switch (info) {
        case { it.newWarnings == 0 && it.fixedWarnings == 0 }:
            info.diffDescription = "PyLint found no new or fixed issues."
            break;
        case info.fixedWarnings == 0:
            info.diffDescription = "PyLint found ${info.newWarnings} new issues."
            break;
        case info.newWarnings == 0:
            info.diffDescription = "PyLint found ${info.fixedWarnings} fixed issues."
            break;
        default:
            info.diffDescription = "PyLint found ${info.newWarnings} new and ${info.fixedWarnings} fixed issues."
            break;
    }
    println info.diffDescription
    println info.totalDescription

    return info
}
