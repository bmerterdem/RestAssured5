import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test() {
        given()
                .when()
                .then();

    }

    @Test
    public void ilktest() {
        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void checkCountryResponseBody() {
        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().statusCode(200)
                .contentType(ContentType.JSON)
                .body("country", equalTo("United States"))

        ;
        //
//    PM                            RestAssured
//    body.country                  body("country")
//    body.'post code'              body("post code")
//    body.places[0].'place name'   body("places[0].'place name'")
//    body.places.'place name'      body("places.'place name'")
//    bütün place nameleri bir arraylist olarak verir
//    https://jsonpathfinder.com/
    }

    @Test
    public void checkStateResponseBody() {
        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().statusCode(200)
                .contentType(ContentType.JSON)
                .body("places[0].state", equalTo("California"))

        ;
    }

    @Test
    public void checkHasResponseBody() {
        given()
                .when().get("http://api.zippopotam.us/tr/01000")
                .then().log().body().statusCode(200)
                .body("places.'place name'", hasItem("Dervişler Köyü"))
        ;

    }

    @Test
    public void BodyArraysizetest() {
        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().statusCode(200)
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void combiningtest() {
        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().statusCode(200)
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
        ;
    }

    @Test
    public void pathparamtest() {
        given().pathParam("ulke", "us")
                .pathParam("postaKod", 90210)
                .log().uri()

                .when().get("http://api.zippopotam.us/{ulke}/{postaKod}")
                .then().statusCode(200)

        ;
    }

    @Test
    public void queryparamtest() {
        given().param("page", 1)
                .log().uri()

                .when().get("https://gorest.co.in/public/v1/users")
                .then().statusCode(200)
                .log().body()

        ;
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void Setup() {

        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void test1() {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1)  // ?page=1  şeklinde linke ekleniyor
                .spec(requestSpec)

                .when()
                .get("/users")  // ?page=1

                .then()
                .spec(responseSpec)
        ;
    }

    @Test
    public void extractingJsonPath() {
        String countryName = given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().extract().path("country");
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");


    }

    @Test
    public void extractingJsonPath2() {
        String placeName = given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().extract().path("places[0].'place name'");
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");


    }

    @Test
    public void extractingJsonPath3() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki limit bilgisini yazdırınız.
        int limit = given().
                when().get("https://gorest.co.in/public/v1/users ").
                then()
                //.log().body()
                .statusCode(200).extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);

    }

    @Test
    public void extractingJsonPath4() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki tum idleri yazdiriniz
        List<Integer> idler = given().
                when().get("https://gorest.co.in/public/v1/users ").
                then()
                //.log().body()
                .statusCode(200).extract().path("data.id");
        System.out.println("idler = " + idler);

    }

    @Test
    public void extractingJsonPath5() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki tum nameleri yazdiriniz
        List<String> nameler = given().
                when().get("https://gorest.co.in/public/v1/users ").
                then()
                //.log().body()
                .statusCode(200).extract().path("data.name");
        System.out.println("nameler = " + nameler);
    }

    @Test
    public void extractingJsonpathresponseall() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki tum nameleri yazdiriniz
        Response donendata = given().
                when().get("https://gorest.co.in/public/v1/users ").
                then()
                //.log().body()
                .statusCode(200).extract().response();
        List<Integer> idler = donendata.path("data.id");
        List<String> names = donendata.path("data.name");
        int limit = donendata.path("meta.pagination.limit");
        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);
        Assert.assertTrue(names.contains("Dakshayani Pandey"));
        Assert.assertTrue(idler.contains(1203767));
        Assert.assertEquals(limit, 10, "test sonucu hatali");

    }

    @Test
    public void extractjsonall() {
        Location locationnesnesi = given().when().get("http://api.zippopotam.us/us/90210").then().extract().body().
                as(Location.class);
        System.out.println("locationnesnesi.getCountry() = " + locationnesnesi.getCountry());
        for (Place p : locationnesnesi.getPlaces())
            System.out.println("p = " + p);
        System.out.println(locationnesnesi.getPlaces().get(0).getPlacename());


    }

    @Test
    public void extractsjonall_POJO() {
        //asagidaki endpointe dortagac koyu ait diger bilgileri yazdiriniz
        Location adana=given()
                .when().get("http://api.zippopotam.us/tr/01000")
                .then()
                //.log().body()
                .statusCode(200).extract().body().as(Location.class)

        ;
        for(Place p: adana.getPlaces())
            if(p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")){
                System.out.println("p = " + p);
            }


    }

}