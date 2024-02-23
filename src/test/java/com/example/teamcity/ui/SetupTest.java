package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.StartUpPage;
import com.example.teamcity.ui.pages.AgentAuthPage;
import org.testng.annotations.Test;

public class SetupTest extends BaseUiTest{
    @Test
    public void startUpTest() {
        new StartUpPage()
                .open()
                .setupTeamCityServer()
                .getHeader().shouldHave(Condition.text("Create Administrator Account"));
    }
}