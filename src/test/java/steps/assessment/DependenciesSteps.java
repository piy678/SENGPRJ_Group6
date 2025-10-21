package steps.assessment;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DependenciesSteps {
    @Given("LEO {string} exists")
    public void leoExists(String arg0) {
    }

    @Given("student {string} has not reached {string}")
    public void studentHasNotReached(String arg0, String arg1) {
    }

    @When("the teacher marks {string} as {string} for {string}")
    public void theTeacherMarksAsFor(String arg0, String arg1, String arg2) {
    }

    @Then("{string} becomes {string} for {string}")
    public void becomesFor(String arg0, String arg1, String arg2) {
    }

    @And("the system logs {string}")
    public void theSystemLogs(String arg0) {
    }

    @Given("LEO {string} exists and LEO {string} exists")
    public void leoExistsAndLEOExists(String arg0, String arg1) {
    }

    @And("{string} is already a prerequisite of {string}")
    public void isAlreadyAPrerequisiteOf(String arg0, String arg1) {
    }

    @When("the teacher attempts to set {string} as a prerequisite of {string}")
    public void theTeacherAttemptsToSetAsAPrerequisiteOf(String arg0, String arg1) {
    }

    @Then("the system rejects the relationship with error {string}")
    public void theSystemRejectsTheRelationshipWithError(String arg0) {
    }

    @Given("{string} is {string} for student {string}")
    public void isForStudent(String arg0, String arg1, String arg2) {
    }

    @When("the teacher marks {string} as {string}")
    public void theTeacherMarksAs(String arg0, String arg1) {
    }

    @Then("{string} remains {string}")
    public void remains(String arg0, String arg1) {
    }

    @Given("the dependency graph for course {string} exists")
    public void theDependencyGraphForCourseExists(String arg0) {
    }

    @When("the system recalculates dependency statuses twice")
    public void theSystemRecalculatesDependencyStatusesTwice() {
    }

    @Then("the resulting statuses are identical in both runs")
    public void theResultingStatusesAreIdenticalInBothRuns() {
    }
}
