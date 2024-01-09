package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedListBuildConfigurationOfProject;
import com.example.teamcity.api.requests.checked.CheckedListProjects;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.admin.CreateNewProjectManually;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateNewProjectTest extends BaseUiTest{

    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/AlexKrosb/hexlet-assignments";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildtype().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

        var projectList = new CheckedListProjects(Specifications.getSpec().superUserSpec())
                .get().getProject();

        var foundProjectList = projectList.stream()
                .filter(project -> project.getName().equals(testData.getProject().getName()))
                .findFirst();

        Project project = foundProjectList.orElseGet(() -> null);

        var build = new CheckedListBuildConfigurationOfProject(Specifications.getSpec().superUserSpec())
                .get(project.getName());

        var foundBuildConfig = build.getBuildType().stream()
                .findFirst();

        BuildType buildConfig = foundBuildConfig.orElseGet(() -> null);

        softy.assertThat(project.getParentProjectId()).isEqualTo(testData.getProject().getParentProject().getLocator());
        softy.assertThat(project.getName()).isEqualTo((testData.getProject().getName()));
        softy.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildtype().getName());
    }

    @Test
    public void authorizedUserShouldBeAbleCreateNewProjectManually() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProjectManually()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject().getName())
                .setupProjectManually(testData.getBuildtype().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

        var projectList = new CheckedListProjects(Specifications.getSpec().superUserSpec())
                .get().getProject();

        var foundProjectList = projectList.stream()
                .filter(project -> project.getName().equals(testData.getProject().getName()))
                .findFirst();

        Project project = foundProjectList.orElseGet(() -> null);

        var build = new CheckedListBuildConfigurationOfProject(Specifications.getSpec().superUserSpec())
                .get(project.getName());

        var foundBuildConfig = build.getBuildType().stream()
                .findFirst();

        BuildType buildConfig = foundBuildConfig.orElseGet(() -> null);

        softy.assertThat(project.getParentProjectId()).isEqualTo(testData.getProject().getParentProject().getLocator());
        softy.assertThat(project.getName()).isEqualTo((testData.getProject().getName()));
        softy.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildtype().getName());
    }

    @DataProvider(name = "IncorrectRepositoryUrl")
    public Object[][] IncorrectRepositoryUrl() {
        return new Object[][]{
                {"https://github.com/" + RandomData.getString()},
                {""},
                {RandomData.getString()}
        };
    }

    @Test(dataProvider = "IncorrectRepositoryUrl")
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithIncorrectRepositoryUrl(String Url) {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(Url);

        if (Url.isEmpty()) {
            new CheckErrorsOnPages().errorByEmptyUrl();
        } else if(Url.contains("github.com")) {
            new CheckErrorsOnPages().repositoryNotFound(Url);
        } else{
           new CheckErrorsOnPages().urlIsNotRecognized();
        }
    }

    @DataProvider(name = "IncorrectRepositoryNameAndBuildTypeName")
    public Object[][] IncorrectRepositoryNameAndBuildTypeName() {
        return new Object[][]{
                {"",""},
                {RandomData.getString(), ""},
                {"", RandomData.getString()},
                {"      ","        "}
        };
    }
    @Test(dataProvider = "IncorrectRepositoryNameAndBuildTypeName")
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithIncorrectName(String projectName, String buildTypeName) {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/AlexKrosb/hexlet-assignments";


        loginAsUser(testData.getUser());
        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(projectName, buildTypeName);

        if (projectName.isEmpty() && buildTypeName.isEmpty()) {
            new CheckErrorsOnPages().NameMustNotBeEmpty();
            new CheckErrorsOnPages().idMustNotBeEmpty();
        } else if (projectName.isEmpty()) {
            new CheckErrorsOnPages().NameMustNotBeEmpty();
        } else if(buildTypeName.isEmpty()){
            new CheckErrorsOnPages().idMustNotBeEmpty();
        }
    }

    @DataProvider(name = "IncorrectRepositoryNameManually")
    public Object[][] IncorrectRepositoryNameManually() {
        return new Object[][]{
                {""},
                {"      "}
        };
    }
    @Test(dataProvider = "IncorrectRepositoryNameManually")
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithIncorrectNameManually(String projectName) {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());
        new CreateNewProjectManually()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(projectName);

        if (projectName.isEmpty()) {
            new CheckErrorsOnPages().projectNameIsEmptyByManually();
        }
    }
    @DataProvider(name = "IncorrectBuildTypeNameManually")
    public Object[][] IncorrectBuildTypeNameManually() {
        return new Object[][]{
                {""},
                {"      "}
        };
    }

    @Test(dataProvider = "IncorrectBuildTypeNameManually")
    public void authorizedUserShouldNotBeAbleCreateNewBuildWithIncorrectNameManually(String buildTypeName) {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());
        new CreateNewProjectManually()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject().getName())
                .setupProjectManually(buildTypeName);

        if (buildTypeName.isEmpty()) {
            new CheckErrorsOnPages().buildNameIsEmptyByManually();
        }
    }
}
