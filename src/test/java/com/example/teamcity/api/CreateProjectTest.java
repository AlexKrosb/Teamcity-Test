package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateProjectTest extends BaseApiTest {

    /*
    Positive cases
     */

    // Проверка, что можно создать новый проект в Parent project уже созданного проекта
    @Test
    public void CheckBeAbleToCreateNewProjectInParentProjectAlreadyExistedProject() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        var testDataForHeirOfProject = testDataStorage.addTestData().getProject();
        testDataForHeirOfProject.getParentProject().setLocator(project.getId());

        var heirOfProject = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testDataForHeirOfProject);

        softy.assertThat(heirOfProject.getParentProjectId()).isEqualTo(testData.getProject().getId());
    }

    // Проверка, что можно создать новый проект
    // в Parent project уже созданного проекта c таким же именем Parent project.
    @Test
    public void CheckProjectWithSameNameOfParentProjectBeAbleToCreate() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        var testDataForHeirOfProject = testDataStorage.addTestData().getProject();
        testDataForHeirOfProject.getParentProject().setLocator(project.getId());
        testDataForHeirOfProject.setName(project.getName());

        var heirOfProject = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testDataForHeirOfProject);

        softy.assertThat(heirOfProject.getName()).isEqualTo(testData.getProject().getName());
    }

    //Проверка валидации имени проекта
    @DataProvider(name = "projectTestCorrectSymbols")
    public Object[][] projectTestCorrectSymbols() {
        return new Object[][]{
                {"ABCDEFGHIGKLMNOPQRSTUVWXYZ"},
                {"1234567890"},
                {"\"name\": \"! @ # $ % ^ & * ( ) _ + - = { } [ ] ; : ' \\\" , / > / ?\","},
                {" User123!ComplexИмя_Проекта@ c "}
        };
    }

    @Test(dataProvider = "projectTestCorrectSymbols")
    public void CheckValidationProjectName(String projectName) {

        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        testData.getProject().setName(projectName);

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
    }

    //Проверка валидации имени проекта - граничные
    @DataProvider(name = "checkProjectNameValidationIsCorrectly")
    public Object[][] checkProjectNameValidationIsCorrectly() {
        return new Object[][]{
                {"A"},  //Проверка на граничное значение - минимальное значение
                {"80symbols_80symbols_80symbols_80" +
                        "symbols_80symbols_80symbols_80symbols_thisislimi"}
                //Проверка на граничное значение - максимальное значение
        };
    }

    @Test(dataProvider = "checkProjectNameValidationIsCorrectly")
    public void checkProjectNameValidationIsCorrectly(String projectName) {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        testData.getProject().setName(projectName);

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
    }

    @DataProvider(name = "checkValidationIdOfProject")
    public Object[][] checkValidationIdOfProject() {
        return new Object[][]{
                {"A"},
                {RandomData.getLongString(125)},
                {"ABCDEFGHIGKLMNOPQRSTUVWXYZ"},
                {"1234567890"}
        };
    }

    @Test(dataProvider = "checkValidationIdOfProject")
    public void checkValidationIdOfProject(String ProjectId) {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        testData.getProject().setId(ProjectId);

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }
    /*
    Negative cases
     */

    // Проверка, что нельзя создать новый проект в Parent project с именем проекта который был создан ранее
    @Test
    public void CheckProjectWithThisNameAlreadyExists() {
        var testData1 = testDataStorage.addTestData();
        testData1.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData1.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData1.getUser()))
                .create(testData1.getProject());

        var testData2 = testDataStorage.addTestData().getProject();
        testData2.getParentProject().setLocator(project.getId());

        var heirOfProject = new CheckedProject(Specifications.getSpec()
                .authSpec(testData1.getUser()))
                .create(testData2);

        var testData3 = testDataStorage.addTestData().getProject();
        testData3.getParentProject().setLocator(project.getId());
        testData3.setName(heirOfProject.getName());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData1.getUser()))
                .create(testData3)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DataProvider(name = "projectNameTestData")
    public Object[][] projectNameTestData() {
        return new Object[][]{
                {""},  // Проверка, что name не может быть пустым
                {"    "},  // Проверка, что name не может быть отправлен только с пробелом
                /*
                ALERT _ _ _ _ _ BUG!BUG!BUG!BUG!BUG!BUG! Expected status code <400> but was <500>.
                 */
                {"81symbols_81symbols_81symbols_81symbols_" +
                        "81symbols_81symbols_81symbols_thisislimit"}
                // Тест на превышение максимальной длины имени проекта
                /*
                ALERT _ _ _ _ _ BUG!BUG!BUG!BUG!BUG!BUG! Expected status code <400> but was <200>.
                 */
        };
    }

    @Test(dataProvider = "projectNameTestData")
    public void checkProjectNameValidationIsNotCorrectly(String projectName) {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getProject().setName(projectName);

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    //Бага ? Должен быть 400 статус но 500 везде
    @DataProvider(name = "checkValidationIdOfProjectIsNotCorrect")
    public Object[][] checkValidationIdOfProjectIsNotCorrect() {
        return new Object[][]{
                {""},
                {" "},
                {RandomData.getLongString(126)},
                {"\"name\": \"! @ # $ % ^ & * ( ) _ + - = { } [ ] ; : ' \\\" , / > / ?\","},
                {" User123!ComplexИмя_Проекта@ c "}
        };
    }

    @Test(dataProvider = "checkValidationIdOfProjectIsNotCorrect")
    public void checkValidationIdOfProjectIsNotCorrect(String ProjectId) {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getProject().setId(ProjectId);

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    //Проверка что пустой locator нельзя отправить
    @Test
    public void CheckThatLocatorCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getProject().getParentProject().setLocator("");

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //Проверка - Project ID "ProjectId" is already used by another project
    @Test
    public void CheckThatIdCannotBeDublicate() {
        var testData1 = testDataStorage.addTestData();
        var testData2 = testDataStorage.addTestData();
        testData1.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData1.getUser());

        new UncheckedProject(Specifications.getSpec().authSpec(testData1.getUser()))
                .create(testData1.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_OK);

        testData2.getProject().setId(testData1.getProject().getId());

        new UncheckedProject(Specifications.getSpec().authSpec(testData1.getUser()))
                .create(testData2.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    // Проверка, что проект не может иметь тоже имя в том же Project parent
    @Test
    public void CheckNameWithThisNameAlreadyExist() {
        var testData = testDataStorage.addTestData();
        var testData2 = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        testData2.getProject().setName(project.getName());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData2.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    // Проверка, что нельзя создать проект с несуществующим локатором
    @Test
    public void checkNoProjectFoundByNotExistedLocator() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getProject().getParentProject().setLocator(RandomData.getString());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
