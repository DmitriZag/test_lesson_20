package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTest {
    @BeforeAll
    static void restAssuredBase() {
        RestAssured.baseURI = "https://reqres.in/api";
}
@Test
void createUserTest() {
    String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

    Response response = given()
            .log().uri()
            .log().method()
            .log().body()
            .contentType(JSON)
            .body(body)
            .when()
            .post("/users")
            .then()
            .log().status()
            .log().body()
            .body(matchesJsonSchemaInClasspath("schemas/user_schema.json"))
            .extract().response();

    assertThat(response.statusCode(), is(201));
    assertThat(response.path("name"), equalTo("morpheus"));
    assertThat(response.path("job"), equalTo("leader"));
}

@Test
public void successLoginTest() {
    String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

    Response response = given()
            .log().uri()
            .log().method()
            .log().body()
            .contentType(JSON)
            .body(body)
            .when()
            .post("/login")
            .then()
            .log().status()
            .log().body()
            .body(matchesJsonSchemaInClasspath("schemas/login_schema.json"))
            .extract().response();

    assertThat(response.statusCode(), is(200));
    assertThat(response.path("token"), is(notNullValue()));
}

    @Test
    public void successRegisterTest() {
        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .body(matchesJsonSchemaInClasspath("schemas/register_schema.json"))
                .extract().response();

        assertThat(response.statusCode(), is(200));
        assertThat(response.path("id"), is(notNullValue()));
        assertThat(response.path("token"), is(notNullValue()));
    }
    @Test
    public void unsuccessLoginTest() {
        String body = "{ \"email\": \"peter@klaven\"}";

        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .body(matchesJsonSchemaInClasspath("schemas/unsuccess_login_schema.json"))
                .extract().response();

        assertThat(response.statusCode(), is(400));
        assertThat(response.path("error"), equalTo("Missing password"));
    }

    @Test
    public void userNotFoundTest() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users/23")
                .then()
                .log().status()
                .statusCode(404);

    }
}

