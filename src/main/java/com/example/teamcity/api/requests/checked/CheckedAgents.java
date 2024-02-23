package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.Agents;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedAgents;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;


public class CheckedAgents extends Request {
    public CheckedAgents(RequestSpecification spec) {
        super(spec);
    }
    public Agents get() {
        return new UncheckedAgents(spec).
                get().then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Agents.class);
    }

    public String update(String id) {
        return new UncheckedAgents(spec).update(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().body().asString();
    }

}

