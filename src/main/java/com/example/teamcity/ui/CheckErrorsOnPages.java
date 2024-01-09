package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.element;

public class CheckErrorsOnPages {

    private SelenideElement errorBuildTypeName = element(Selectors.byId("error_buildTypeName"));
    private SelenideElement errorProjectName = element(Selectors.byId("error_projectName"));
    private SelenideElement errorByUrl = element(Selectors.byId("error_url"));
    private SelenideElement errorName = element(Selectors.byId("errorName"));

    public void errorByEmptyUrl() {
        errorByUrl.shouldHave(Condition.text("URL must not be empty"));
    }

    public void repositoryNotFound(String Url) {
        errorByUrl.shouldHave(Condition.text("git -c credential.helper= ls-remote origin command failed.\n" +
                    "exit code: 128\n" +
                    "stderr: remote: Not Found\n" +
                    "fatal: repository '" + Url + "/' not found"));
    }

    public void urlIsNotRecognized() {
        errorByUrl.shouldHave(Condition.text("Cannot create a project using the specified URL" +
                ". The URL is not recognized."));
    }
    public void NameMustNotBeEmpty(){
        errorProjectName.shouldHave(Condition.text("Project name must not be empty"));
    }
    public void idMustNotBeEmpty(){
        errorBuildTypeName.shouldHave(Condition.text("Build configuration name must not be empty"));
    }

    public void projectNameIsEmptyByManually() {
        errorName.shouldHave(Condition.text("Project name is empty"));
    }

    public void buildNameIsEmptyByManually() {
        errorBuildTypeName.shouldHave(Condition.text("Name must not be empty"));
    }
}
