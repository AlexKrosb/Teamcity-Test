package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProject extends Page {

    private SelenideElement urlInput = element(Selectors.byId("url"));
    private SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));
    private SelenideElement errorBuildTypeName = element(Selectors.byId("error_buildTypeName"));
    private SelenideElement errorProjectName = element(Selectors.byId("error_projectName"));
    private SelenideElement errorByUrl = element(Selectors.byId("error_url"));
    private SelenideElement createManually = element(Selectors.byClass("createOption readyToUseOption expanded"));
    private SelenideElement errorName = element(Selectors.byId("errorName"));
    private SelenideElement inputName = element(Selectors.byId("name"));
    public CreateNewProject open(String parentProjectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId="+ parentProjectId +"&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        return this;
    }
    public CreateNewProject createProjectByUrl(String url) {
        urlInput.clear();
        urlInput.sendKeys(url);
        submit();
        waitUntilDataIsSaved();
        return this;
    }

    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }
    public CreateNewProject setupIncorrectProject(String projectName, String buildTypeName) {
        setupProject(projectName, buildTypeName);
        errorBuildTypeName.shouldHave(Condition.text("Name must not be empty"));
        errorProjectName.shouldHave(Condition.text("The ID field must not be empty."));
        submit();
        return this;
    }
    public CreateNewProject notCreateProjectByIncorrectUrl(String url) {
        createProjectByUrl(url);
        if (url.isEmpty()) {
            errorByUrl.shouldHave(Condition.text("URL must not be empty"));
        } else if(url.contains("github.com")) {
            errorByUrl.shouldHave(Condition.text("git -c credential.helper= ls-remote origin command failed.\n" +
                    "exit code: 128\n" +
                    "stderr: remote: Not Found\n" +
                    "fatal: repository '" + url + "/' not found"));
        }
        else {
            errorByUrl.shouldHave(Condition.text("Cannot create a project using the specified URL" +
                    ". The URL is not recognized."));
        }
        return this;
    }
    public CreateNewProject createIProjectManually(String name) {
        createManually.click();
        inputName.sendKeys(name);
        submit();
        waitUntilDataIsSaved();
        return this;
    }
    public CreateNewProject createIncorrectProjectManually(String name) {
        createIProjectManually(name);
        errorName.shouldHave(Condition.text("Project name is empty"));
        return this;
    }
}
