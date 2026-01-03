package steps.assessment;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

public class DependenciesSteps {
    @Given("LEO {string} exists")
    public void leoExists(String arg0) {
    }

    @Given("LEO {string} exists in course")
    public void leoExistsInCourse(String arg0) {
    }

    @And("LEO {string} depends on {string}")
    public void leoDependsOn(String arg0, String arg1) {
    }

    @When("teacher {string} records assessment {string} for {string} for {string}")
    public void teacherRecordsAssessmentForFor(String arg0, String arg1, String arg2, String arg3) {
    }

    @Then("{string} is marked {string} \\(unlocked) for {string}")
    public void isMarkedUnlockedFor(String arg0, String arg1, String arg2) {
    }

    @And("a suggestion for {string} appears in {string} recommendations")
    public void aSuggestionForAppearsInRecommendations(String arg0, String arg1) {
    }

    @And("the rationale shows {string}")
    public void theRationaleShows(String arg0) {
    }

    @And("an audit log entry records this prerequisite satisfaction")
    public void anAuditLogEntryRecordsThisPrerequisiteSatisfaction() {
    }

    @And("LEO {string} requires both:")
    public void leoRequiresBoth(String leo, DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        List<String> prerequisites = rows.stream()
                .map(r -> r.get("Prerequisite"))
                .toList();

        // TODO store
    }

    @And("student {string} has status {string} for {string}")
    public void studentHasStatusFor(String arg0, String arg1, String arg2) {
    }
}