package steps.system;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AvailabilitySteps {
    @Given("a health check endpoint is monitored every {int} seconds for {int} hours")
    public void aHealthCheckEndpointIsMonitoredEverySecondsForHours(int arg0, int arg1) {
    }

    @When("uptime is calculated")
    public void uptimeIsCalculated() {
    }

    @Then("the measured uptime is at least {int}%")
    public void theMeasuredUptimeIsAtLeast(int arg0) {
    }

    @And("no single outage lasts longer than {int} minutes")
    public void noSingleOutageLastsLongerThanMinutes(int arg0) {
    }
}
