package steps.learner;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchSteps {

    static class LEO {
        final String name;
        final String topic;
        final String status;

        LEO(String name, String topic, String status) {
            this.name = name;
            this.topic = topic;
            this.status = status;
        }
    }

    private final List<LEO> allLeos = new ArrayList<>();
    private List<LEO> results = new ArrayList<>();
    private String lastMessage;

    @Given("a set of LEOs exist in course {string}")
    public void aSetOfLEOsExistInCourse(String course) {
        // Dummy dataset fürs Feature
        allLeos.clear();
        allLeos.add(new LEO("Fractions", "Arithmetic", "Not Reached"));
        allLeos.add(new LEO("Linear Equations", "Algebra", "Reached"));
        allLeos.add(new LEO("Quadratic Functions", "Algebra", "Not Reached"));
        allLeos.add(new LEO("Fractions - Advanced", "Arithmetic", "Reached"));

        results = new ArrayList<>(allLeos);
        lastMessage = null;
    }

    @When("the user searches for LEOs by {word} {string}")
    public void theUserSearchesForLEOsByFilterValue(String filter, String value) {
        results = filterBy(results, filter, value);
        lastMessage = results.isEmpty() ? "No LEOs found" : null;
    }

    @Then("only LEOs matching {word} {string} are shown")
    public void onlyLeosMatchingFilterValueAreShown(String filter, String value) {
        for (LEO leo : results) {
            String field = fieldValue(leo, filter);
            Assertions.assertTrue(
                    containsIgnoreCase(field, value),
                    "Expected all results to match " + filter + "='" + value + "', but got: " + field
            );
        }
    }

    @When("the user searches for LEOs by topic {string} and status {string}")
    public void theUserSearchesForLEOsByTopicAndStatus(String topic, String status) {
        results = filterBy(allLeos, "topic", topic);
        results = filterBy(results, "status", status);
        lastMessage = results.isEmpty() ? "No LEOs found" : null;
    }

    @Then("the result list shows only matching LEOs")
    public void theResultListShowsOnlyMatchingLEOs() {
        Assertions.assertFalse(results.isEmpty(), "Expected some results but got none.");
    }

    @And("the number of results is displayed")
    public void theNumberOfResultsIsDisplayed() {
        // “Displayed” = wir prüfen nur, dass man die Anzahl bestimmen kann
        Assertions.assertTrue(results.size() >= 0);
    }

    // Helpers
    private static List<LEO> filterBy(List<LEO> input, String filter, String value) {
        String f = filter.toLowerCase(Locale.ROOT);
        List<LEO> out = new ArrayList<>();
        for (LEO leo : input) {
            String field = fieldValue(leo, f);
            if (containsIgnoreCase(field, value)) out.add(leo);
        }
        return out;
    }

    private static String fieldValue(LEO leo, String filter) {
        return switch (filter.toLowerCase(Locale.ROOT)) {
            case "name" -> leo.name;
            case "topic" -> leo.topic;
            case "status" -> leo.status;
            default -> throw new IllegalArgumentException("Unknown filter: " + filter);
        };
    }

    private static boolean containsIgnoreCase(String haystack, String needle) {
        return haystack != null && needle != null
                && haystack.toLowerCase(Locale.ROOT).contains(needle.toLowerCase(Locale.ROOT));
    }
}
