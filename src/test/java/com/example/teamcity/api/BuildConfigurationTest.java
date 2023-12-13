package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest {
    @Test
    public void buildConfigurationTest() {
        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    //граничные значения и валидация projectId
    @DataProvider(name = "buildConfigIdTestCorrectSymbols")
    public Object[][] buildConfigIdTestCorrectSymbols() {
        return new Object[][]{
                {"ABCDEFGHIGKLMNOPQRSTUVWXYZ"},
                {"Qwerty1234"},
                {"symbols81_81symbols_81symbols_81symbols_" +
                        "81symbols_81symbols_81symbols_thisislimit"},
                {RandomData.getLongString(125)}
        };
    }
    @Test(dataProvider = "buildConfigIdTestCorrectSymbols")
    public void buildConfigIdTestCorrectSymbols(String buildTypeId) {
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getBuildtype().setId(buildTypeId);
        testData.getBuildtype().getProject().setId(project.getId());

        var build = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype());

        softy.assertThat(build.getName()).isEqualTo(testData.getBuildtype().getName());
    }

    //БАг? Должна быть 400, а тут везде 500
    //projectId граничные и валидация
    @DataProvider(name = "buildConfigIdTestInCorrectSymbols")
    public Object[][] buildConfigIdTestInCorrectSymbols() {
        return new Object[][]{
                {"1234567890"},
                {"\"name\": \"! @ # $ % ^ & * ( ) _ + - = { } [ ] ; : ' \\\" , / > / ?\","},
                {" User123!ComplexИмя_Проекта@ c "},
                {""},
                {"    "},
                {RandomData.getLongString(126)}
        };
    }

    @Test(dataProvider = "buildConfigIdTestInCorrectSymbols")
    public void buildConfigIdTestIncorrectSymbols(String buildTypeId) {
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getBuildtype().setId(buildTypeId);
        testData.getBuildtype().getProject().setId(project.getId());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    //The build configuration / template ID  is already used by another configuration projectId
    @Test
    public void buildConfigIdCannotBeDoubled() {
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        uncheckedWithSuperUser.getUserRequest().create(testData.getUser());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype());

        testData.getBuildtype().setId(buildConfig.getId());
        testData.getBuildtype().getProject().setId(project.getId());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //граничные значения и валидация name config
    @DataProvider(name = "buildConfigNameTestCorrectSymbols")
    public Object[][] buildConfigNameTestCorrectSymbols() {
        return new Object[][]{
                {"ABCDEFGHIGKLMNOPQRSTUVWXYZ"},
                {"Qwerty1234"},
                {"symbols81_81symbols_81symbols_81symbols_" +
                        "81symbols_81symbols_81symbols_thisislimit"},
                {RandomData.getLongString(125)},
                {"1234567890"},
                {"\"name\": \"! @ # $ % ^ & * ( ) _ + - = { } [ ] ; : ' \\\" , / > / ?\","},
                {" User123!ComplexИмя_Проекта@ c "}
        };
    }

    @Test(dataProvider = "buildConfigNameTestCorrectSymbols")
    public void buildConfigNameTestCorrectSymbols(String BuildName) {
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getBuildtype().setName(BuildName);
        testData.getBuildtype().getProject().setId(project.getId());

        var build = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype());

        softy.assertThat(build.getName()).isEqualTo(testData.getBuildtype().getName());
    }

    //граничные значения и валидация name config - негативные кейсы
    @DataProvider(name = "buildConfigNameTestIncorrectSymbols")
    public Object[][] buildConfigNameTestIncorrectSymbols() {
        return new Object[][]{
                {""},
                {"    "}, // бага была 200
                { RandomData.getLongString(126)} //бага ограничение out of length
        };
    }

    @Test(dataProvider = "buildConfigNameTestIncorrectSymbols")
    public void buildConfigNameTestIncorrectSymbols(String BuildName) {
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getBuildtype().setName(BuildName);
        testData.getBuildtype().getProject().setId(project.getId());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //Check that Build configuration with name already exists in project
    @Test
    public void buildConfigNameTestCannotBeDoubledInSameProject() {
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype());

        testData.getBuildtype().setName(buildConfig.getName());
        testData.getBuildtype().getProject().setId(project.getId());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void buildConfigNameTestCanBeDoubledInDifferentProject() {
        var testData1 = testDataStorage.addTestData();
        var testData2 = testDataStorage.addTestData();

        var project1 = checkedWithSuperUser.getProjectRequest().create(testData1.getProject());
        var project2 = checkedWithSuperUser.getProjectRequest().create(testData2.getProject());

        testData1.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData1.getUser());
        checkedWithSuperUser.getUserRequest().create(testData2.getUser());

        testData1.getBuildtype().setName("Double");
        testData2.getBuildtype().setName("Double");

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData1.getUser()))
                .create(testData1.getBuildtype());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData2.getUser()))
                .create(testData2.getBuildtype());

        softy.assertThat(project1.getName()).isEqualTo(project2.getName());
    }

    @DataProvider(name = "f")
    public Object[][] f() {
        return new Object[][]{
                {""},
                {"Awertyuiopgfdsertyuytuijl"}
        };
    }

    //Check that No project found by empty or not existed locator
    @Test(dataProvider = "f")
    public void CheckNoProjectFoundByEmptyOrNotExistedLocator(String projectId) {
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getBuildtype().getProject().setId(projectId);

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildtype())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);

    }
}
