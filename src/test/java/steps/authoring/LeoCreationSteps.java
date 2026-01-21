package steps.authoring;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

public class LeoCreationSteps {

    @When("the teacher creates a LEO with:")
    public void theTeacherCreatesALEOWith(DataTable table) {
        Map<String, String> data = table.asMap(String.class, String.class);

        String name = data.get("name");
        String description = data.get("description");
        String course = data.get("course");
    }

    @Given("teacher {string} is on the LEO authoring page for course {string}")
    public void teacherIsOnTheLEOAuthoringPageForCourse(String arg0, String arg1) {
    }

    @Then("the LEO {string} is saved in the system")
    public void theLEOIsSavedInTheSystem(String arg0) {
    }

    @And("the LEO has:")
    public void theLEOHas(DataTable table) {
        Map<String, String> attributes = table.asMap(String.class, String.class);

        String courseId = attributes.get("course_id");
        String createdBy = attributes.get("created_by");
        String isActive = attributes.get("is_active");


    }


    @And("the LEO appears in the course LEO list with green indicator {string}")
    public void theLEOAppearsInTheCourseLEOListWithGreenIndicator(String arg0) {
    }

    @And("an audit log entry records: action={string}, created_by={string}")
    public void anAuditLogEntryRecordsActionCreated_by(String arg0, String arg1) {
    }

    @Given("LEO {string} exists in the course")
    public void leoExistsInTheCourse(String arg0) {
    }

    @When("teacher {string} sets {string} as a prerequisite of {string}")
    public void teacherSetsAsAPrerequisiteOf(String arg0, String arg1, String arg2) {
    }

    @Then("the relationship {string} is saved")
    public void theRelationshipIsSaved(String arg0) {
    }

    @And("the relationship is visible in:")
    public void theRelationshipIsVisibleIn(DataTable table) {
        var rows = table.asMaps(String.class, String.class);

        for (var row : rows) {
            String view = row.get("View");
            String display = row.get("Display");


        }
    }


    @And("an audit log entry records: action={string}, created_by={string}, source={string}, target={string}")
    public void anAuditLogEntryRecordsActionCreated_bySourceTarget(String arg0, String arg1, String arg2, String arg3) {
    }

    @And("an audit log records: action={string}, status={string}")
    public void anAuditLogRecordsActionStatus(String arg0, String arg1) {
    }

    @And("LEOs {string}, {string}, {string} depend on {string}")
    public void leosDependOn(String arg0, String arg1, String arg2, String arg3) {
    }

    @When("teacher {string} attempts to delete {string}")
    public void teacherAttemptsToDelete(String arg0, String arg1) {
    }

    @Then("the system shows warning dialog:")
    public void theSystemShowsWarningDialog(DataTable table) {
        Map<String, String> dialog = table.asMap(String.class, String.class);

        String title = dialog.get("Title");
        String message = dialog.get("Message");
        String details = dialog.get("Details");
        String options = dialog.get("Options");
    }

    @And("the LEO is NOT deleted unless explicitly confirmed")
    public void theLEOIsNOTDeletedUnlessExplicitlyConfirmed() {
    }
}
