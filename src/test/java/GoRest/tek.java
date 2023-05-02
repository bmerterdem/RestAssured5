package GoRest;

import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class tek {
    @Test
    public void extractingPath(){
        //"http://api.zippopotam.us/us/90210"
       String postcode= given()
                .when().get("http://api.zippopotam.us/us/90210")

                .then().log().body().extract().path("'post code'");
        System.out.println("postcode = " + postcode);
    }

    @Test
    public void extracting2Path(){
        //"http://api.zippopotam.us/us/90210"
        int postcode= given()
                .when().get("http://api.zippopotam.us/us/90210")

                .then().log().body().extract().jsonPath().getInt("'post code'");
        System.out.println("postcode = " + postcode);
    }
    @Test
    public void getusers(){
        Response response=given().when().get("https://gorest.co.in/public/v2/users/")
                .then().log().body().extract().response();
        int idpath=response.path("[2].id");
        int idjsonpath=response.jsonPath().getInt("[2].id");
        System.out.println("idpath = " + idpath);
        System.out.println("idjsonpath = " + idjsonpath);
       User[] userpath= response.as(User[].class);
        List<User>usersjsonpath=response.jsonPath().getList("", User.class);
        System.out.println(Arrays.toString(userpath));
        System.out.println("usersjsonpath = " + usersjsonpath);

    }
    @Test
    public void getusersv1(){
        Response body=
        given()
                .when().get("https://gorest.co.in/public/v1/users/")

                .then().log().body().extract().response();
        List<User>datausers=body.jsonPath().getList("data", User.class);
        System.out.println("datausers = " + datausers);
    }
    @Test
    public void getzip(){
       Response response= given().when().get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body();
                .extract().response();
        Location locpathas=response.as(Location.class);
        System.out.println(locpathas.getPlaces());
        List<Place>places=response.jsonPath().getList("places",Place.class);
        System.out.println("places = " + places);
    }
}
