package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
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

        var project = checkedWithSuperUser.getProjectRequest().get(testData.getProject().getId());
        var build = checkedWithSuperUser.getBuildConfigRequst().get(testData.getProject().getParentProject().getLocator());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
        softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
        softy.assertThat(build.getName()).isEqualTo(testData.getBuildtype().getName());
        softy.assertThat(build.getId()).isEqualTo(testData.getBuildtype().getId());
    }
    @Test
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithIncorrectRepositoryUrl() {
        var testData = testDataStorage.addTestData();
        var incorrectUrl = "https://github.com/" + RandomData.getString();
        var correctUrl = "https://github.com/AlexKrosb/hexlet-assignments";


        loginAsUser(testData.getUser());
        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .notCreateProjectByIncorrectUrl("")
                .notCreateProjectByIncorrectUrl(testData.getProject().getName())
                .notCreateProjectByIncorrectUrl(incorrectUrl)
                .createProjectByUrl(correctUrl)
                .setupIncorrectProject("", "")
                .setupProject(testData.getProject().getName(), testData.getBuildtype().getName());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createIncorrectProjectManually("");

    }
}
