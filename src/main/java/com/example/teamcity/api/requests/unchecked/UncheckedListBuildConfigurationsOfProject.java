package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedListBuildConfigurationsOfProject extends Request {
    private static final String BUILD_CONFIG_ENDPOINT = "/app/rest/projects/";

    public UncheckedListBuildConfigurationsOfProject(RequestSpecification spec) {
        super(spec);
    }

    public Response get(String projectLocator) {
        return given().spec(spec).get(BUILD_CONFIG_ENDPOINT + projectLocator + "/buildTypes");
    }
}
