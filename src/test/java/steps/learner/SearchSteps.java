package steps.learner;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchSteps {
    @Given("a set of LEOs exist in course {string}")
    public void aSetOfLEOsExistInCourse(String arg0) {
    }

    @When("the user searches for LEOs by <filter> {string}")
    public void theUserSearchesForLEOsByFilter(String arg0) {
    }

    @Then("only LEOs matching <filter> {string} are shown")
    public void onlyLEOsMatchingFilterAreShown(String arg0) {
    }

    @When("the user searches for LEOs by topic {string} and status {string}")
    public void theUserSearchesForLEOsByTopicAndStatus(String arg0, String arg1) {
    }

    @Then("the result list shows only matching LEOs")
    public void theResultListShowsOnlyMatchingLEOs() {
    }

    @And("the number of results is displayed")
    public void theNumberOfResultsIsDisplayed() {
    }

    @When("the user searches for {string}")
    public void theUserSearchesFor(String arg0) {
    }

    @Then("the system shows {string}")
    public void theSystemShows(String arg0) {
    }
}
