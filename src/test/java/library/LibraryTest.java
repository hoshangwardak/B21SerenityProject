package library;

import config_util.ConfigReader;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.reset;

@SerenityTest
public class LibraryTest {

    @BeforeAll
    public static void init(){
        baseURI  = ConfigReader.getProperty("base.url");
        basePath = ConfigReader.getProperty("base.path") ;

    }
    @AfterAll()
    public static void cleanup() {
        reset();
    }

    @DisplayName("Login and Get Dashboard info")
    @Test
    public void testDashboardStats(){

        // Send POST /login Request and grab the token from the response
        String token =  given()
                .contentType(ContentType.URLENC)
                .formParam("email" , ConfigReader.getProperty("librarian.username")  )
                .formParam("password" , ConfigReader.getProperty("librarian.password")  ).
                        when()
                .post("/login").prettyPeek()
                .path("token") ;
        // Send GET /dashboard_stats Request and manually verify the number
        SerenityRest.given()
                .header("X-LIBRARY-TOKEN" , token).
                when()
                .get("/dashboard_stats").prettyPeek() ;
        // Verify these number against database database
        // It should work in any environment


    }


    @Test
    public void testReadingConfProperties(){

        System.out.println( ConfigReader.getProperty("base.url")  );
        // Select Environment in maven command as below





    }


}
