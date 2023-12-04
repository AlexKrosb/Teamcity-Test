package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedProject extends Request implements CrudInterface {
    public static final String PROJECT_ENDPOINT = "/app/rest/projects";

    public UncheckedProject(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .post(PROJECT_ENDPOINT);
    }

    @Override
    public Response get(String id) {
        return given().spec(spec).get(PROJECT_ENDPOINT + "/id:" + id);
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given()
                .spec(spec)
                .delete(PROJECT_ENDPOINT + "/id:" + id);
    }
    public Response createUncheckedProjectWithParameter(String name, String locator, String id ) {
        var newProjectDescription = NewProjectDescription.builder()
                .parentProject(Project.builder().locator(locator).build())
                .name(name)
                .id(id)
                .copyAllAssociatedSettings(true)
                .build();

        return given()
                .spec(spec)
                .body(newProjectDescription)
                .post(PROJECT_ENDPOINT);
    }
}
