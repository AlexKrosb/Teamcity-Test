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
                {"Awertyuiopgfdsertyuytuijl"+
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl"
                }
        };
    }
    @Test(dataProvider = "buildConfigIdTestCorrectSymbols")
    public void buildConfigIdTestCorrectSymbols(String buildTypeId) {
        var TestData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(TestData.getProject());

        TestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(TestData.getUser());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(TestData.getUser()))
                .createCheckedBuildConfigWithParameter(buildTypeId, RandomData.getString(), project.getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
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
                {"Awertyuiopgfdsertyuytuijl"+
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" + "e"
                }
        };
    }
    @Test(dataProvider = "buildConfigIdTestInCorrectSymbols")
    public void buildConfigIdTestInCorrectSymbols(String buildTypeId) {
        var TestData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(TestData.getProject());

        TestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(TestData.getUser());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(TestData.getUser()))
                .createUncheckedBuildConfigWithParameter(buildTypeId, RandomData.getString(), project.getId())
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

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .createUncheckedBuildConfigWithParameter(buildConfig.getId(), RandomData.getString(), project.getId())
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
                {"Awertyuiopgfdsertyuytuijl"+
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl"

                },{"1234567890"},
                {"\"name\": \"! @ # $ % ^ & * ( ) _ + - = { } [ ] ; : ' \\\" , / > / ?\","},
                {" User123!ComplexИмя_Проекта@ c "}
        };
    }
    @Test(dataProvider = "buildConfigNameTestCorrectSymbols")
    public void buildConfigNameTestCorrectSymbols(String BuildName) {
        var TestData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(TestData.getProject());

        TestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(TestData.getUser());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(TestData.getUser()))
                .createCheckedBuildConfigWithParameter(RandomData.getString(), BuildName, project.getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
    //граничные значения и валидация name config - негативные кейсы
    @DataProvider(name = "buildConfigNameTestInCorrectSymbols")
    public Object[][] buildConfigNameTestInCorrectSymbols() {
        return new Object[][]{
                {""},
                {"    "}, // бага была 200
                {"Awertyuiopgfdsertyuytuijl"+
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" +
                        "Awertyuiopgfdsertyuytuijl" + "e"
                } //бага ограничение out of length
        };
    }
    @Test(dataProvider = "buildConfigNameTestInCorrectSymbols")
    public void buildConfigNameTestInCorrectSymbols(String BuildName) {
        var TestData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest().create(TestData.getProject());

        TestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(TestData.getUser());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(TestData.getUser()))
                .createCheckedBuildConfigWithParameter(RandomData.getString(), BuildName, project.getId())
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

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .createUncheckedBuildConfigWithParameter(RandomData.getString(), buildConfig.getName(), project.getId())
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

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData1.getUser()))
                .createCheckedBuildConfigWithParameter(RandomData.getString(), "Double", project1.getId());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData1.getUser()))
                .createCheckedBuildConfigWithParameter(RandomData.getString(), "Double", project2.getId());
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

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .createCheckedBuildConfigWithParameter(RandomData.getString(), RandomData.getString(), projectId)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);

}}
