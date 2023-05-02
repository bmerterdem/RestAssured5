package GoRest;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    Faker randomuretici=new Faker();
    int userid;

    @Test
    public void createUser() {

        String rdnFullname=randomuretici.name().fullName();
        String rdnemail=randomuretici.internet().emailAddress();
        Map<String,String>newUser=new HashMap<>();
        newUser.put("name",rdnFullname);
        newUser.put("gender","male");
        newUser.put("email",rdnemail);
        newUser.put("status","active");
       userid=
               given().header("Authorization","Bearer b589147755bf3e072fc01f9b26bcf5aa0b2731fdfd75d45b3ef1d834f88600d9")
                       .contentType(ContentType.JSON).body(newUser).log().uri().when().post("https://gorest.co.in/public/v2/users/")
                       .then().log().body().statusCode(201).contentType(ContentType.JSON).extract().path("id");



    }

    @Test(dependsOnMethods = "createUser")
    public void GetUserById() {
    given().header("Authorization", "Bearer b589147755bf3e072fc01f9b26bcf5aa0b2731fdfd75d45b3ef1d834f88600d9").when()
                .get("https://gorest.co.in/public/v2/users/"+userid)
                .then().log().body().statusCode(200).contentType(ContentType.JSON).body("id",equalTo(userid));


    }

    @Test(dependsOnMethods = "GetUserById")
    public void UpdateUser() {
        Map<String,String> updateUser=new HashMap<>();
        updateUser.put("name","mert erdem");
        given()
                .header("Authorization","Bearer b589147755bf3e072fc01f9b26bcf5aa0b2731fdfd75d45b3ef1d834f88600d9")

         .body(updateUser)

                .when()
                .put("https://gorest.co.in/public/v2/users/"+userid)

                .then()
                //.log().body()
                .statusCode(200)
                .body("id", equalTo(userid))
                .body("name", equalTo("mert erdem"));

    }
}
