package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProjectManually extends Page {

    private SelenideElement createManually = element(Selectors.byClass("createOption readyToUseOption collapsed"));
    private SelenideElement inputName = element(Selectors.byId("name"));
    private SelenideElement inputBuildTypeName = element(Selectors.byId("buildTypeName"));
    private SelenideElement createBuildConfiguration = element(Selectors.byClass("btn"));

    public CreateNewProjectManually open(String parentProjectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId="+ parentProjectId +"&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        return this;
    }
    public CreateNewProjectManually createProjectManually(String name) {
        createManually.click();
        inputName.sendKeys(name);
        submit();
        return this;
    }
    public void setupProjectManually(String buildTypeName) {
        createBuildConfiguration.click();
        inputBuildTypeName.shouldHave(Condition.visible, Duration.ofSeconds(30));
        inputBuildTypeName.sendKeys(buildTypeName);
        submit();
    }
}
