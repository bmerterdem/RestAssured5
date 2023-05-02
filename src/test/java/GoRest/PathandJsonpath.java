package GoRest;

import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PathandJsonpath {
    @Test
    public void extractingJsonPath(){
        int postCode=given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then().log().body().extract().jsonPath().getInt("'post code'")
        ;
        System.out.println("postCode = " + postCode);
    }
    @Test
    public void getUsers(){
        Response response=
        given().when().get("https://gorest.co.in/public/v2/users/").
                then()
                .log().body()
                .extract().response()
        ;
        int idPath=response.path("[2].id");
        int idjsonpath=response.jsonPath().getInt("[2].id");
        System.out.println("idjsonpath = " + idjsonpath);
        System.out.println("idPath = " + idPath);
        User[]userpath=response.as(User[].class);//as nesne donusumunde pojo dizi destekli
        List<User> usersJsonPath=response.jsonPath().getList("",User.class);
        System.out.println("userpath = " + userpath);
        System.out.println(Arrays.toString(userpath));
    }
    @Test
    public void getUsersv1(){
       Response body= given().
                when().get("https://gorest.co.in/public/v1/users/").
                then()
               //.log().body()
                .extract().response()
        ;
       List<User>datausers=body.jsonPath().getList("data",User.class);
        System.out.println("datausers = " + datausers);
        //// Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        //        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        //        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        //        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        //        // diğer class lara gerek kalmadan
        //
        //        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        //        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }
    @Test
    public void getzipcode(){
       Response response= given().
                when().get("http://api.zippopotam.us/us/90210").
                then().extract().response()
                //.log().body()
        ;
        Location locpathas=response.as(Location.class);
        System.out.println(locpathas.getPlaces());
        List<Place>places=response.jsonPath().getList("places",Place.class);
        System.out.println("places = " + places);
    }
}
