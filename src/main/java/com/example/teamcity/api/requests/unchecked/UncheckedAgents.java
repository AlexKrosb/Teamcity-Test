package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedAgents extends Request {

    private static final String AGENTS_ENDPOINT = "/app/rest/agents";

    public UncheckedAgents(RequestSpecification spec) {
        super(spec);
    }

    public Response get() {
        return given()
                .spec(spec)
                .get(AGENTS_ENDPOINT +"?locator=authorized:any");
    }

    public Response update(String id) {
        String body = "true";
        return given()
                .spec(spec)
                .body(body)
                .contentType("text/plain")
                .accept("text/plain")
                .put(AGENTS_ENDPOINT+"/id:"+id+"/authorized");
    }
}
