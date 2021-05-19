package b21.spartan.admin;

import io.cucumber.java.af.En;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import net.serenitybdd.rest.SerenityRest.*;

import static org.hamcrest.Matchers.hasSize;

@SerenityTest
@Disabled
public class SpartanAdminTest {

    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://18.234.107.235:7000";
        RestAssured.basePath = "/api/";

    }

    @DisplayName("Test Admin GET /spartans endpoint")
    @Test
    public void testAllSpartans() {

        SerenityRest.given()
                .auth().basic("admin","admin")
                .log().all()
        .when()
                .get("spartans")
//        .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
                ;

        Ensure.that("Successfull 200 Response", p-> p.statusCode(200));
        Ensure.that("Content type is json",
                validatableResponse -> validatableResponse.contentType(ContentType.JSON));
        Ensure.that("Response has correct size",
                validatableResponse -> validatableResponse.body("", hasSize(111)));

    }

    @DisplayName("Test public user GET /spartans endpoint")
    @Test
    public void testPublicUserGetSpartanData() {

        SerenityRest.get("spartans");
        Ensure.that("Public user should not be able to get all Spartan",
                validatableResponse -> validatableResponse.statusCode(401));
    }


    @AfterAll
    public static void cleanUp() {
        RestAssured.reset();
    }

}
