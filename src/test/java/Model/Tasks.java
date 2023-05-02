package Model;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {
    /** Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */
    @Test
    public void task2(){
        todo td=given()

                .when().get(" https://jsonplaceholder.typicode.com/todos/2")
                .then().log().all().extract().body().as(todo.class)
        ;
        System.out.println("td = " + td);
        System.out.println(td.getTitle());
    }
    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
@Test
    public void task3(){
    given()
            .when().get("https://jsonplaceholder.typicode.com/todos/2")
            .then().log().body().statusCode(200).contentType(ContentType.JSON).body("title",equalTo("quis ut nam facilis et officia qui"))


    ;
}
/**
 * Task 4
 * create a request to https://jsonplaceholder.typicode.com/todos/2
 * expect status 200
 * expect content type JSON
 * expect response completed status to be false(hamcrest)
 * extract completed field and testNG assertion(testNG)
 */

@Test
    public void task4(){
    given()

            .when().get("https://jsonplaceholder.typicode.com/todos/2")

            .then().log().body().statusCode(200).contentType(ContentType.JSON).body("completed",equalTo(false))


    ;
    Boolean completed=given()

            .when().get("https://jsonplaceholder.typicode.com/todos/2")

            .then().log().body().statusCode(200).contentType(ContentType.JSON).extract().path("completed");
    Assert.assertFalse(completed);


}
}
