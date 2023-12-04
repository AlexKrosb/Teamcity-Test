package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import static com.example.teamcity.api.requests.unchecked.UncheckedProject.PROJECT_ENDPOINT;

import static io.restassured.RestAssured.given;

public class CheckedProject extends Request implements CrudInterface {

    public CheckedProject(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Project create(Object obj) {
        return new UncheckedProject(spec).create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public Project get(String id) {
        return new UncheckedProject(spec)
                .get(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public String delete(String id) {
        return new UncheckedProject(spec)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
    public Project createCheckedProjectWithParameter(String name, String locator, String id ) {
        var newProjectDescription = NewProjectDescription.builder()
                .parentProject(Project.builder().locator(locator).build())
                .name(name)
                .id(id)
                .copyAllAssociatedSettings(true)
                .build();

        return given()
                .spec(spec)
                .body(newProjectDescription)
                .post(PROJECT_ENDPOINT)
                .then()
                .extract().as(Project.class);
    }
}
