package b21.gitHub;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

@SerenityTest
@Disabled
public class GithubTest {


    @BeforeAll
    public static void init() {
        baseURI = "https://api.github.com";
    }

    @Test
    public void testGitHubUser() {

        SerenityRest.given()
                .pathParam("user_id","CybertekSchool")
                .log().all()
        .when()
                .get("/users/{user_id}")
        .then()
                .log().all()
                .statusCode(200)
                ;
    }

    @Test
    public void testGitHubUser2() {

        SerenityRest.given()
                .pathParam("user_id","CybertekSchool")
                .log().all()
        .when()
                .get("/users/{user_id}");
        System.out.println(SerenityRest.lastResponse().statusCode());
        System.out.println(SerenityRest.lastResponse().header("Date"));
        String loginFieldValue = SerenityRest.lastResponse().path("login");
        System.out.println("loginFieldValue = " + loginFieldValue);
        System.out.println(SerenityRest.lastResponse().jsonPath().getInt("id"));
    }

    @Test
    public void testGitHubUser3() {

        SerenityRest.given()
                .pathParam("user_id","CybertekSchool")
                .log().all()
        .when()
                .get("/users/{user_id}");

        Ensure.that("request ran successfully",
                validatableResponse -> validatableResponse.statusCode(200));

        Ensure.that("login field value is CybertekSchool",
                validatableResponse -> validatableResponse.body("login", is("CybertekSchool") ));

        Ensure.that("name field value is 'Cybertek School'",
                validatableResponse -> validatableResponse.body("name",is("Cybertek School")))
                ;
    }




    @AfterAll
    public static void cleanUp() {
        RestAssured.reset();
    }

}
