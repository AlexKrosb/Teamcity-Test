package com.example.teamcity.ui.pages.favorites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.PageElement;
import com.example.teamcity.ui.elements.ProjectElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.elements;
import static com.codeborne.selenide.Selenide.sleep;

public class ProjectsPage extends FavoritesPage {
    //куда дели в FAVORITE_PROJECTS_URL mode=builds
    private static final String FAVORITE_PROJECTS_URL = "/favorite/projects";
    ///&&
    private ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--Px"));


    public ProjectsPage open() {
        Selenide.open(FAVORITE_PROJECTS_URL);
        waitUntilFavoritePageIsLoaded();
        return this;
    }
    public List<ProjectElement> getSubprojects() {
        return generatePageElements(subprojects, ProjectElement::new);
    }

}
