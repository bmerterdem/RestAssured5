package GoRest;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoResttamir {
    int userID;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {
        reqSpec = new RequestSpecBuilder().addHeader("Authorization", "Bearer b589147755bf3e072fc01f9b26bcf5aa0b2731fdfd75d45b3ef1d834f88600d9")
                .setContentType(ContentType.JSON).build();

    }

    Faker randomuretici = new Faker();

    @Test(enabled = false)
    public void createUserJson() {

        String rndFUllname = randomuretici.name().fullName();
        String rndEmail = randomuretici.internet().emailAddress();

        userID = given().spec(reqSpec).
                contentType(ContentType.JSON).body("{\"name\":\"" + rndFUllname + "\", \"gender\":\"male\", \"email\":\"" + rndEmail + "\", \"status\":\"active\"}").
                log().uri().log().body().
                when().post("https://gorest.co.in/public/v2/users").
                then().log().body().statusCode(201).contentType(ContentType.JSON).extract().path("id")
        ;


    }

    @Test
    public void createUserMap() {

        String rndFUllname = randomuretici.name().fullName();
        String rndEmail = randomuretici.internet().emailAddress();
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFUllname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID = given().spec(reqSpec).
                contentType(ContentType.JSON)
                .body(newUser).
                log().uri().log().body().
                when().post("https://gorest.co.in/public/v2/users").
                then().log().body().statusCode(201).contentType(ContentType.JSON).extract().path("id")
        ;

    }

    @Test(enabled = false)
    public void createUserclas() {

        String rndFUllname = randomuretici.name().fullName();
        String rndEmail = randomuretici.internet().emailAddress();
        User newUser = new User();
        newUser.name = rndFUllname;
        newUser.gender = "male";
        newUser.email = rndEmail;
        newUser.status = "active";

        userID = given().spec(reqSpec).
                contentType(ContentType.JSON)
                .body(newUser).
                log().uri().log().body().
                when().post("https://gorest.co.in/public/v2/users").
                then().log().body().statusCode(201).contentType(ContentType.JSON).extract().path("id");
    }

    @Test(dependsOnMethods = "createUserMap")
    public void getUserByID() {
        given().spec(reqSpec).

                when().get("https://gorest.co.in/public/v2/users/" + userID).

                then().log().body().statusCode(200).contentType(ContentType.JSON).body("id", equalTo(userID))

        ;


    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "mert erdem");
        given().spec(reqSpec).body(updateUser).

                when().put("https://gorest.co.in/public/v2/users/" + userID).
                then().log().body().statusCode(200).body("id", equalTo(userID)).body("name", equalTo("mert erdem"));

    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {
        given().spec(reqSpec).
                when().delete("https://gorest.co.in/public/v2/users/" + userID).
                then().log().all().statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserneg() {
        given().spec(reqSpec).
                when().delete("https://gorest.co.in/public/v2/users/" + userID).
                then().log().all().statusCode(404)

        ;
    }
}