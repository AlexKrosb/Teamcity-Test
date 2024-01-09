package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.Projects;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedListProjects;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedListProjects extends Request {
    public CheckedListProjects(RequestSpecification spec) {
        super(spec);
    }
    public Projects get() {
        return new UncheckedListProjects(spec)
                .get()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Projects.class);
    }
}
