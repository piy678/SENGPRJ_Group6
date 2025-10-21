package steps.authoring;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LeoCreationSteps {
    @Given("a teacher is on the {string} authoring page")
    public void aTeacherIsOnTheAuthoringPage(String arg0) {
    }

    @When("the teacher creates a LEO named {string}")
    public void theTeacherCreatesALEONamed(String arg0) {
    }

    @And("the teacher sets {string} as a prerequisite of {string}")
    public void theTeacherSetsAsAPrerequisiteOf(String arg0, String arg1) {
    }

    @Then("both LEOs should be saved")
    public void bothLEOsShouldBeSaved() {
    }

    @And("the relationship {string} should be visible in the LEO graph")
    public void theRelationshipShouldBeVisibleInTheLEOGraph(String arg0) {
    }

    @Given("a teacher is authenticated with role {string}")
    public void aTeacherIsAuthenticatedWithRole(String arg0) {
    }

    @And("a course {string} exists")
    public void aCourseExists(String arg0) {
    }

    @When("the teacher creates a LEO named {string} with topic {string}")
    public void theTeacherCreatesALEONamedWithTopic(String arg0, String arg1) {
    }

    @Then("the system saves the new LEO in the course")
    public void theSystemSavesTheNewLEOInTheCourse() {
    }

    @And("the LEO {string} appears in the authoring list")
    public void theLEOAppearsInTheAuthoringList(String arg0) {
    }

    @Given("{string} and {string} exist")
    public void andExist(String arg0, String arg1) {
    }

    @Then("the relationship is stored in the dependency graph")
    public void theRelationshipIsStoredInTheDependencyGraph() {
    }

    @Given("a LEO named {string} already exists")
    public void aLEONamedAlreadyExists(String arg0) {
    }

    @When("the teacher attempts to create another LEO named {string}")
    public void theTeacherAttemptsToCreateAnotherLEONamed(String arg0) {
    }

    @Then("the system shows error {string}")
    public void theSystemShowsError(String arg0) {
    }

    @Given("{string} is a prerequisite of {string}")
    public void isAPrerequisiteOf(String arg0, String arg1) {
    }

    @When("the teacher attempts to delete {string}")
    public void theTeacherAttemptsToDelete(String arg0) {
    }

    @Then("the system displays a confirmation warning listing dependent LEOs")
    public void theSystemDisplaysAConfirmationWarningListingDependentLEOs() {
    }
}
