package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedBuildConfig extends Request implements CrudInterface {
    public static final String BUILD_CONFIG_ENDPOINT = "/app/rest/buildTypes";

    public UncheckedBuildConfig(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return given().spec(spec).body(obj).post(BUILD_CONFIG_ENDPOINT);
    }

    @Override
    public Response get(String locator) {
        return given().spec(spec).body(locator).post(BUILD_CONFIG_ENDPOINT);
    }

    @Override
    public Object update(String obj) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given().spec(spec).delete(BUILD_CONFIG_ENDPOINT + "/id:" + id);
    }
}
