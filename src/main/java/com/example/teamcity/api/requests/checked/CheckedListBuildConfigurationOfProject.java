package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.BuildTypes;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedListBuildConfigurationsOfProject;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedListBuildConfigurationOfProject extends Request {
    public CheckedListBuildConfigurationOfProject(RequestSpecification spec) {
        super(spec);
    }
    public BuildTypes get(String locator) {
        return new UncheckedListBuildConfigurationsOfProject(spec).get(locator)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(BuildTypes.class);
    }
}
