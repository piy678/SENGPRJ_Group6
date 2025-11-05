package steps.system;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SecurityStorageSteps {
    @Given("the application is connected to the production database")
    public void theApplicationIsConnectedToTheProductionDatabase() {
    }

    @Then("all connections are encrypted via TLS")
    public void allConnectionsAreEncryptedViaTLS() {
    }

    @Given("the database contains student personal data")
    public void theDatabaseContainsStudentPersonalData() {
    }

    @Then("the fields {string} and {string} are stored encrypted")
    public void theFieldsAndAreStoredEncrypted(String arg0, String arg1) {
    }
}
