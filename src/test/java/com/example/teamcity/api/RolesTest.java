package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class RolesTest extends BaseApiTest {
    //Неавторизованный юзер не имеет прав создать проект
    @Test
    public void unauthorizedUserShouldNotHaveRightToCreateProject() {
        var testData = testDataStorage.addTestData();

        new UncheckedRequests(Specifications.getSpec().unauthSpec()).getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator"
                        + " 'count:1,id:" + testData.getProject().getId() + "'"));
    }

    //System admin имеет права создать проект
    @Test
    public void systemAdminShouldHaveRightsToCreateProject() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    //Project admin имеет права создать проект и билдконфиг
    @Test
    public void projectAdminShouldHaveRightsToCreateBuildConfigToHisProject() {
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype());

        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildtype().getId());
    }

    //Project admin не имеет права создать билдконфиг в другом проекте.
    /*
    ALERT _ _ _ _ _ BUG!BUG!BUG!BUG!BUG!BUG! Expected status code <400> but was <200>.
     */
    @Test
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProject() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());

        secondTestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(secondTestData.getUser());
        //Bug - expected result - Bad Result
        new UncheckedBuildConfig(Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildtype())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //Project developer не имеет прав создать проект при любом скоуп.
    @Test
    public void projectDeveloperShouldNotHaveRightsToCreateProject() {
        var testData1 = testDataStorage.addTestData();
        var testData2 = testDataStorage.addTestData();

        testData1.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_DEVELOPER, "p:" + testData1.getProject().getId()));
        uncheckedWithSuperUser.getUserRequest().create(testData1.getUser());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData1.getUser()))
                .create(testData1.getProject()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);

        testData2.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_DEVELOPER, "g"));
        uncheckedWithSuperUser.getUserRequest().create(testData2.getUser());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData2.getUser()))
                .create(testData2.getProject()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    //Project viewer не имеет прав создать проект при любом скоуп.
    @Test
    public void projectViewerShouldNotHaveRightsToCreateProject() {
        var testData1 = testDataStorage.addTestData();
        var testData2 = testDataStorage.addTestData();

        testData1.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_VIEWER, "p:" + testData1.getProject().getId()));
        uncheckedWithSuperUser.getUserRequest().create(testData1.getUser());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData1.getUser()))
                .create(testData1.getProject()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);

        testData2.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_VIEWER, "g"));
        uncheckedWithSuperUser.getUserRequest().create(testData2.getUser());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData2.getUser()))
                .create(testData2.getProject()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    //Agent manager не имеет прав создать проект при любом скоуп.
    /*
    ALERT _ _ _ _ _ BUG!BUG!BUG!BUG!BUG!BUG! Expected status code <403> but was <200>.
     */
    @Test
    public void agentManagerShouldNotHaveRightsToCreateProject() {
        var testData1 = testDataStorage.addTestData();
        var testData2 = testDataStorage.addTestData();

        testData1.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.AGENT_MANAGER, "p:" + testData1.getProject().getId()));
        uncheckedWithSuperUser.getUserRequest().create(testData1.getUser());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData1.getUser()))
                .create(testData1.getProject()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);

        testData2.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.AGENT_MANAGER, "g"));
        uncheckedWithSuperUser.getUserRequest().create(testData2.getUser());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData2.getUser()))
                .create(testData2.getProject()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }
}
