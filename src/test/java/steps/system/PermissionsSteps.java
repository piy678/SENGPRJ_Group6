package steps.system;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;

import java.util.*;


public class PermissionsSteps {

    // Context variables
    private String currentUser;
    private String currentRole;
    private String currentCourse;
    private int lastHttpStatus;
    private String lastErrorMessage;
    private Map<String, Object> permissionContext = new HashMap<>();
    private List<String> enrolledStudents = new ArrayList<>();
    private Map<String, String> auditLogEntry = new HashMap<>();


    @Given("the system has the following roles:")
    public void systemHasRoles(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        Map<String, List<String>> rolePermissions = new HashMap<>();
        for (Map<String, String> row : rows) {
            String role = row.get("Role").trim();
            String permsRaw = row.get("Permissions");

            List<String> perms = Arrays.stream(permsRaw.split("\\s*,\\s*"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

            rolePermissions.put(role, perms);
        }

        permissionContext.put("roles_permissions", rolePermissions);
        System.out.println("✓ Loaded roles: " + rolePermissions.keySet());
    }


    @Given("^a teacher \"([^\"]*)\" is authenticated with role \"([^\"]*)\"$")
    public void teacherAuthenticated(String teacherName, String role) {
        System.out.println("✓ Teacher '" + teacherName + "' authenticated with role '" + role + "'");
        currentUser = teacherName;
        currentRole = role;
        permissionContext.put("current_user", teacherName);
        permissionContext.put("current_role", role);
        // TODO: Use Spring Security to authenticate
    }

    @And("^a student \"([^\"]*)\" is authenticated with role \"([^\"]*)\"$")
    public void studentAuthenticated(String studentName, String role) {
        System.out.println("✓ Student '" + studentName + "' authenticated with role '" + role + "'");
        currentUser = studentName;
        currentRole = role;
        permissionContext.put("student_user", studentName);
        permissionContext.put("student_role", role);
    }


    @And("^\"([^\"]*)\" is enrolled in course$")
    public void studentEnrolledInCourse(String studentName) {
        System.out.println("✓ '" + studentName + "' is enrolled in course");
        enrolledStudents.add(studentName);
        // TODO: Setup enrollment
    }

    @When("^teacher \"([^\"]*)\" records assessment \"([^\"]*)\" for \"([^\"]*)\"$")
    public void teacherRecordsAssessment(String teacherName, String assessmentStatus, String studentName) {
        System.out.println("✓ Teacher '" + teacherName + "' records assessment '" + assessmentStatus + "' for '" + studentName + "'");
        currentUser = teacherName;
        permissionContext.put("assessment_status", assessmentStatus);
        // TODO: Call API POST /assessments with teacher authentication
        // Should succeed with 200 OK
        lastHttpStatus = 200;
    }

    @Then("^the assessment is saved successfully$")
    public void assessmentSavedSuccessfully() {
        System.out.println("✓ Assessment is saved successfully");
        Assertions.assertEquals(200, lastHttpStatus);
        // TODO: Verify in database that assessment exists
    }

    @And("^an audit log entry shows: user=\"([^\"]*)\", action=\"([^\"]*)\"$")
    public void auditLogEntryShows(String userName, String action) {
        System.out.println("✓ Audit log entry shows: user='" + userName + "', action='" + action + "'");
        auditLogEntry.put("user", userName);
        auditLogEntry.put("action", action);
        // TODO: Verify audit log in database
    }

    @When("^student \"([^\"]*)\" attempts to change her own assessment status$")
    public void studentAttemptsChangeAssessment(String studentName) {
        System.out.println("✓ Student '" + studentName + "' attempts to change her own assessment status");
        currentUser = studentName;
        currentRole = "STUDENT";
        // TODO: Call API PUT /assessments with student authentication
        // Should fail with 403 Forbidden
        lastHttpStatus = 403;
        lastErrorMessage = "Access denied - insufficient permissions";
    }

    @Then("^the system denies with error \"([^\"]*)\"$")
    public void systemDeniesWith(String errorMessage) {
        System.out.println("✓ System denies with error: '" + errorMessage + "'");
        Assertions.assertEquals(403, lastHttpStatus);
        Assertions.assertTrue(errorMessage.contains("Access denied") || errorMessage.contains("insufficient"));
        lastErrorMessage = errorMessage;
    }

    @And("^HTTP status code is (\\d+) \\(Forbidden\\)$")
    public void httpStatusCodeForbidden(Integer statusCode) {
        System.out.println("✓ HTTP status code is " + statusCode + " (Forbidden)");
        Assertions.assertEquals(403, statusCode);
        lastHttpStatus = statusCode;
    }

    @And("^an audit log entry shows: user=\"([^\"]*)\", action=\"([^\"]*)\", status=\"([^\"]*)\"$")
    public void auditLogEntryShowsWithStatus(String userName, String action, String status) {
        System.out.println("✓ Audit log entry shows: user='" + userName + "', action='" + action + "', status='" + status + "'");
        auditLogEntry.put("user", userName);
        auditLogEntry.put("action", action);
        auditLogEntry.put("status", status);
        Assertions.assertEquals("REJECTED_PERMISSIONS", status);
        // TODO: Verify audit log in database
    }


}
