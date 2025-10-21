package steps.system;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ErrorHandlingSteps {
    @When("an <errorType> occurs")
    public void anErrorTypeOccurs() {
    }

    @Then("the system displays {string} without exposing technical details")
    public void theSystemDisplaysWithoutExposingTechnicalDetails(String arg0) {
    }
}
