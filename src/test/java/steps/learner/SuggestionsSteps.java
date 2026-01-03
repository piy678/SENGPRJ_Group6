package steps.learner;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuggestionsSteps {
    @Given("learner {string} is enrolled in {string}")
    public void learnerIsEnrolledIn(String arg0, String arg1) {
    }

    @And("student {string} is enrolled in course {string}")
    public void studentIsEnrolledInCourse(String arg0, String arg1) {
    }
    @Given("student {string} is authenticated with role {string}")
    public void student_is_authenticated_with_role(String studentName, String role) {
    }



    @Given("{string} has completed {string} with status {string} \\(on {int}-{int}-{int})")
    public void hasCompletedWithStatusOnInts(String student, String leo, String status,
                                             int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        // TODO store in context
    }



    @And("LEO {string} requires {string}")
    public void leoRequires(String arg0, String arg1) {
    }

    @When("Alice opens the {string} panel")
    public void aliceOpensThePanel(String arg0) {
    }

    @Then("the system suggests {string} with:")
    public void theSystemSuggestsWith(String leo, DataTable table) {

        Map<String, String> map = table.asMap(String.class, String.class);

        String rationale = map.get("Rationale");
        String ready     = map.get("Ready");
        String color     = map.get("Color");
    }


    @Given("{string} has completed {string} with status {string}")
    public void hasCompletedWithStatus(String arg0, String arg1, String arg2) {
    }

    @And("{string} has status {string} for {string}")
    public void hasStatusFor(String arg0, String arg1, String arg2) {
    }

    @Then("{string} does NOT appear in suggestions")
    public void doesNOTAppearInSuggestions(String arg0) {
    }

    @And("the system shows message:")
    public void theSystemShowsMessage(DataTable table) {
        Map<String, String> map = table.asMap(String.class, String.class);

        String text = map.get("Text");
        String tip  = map.get("Tip");

        // TODO assert/store
    }
}
