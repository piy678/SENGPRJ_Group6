package steps.learner;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SuggestionsSteps {
    @Given("learner {string} is enrolled in {string}")
    public void learnerIsEnrolledIn(String arg0, String arg1) {
    }

    @Given("learner {string} has mastered {string}")
    public void learnerHasMastered(String arg0, String arg1) {
    }

    @And("{string} requires {string}")
    public void requires(String arg0, String arg1) {
    }

    @When("the learner opens the {string} panel")
    public void theLearnerOpensThePanel(String arg0) {
    }

    @Then("the system suggests {string} with rationale {string}")
    public void theSystemSuggestsWithRationale(String arg0, String arg1) {
    }

    @And("{string} is not yet reached for {string}")
    public void isNotYetReachedFor(String arg0, String arg1) {
    }

    @Then("{string} is not suggested")
    public void isNotSuggested(String arg0) {
    }

    @Given("learner {string} has already mastered {string}")
    public void learnerHasAlreadyMastered(String arg0, String arg1) {
    }

    @When("the learner opens {string}")
    public void theLearnerOpens(String arg0) {
    }

    @Then("{string} is not included in suggestions")
    public void isNotIncludedInSuggestions(String arg0) {
    }
}
