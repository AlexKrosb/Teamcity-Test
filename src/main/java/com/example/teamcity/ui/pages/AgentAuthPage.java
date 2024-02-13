package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

public class AgentAuthPage extends Page{
    private SelenideElement commentField = $x("//*[@data-test='ring-input']//*[contains(@id, 'ring-input')]");
    private SelenideElement acceptButton = $x("//button//*[contains(text(), 'Authorize')]");
    private SelenideElement authorizeModalButton = $x("//*[@data-test=\"ring-island-content\"]//button//*[contains(text(), 'Authorize')]");
    private SelenideElement agenNametLink = $x("//*[contains(@title, 'Agent name:')]");
    @Getter
    private SelenideElement agentAuthStatus = $x("//*[@data-agent-authorization-status=\"true\"]//span[1]");

    public AgentAuthPage open() {
        Selenide.open("/agents/unauthorized");
        return this;
    }

    public AgentAuthPage authTeamCityAgent() {
        waitUntilPageIsLoaded();
        acceptButton.click();
        waitUntilPageIsLoaded();
        commentField.shouldBe(Condition.enabled, Duration.ofSeconds(10));
        commentField.sendKeys("test");
        waitUntilPageIsLoaded();
        authorizeModalButton.click();
        waitUntilPageIsLoaded();
        agenNametLink.click();
        agentAuthStatus.shouldBe(Condition.enabled, Duration.ofSeconds(10));
        return this;
    }
}