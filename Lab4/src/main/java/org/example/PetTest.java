package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetTest {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String PET = "/pet";
    private static final int PET_ID = 12206; // Номер групи + номер студента
    private static final String PET_NAME = "Valeriia";
    private static final String PET_STATUS = "available";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }

    // POST-запит: створюємо нового улюбленця
    @Test
    public void verifyCreatePet() {
        System.out.println("Перевірка POST-запиту:");
        Map<String, Object> category = Map.of(
                "id", 122,
                "name", "DVO-Pets"
        );

        List<Map<String, Object>> tags = List.of(
                Map.of("id", 1, "name", "DVO"),
                Map.of("id", 2, "name", "Denisyuk")
        );

        Map<String, Object> body = Map.of(
                "id", PET_ID,
                "category", category,
                "name", PET_NAME,
                "tags", tags,
                "status", PET_STATUS
        );

        Response response = given().body(body)
                .when().post(PET);

        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();

        response.then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(PET_ID))
                .body("name", equalTo(PET_NAME))
                .body("status", equalTo(PET_STATUS))
                .body("category.id", equalTo(122))
                .body("category.name", equalTo("DVO-Pets"))
                .body("tags[0].name", equalTo("DVO"))
                .body("tags[1].name", equalTo("Denisyuk"));
    }

    // GET-запит: отримуємо дані про улюбленця
    @Test(dependsOnMethods = "verifyCreatePet")
    public void verifyGetPet() {
        System.out.println("Перевірка GET-запиту:");
        Response response = given()
                .pathParam("petId", PET_ID)
                .when()
                .get(PET + "/{petId}");

        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();

        response.then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(PET_ID))
                .body("name", equalTo(PET_NAME))
                .body("status", equalTo(PET_STATUS))
                .body("category.id", equalTo(122))
                .body("category.name", equalTo("DVO-Pets"))
                .body("tags[0].name", equalTo("DVO"))
                .body("tags[1].name", equalTo("Denisyuk"));
    }

    // PUT-запит: оновлюємо статус улюбленця
    @Test(dependsOnMethods = "verifyCreatePet")
    public void verifyUpdatePet() {
        System.out.println("Перевірка PUT-запиту:");
        String newStatus = "sold";

        Map<String, Object> category = Map.of(
                "id", 122,
                "name", "DVO-Pets"
        );

        List<Map<String, Object>> tags = List.of(
                Map.of("id", 1, "name", "DVO"),
                Map.of("id", 2, "name", "Denisyuk")
        );

        Map<String, Object> body = Map.of(
                "id", PET_ID,
                "category", category,
                "name", PET_NAME,
                "tags", tags,
                "status", newStatus
        );
        Response response = given().body(body)
                .when().put(PET);

        System.out.println("Код статусу: " + response.getStatusCode());
        response.prettyPrint();
        response.then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(PET_ID))
                .body("name", equalTo(PET_NAME))
                .body("status", equalTo(newStatus))
                .body("category.id", equalTo(122))
                .body("category.name", equalTo("DVO-Pets"))
                .body("tags[0].name", equalTo("DVO"))
                .body("tags[1].name", equalTo("Denisyuk"));
    }
}