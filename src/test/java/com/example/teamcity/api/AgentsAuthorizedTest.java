package com.example.teamcity.api;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Agents;
import com.example.teamcity.ui.pages.AgentAuthPage;
import org.testng.annotations.Test;

public class AgentsAuthorizedTest extends BaseApiTest{
    @Test
    public void authorizeAgentTest() {
        Agents agentsList = checkedWithSuperUser.getAgentsRequest().get();

        try {
            checkedWithSuperUser.getAgentsRequest().update(agentsList.getAgent().get(0).getId());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Teamcity Agent has not been authorized");
        }

}
}