package steps.learner;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MasteryViewSteps {
    @Given("learner {string} is authenticated")
    public void learnerIsAuthenticated(String arg0) {
    }

    @And("course {string} exists")
    public void courseExists(String arg0) {
    }

    @Given("LEOs {string} and {string} exist with statuses for {string}")
    public void leosAndExistWithStatusesFor(String arg0, String arg1, String arg2) {
    }

    @When("the learner opens the mastery overview")
    public void theLearnerOpensTheMasteryOverview() {
    }

    @Then("the list shows each LEO with current mastery status and last-updated date")
    public void theListShowsEachLEOWithCurrentMasteryStatusAndLastUpdatedDate() {
    }

    @And("only data for learner {string} is visible")
    public void onlyDataForLearnerIsVisible(String arg0) {
    }
}
