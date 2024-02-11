package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.example.teamcity.api.requests.unchecked.UncheckedProject.PROJECT_ENDPOINT;
import static io.restassured.RestAssured.given;

public class UncheckedListProjects extends Request {
    public UncheckedListProjects(RequestSpecification spec) {
        super(spec);
    }

    public Response get() {
        return given().spec(spec).get(PROJECT_ENDPOINT);
    }
}
