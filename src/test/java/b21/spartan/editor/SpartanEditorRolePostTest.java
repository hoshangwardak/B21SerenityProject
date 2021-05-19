package b21.spartan.editor;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import config_util.ConfigReader;
import spartan_util.SpartanUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.Matchers.*;

@SerenityTest
@Disabled
public class SpartanEditorRolePostTest {

    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://18.234.107.235:7000";
        RestAssured.basePath = "/api/";

    }

    @AfterAll
    public static void cleanUp() {
        RestAssured.reset();
    }

    @DisplayName("Editor Should be able to Post Valid Data")
    @Test
    public void testEditorPostData() {

        Map<String,Object> bodyMap = SpartanUtil.getRandomSpartanMap();

        SerenityRest
                .given()
                .auth().basic("editor","editor")
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap)
        .when()
                .post("/spartans")
                .prettyPeek()
        ;

        // Do all assertions here
        Ensure.that("It ran successfully", thenSection-> thenSection.statusCode( equalTo(201)  ) );
        Ensure.that("Response format is correct" , thenSection -> thenSection.contentType(ContentType.JSON) );

        Ensure.that("success message is correct" , v -> v.body("success", is("A Spartan is Born!")   )       ) ;
        Ensure.that("ID is generated and not null" , v-> v.body("data.id" , notNullValue()      ) ) ;
        // checking actual data
        Ensure.that("name is correct" ,
                v-> v.body("data.name" ,  is(bodyMap.get("name")) )
        ) ;
        Ensure.that("gender is correct" ,
                v-> v.body("data.gender" ,  is(bodyMap.get("gender")) )
        ) ;
        Ensure.that("phone is correct" ,
                v-> v.body("data.phone" ,  is(bodyMap.get("phone")) )
        ) ;
        // check Location header end with newly generated id
        String newId = lastResponse().path("data.id").toString() ;
        System.out.println(   lastResponse().header("Location")   );

        Ensure.that("location header end with "+ newId ,
                v-> v.header("Location" , endsWith(newId)  )
        )  ;


    }

    // TODO : Add Parameterized Test for Positive Valid Data

    @ParameterizedTest
    @CsvSource({
            "Ercan Civi, Male, 2345678911",
            "Mohammad, Male, 3214567822",
            "Inci, Female, 43215678233"
    })
    @Test
    public void testPostValidDataWithCSVSource(String name, String gender, long phone) {

//        System.out.println("name = " + name);
//        System.out.println("gender = " + gender);
//        System.out.println("phone = " + phone);

        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name",name);
        bodyMap.put("gender",gender);
        bodyMap.put("phone",phone);

        System.out.println("body = " + bodyMap);

        SerenityRest
                .given()
                .auth().basic("editor","editor")
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .post("/spartans")
                .prettyPeek()
        ;

        // Do all assertions here
        Ensure.that("It ran successfully", thenSection-> thenSection.statusCode( equalTo(201)  ) );
        Ensure.that("Response format is correct" , thenSection -> thenSection.contentType(ContentType.JSON) );

        Ensure.that("success message is correct" , v -> v.body("success", is("A Spartan is Born!")   )       ) ;
        Ensure.that("ID is generated and not null" , v-> v.body("data.id" , notNullValue()      ) ) ;
        // checking actual data
        Ensure.that("name is correct" ,
                v-> v.body("data.name" ,  is(bodyMap.get("name")) )
        ) ;
        Ensure.that("gender is correct" ,
                v-> v.body("data.gender" ,  is(bodyMap.get("gender")) )
        ) ;
        Ensure.that("phone is correct" ,
                v-> v.body("data.phone" ,  is(bodyMap.get("phone")) )
        ) ;
        // check Location header end with newly generated id
        String newId = lastResponse().path("data.id").toString() ;
        System.out.println(   lastResponse().header("Location")   );

        Ensure.that("location header end with "+ newId ,
                v-> v.header("Location" , endsWith(newId)  )
        )  ;

    }



    // TODO : Add Parameterized Test for negative invalid Data
    @ParameterizedTest
    @CsvSource({
            "E , male , 713302 , 3",
            "Muhammad , Male , 123 , 1",
            "Inci is from Batch 21, female, 7038142311 , 2"
    })
    public void testInvalidData(String nameArg, String genderArg, long phone , int expectedErrorCount) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name", nameArg);
        bodyMap.put("gender", genderArg);
        bodyMap.put("phone", phone);
        System.out.println("bodyMap = " + bodyMap);
        SerenityRest.given()
                .auth().basic("editor", "editor")
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap).
                when()
                .post("/spartans").prettyPeek();

        Ensure.that("Expected 400 status code ",
                v-> v.statusCode(400)  );
        Ensure.that("Expected error count is " +expectedErrorCount,
                validatableResponse -> validatableResponse.body("errors", hasSize(expectedErrorCount)));
    }

    @Test
    public void testingOutConfigReaderTakeItAndGoUtility() {
        System.out.println(ConfigReader.getProperty("serenity.project.name"));
        System.out.println(ConfigReader.getProperty("spartan.rest.url"));
        System.out.println(ConfigReader.getProperty("spartan.editor.username"));
        System.out.println(ConfigReader.getProperty("spartan.editor.password"));
    }






}
