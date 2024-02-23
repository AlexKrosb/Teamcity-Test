package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

public class AgentAuthPage extends Page{
    @Getter
    private SelenideElement agentAuthStatus = $x("//*[@data-agent-authorization-status=\"true\"]//span[1]");

    public AgentAuthPage open() {
        Selenide.open("/agent/1#all-pools");
        return this;
    }
}