package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LabThree {
    private static final String MOCK_SERVER_URL = "https://2157760f-0c07-45c7-8acd-24b871661210.mock.pstmn.io";
    private static final String USERS = "/users";
    private static final int USER_ID = 12206;
    private static final String USER_NAME = "ValeriiaDenisyuk";
    private static final String USER_EMAIL = "valeriia@example.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = MOCK_SERVER_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }

    //Тест GET-запиту отримання користувачів
    @Test
    public void testGetAllUsers() {
        Response response = given()
                .when()
                .get(USERS);

        response.then().statusCode(HttpStatus.SC_OK)
                .body("[0].id", equalTo(USER_ID))
                .body("[0].name", equalTo(USER_NAME))
                .body("[0].email", equalTo(USER_EMAIL))
                .body("[0].status", equalTo("active"));

        System.out.println("GET-запит отримання користувачів: ");
        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();
    }

    //Тест GET-запиту отримання користувача за id
    @Test(dependsOnMethods = "testCreateUser")
    public void testGetUserById() {
        Response response = given()
                .queryParam("id", USER_ID)
                .accept(ContentType.JSON)
                .when()
                .get(USERS);

        response.then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(USER_ID))
                .body("name", equalTo(USER_NAME))
                .body("email", equalTo(USER_EMAIL))
                .body("status", equalTo("active"));

        System.out.println("GET-запит отримання користувача за id: ");
        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();
    }

    //Тест POST-запиту створення користувача
    @Test
    public void testCreateUser() {
        Map<String, Object> body = Map.of(
                "id", USER_ID,
                "name", USER_NAME,
                "email", USER_EMAIL,
                "status", "active"
        );

        Response response = given()
                .queryParam("permission", "yes")
                .body(body)
                .when()
                .post(USERS);

        response.then().statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("User created successfully"))
                .body("id", equalTo(USER_ID));

        System.out.println("POST-запит створення користувача: ");
        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();
    }

    //Тест PUT-запиту оновлення даних користувача
    @Test(dependsOnMethods = "testCreateUser")
    public void testUpdateUser() {
        Map<String, Object> updatedBody = Map.of(
                "id", USER_ID,
                "name", "DVO",
                "email", "updated@example.com",
                "status", "inactive"
        );

        Response response = given()
                .queryParam("id", USER_ID)
                .body(updatedBody)
                .when()
                .put(USERS + "/" + USER_ID);

        response.then().statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("User updated successfully"))
                .body("id", equalTo(USER_ID));

        System.out.println("PUT-запит оновлення даних користувача: ");
        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();
    }

    //Тест DELETE-запиту видалення користувача
    @Test(dependsOnMethods = "testCreateUser")
    public void testDeleteUser() {
        Response response = given()
                .queryParam("id", USER_ID)
                .when()
                .delete(USERS);

        response.then().statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("User deleted successfully"));

        System.out.println("DELETE-запит видалення користувача: ");
        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();
    }

}
