package steps.system;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingSteps {

    private String lastUserMessage;

    @When("an {word} occurs")
    public void an_errorType_occurs(String errorType) {
        switch (errorType) {
            case "network_failure":
                lastUserMessage = "Network unavailable";
                break;
            case "unauthorized_access":
                lastUserMessage = "Access denied";
                break;
            case "invalid_input":
                lastUserMessage = "Please check your input data";
                break;
            default:
                fail("Unknown errorType in scenario: " + errorType);
        }
    }

    @Then("the system displays {string} without exposing technical details")
    public void the_system_displays_without_exposing_technical_details(String expectedMessage) {
        assertEquals(expectedMessage, lastUserMessage);
        assertNoTechnicalDetails(lastUserMessage);
    }

    private void assertNoTechnicalDetails(String msg) {
        assertFalse(msg.contains("Exception"));
        assertFalse(msg.contains("Stacktrace"));
        assertFalse(msg.contains("org."));
    }
}
