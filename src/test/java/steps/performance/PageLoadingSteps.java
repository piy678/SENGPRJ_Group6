package steps.performance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PageLoadingSteps {
    @Given("a user is authenticated and has stable network connection")
    public void aUserIsAuthenticatedAndHasStableNetworkConnection() {
    }

    @When("the user navigates to the {string} page")
    public void theUserNavigatesToThePage(String arg0) {
    }

    @Then("the DOMContentLoaded event fires within {int} second")
    public void theDOMContentLoadedEventFiresWithinSecond(int arg0) {
    }

    @And("the largest contentful paint occurs within {int} seconds")
    public void theLargestContentfulPaintOccursWithinSeconds(int arg0) {
    }

    @And("the page becomes interactive within {double} seconds")
    public void thePageBecomesInteractiveWithinSeconds(int arg0, int arg1) {
    }
}
