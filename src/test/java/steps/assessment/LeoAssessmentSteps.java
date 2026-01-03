package steps.assessment;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LeoAssessmentSteps {
    @And("student {string} is enrolled in {string}")
    public void studentIsEnrolledIn(String arg0, String arg1) {
    }

    @And("course {string} has teacher {string}")
    public void courseHasTeacher(String arg0, String arg1) {
    }

    @And("a LEO named {string} exists in {string}")
    public void aLEONamedExistsIn(String arg0, String arg1) {
    }

    @Given("student {string} has no assessment for {string}")
    public void studentHasNoAssessmentFor(String arg0, String arg1) {
    }

    @When("teacher {string} records assessment {string} for {string} for student {string} with date {string}")
    public void teacherRecordsAssessmentForForStudentWithDate(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Then("the assessment is stored in database")
    public void theAssessmentIsStoredInDatabase() {
    }

    @And("{string} shows status {string} for {string}")
    public void showsStatusFor(String arg0, String arg1, String arg2) {
    }

    @And("assessed_by field contains {string}")
    public void assessed_byFieldContains(String arg0) {
    }

    @And("is_archived flag is false")
    public void is_archivedFlagIsFalse() {
    }

    @Given("student {string} has assessment {string} for {string} recorded on {string}")
    public void studentHasAssessmentForRecordedOn(String arg0, String arg1, String arg2, String arg3) {
    }

    @When("teacher {string} updates assessment to {string} for {string} on {string}")
    public void teacherUpdatesAssessmentToForOn(String arg0, String arg1, String arg2, String arg3) {
    }

    @Then("the current assessment shows status {string}")
    public void theCurrentAssessmentShowsStatus(String arg0) {
    }

    @And("the current assessment is_archived is false")
    public void theCurrentAssessmentIs_archivedIsFalse() {
    }

    @And("the previous assessment is marked as is_archived = true")
    public void thePreviousAssessmentIsMarkedAsIs_archivedTrue() {
    }

    @And("the current assessment has field previous_assessment_id linking to old assessment")
    public void theCurrentAssessmentHasFieldPrevious_assessment_idLinkingToOldAssessment() {
    }
}