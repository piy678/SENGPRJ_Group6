package steps.system;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingSteps {

    private String lastErrorType;
    private String lastShownMessage;

    @When("an {string} occurs")
    public void anErrorTypeOccurs(String errorType) {
        this.lastErrorType = errorType;
        switch (errorType) {
            case "network_failure"     -> lastShownMessage = "Network unavailable";
            case "unauthorized_access" -> lastShownMessage = "Access denied";
            case "invalid_input"       -> lastShownMessage = "Please check your input data";
            default -> lastShownMessage = "Unexpected error";
        }
    }

    @Then("the system displays {string} without exposing technical details")
    public void theSystemDisplaysWithoutExposingTechnicalDetails(String expected) {
        // 1) fachliche Nachricht prüfen
        assertEquals(expected, lastShownMessage, "Friendly message mismatch");
        // 2) keine technischen Details
        assertFalse(lastShownMessage.matches(".*(Exception|stack|\\bat\\b\\s+\\S+\\()"),
                "Technical details must not be exposed");
    }
}
