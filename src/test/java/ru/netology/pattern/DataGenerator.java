package ru.netology.pattern;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static RegistrationDto sendRequest(RegistrationDto user) {

        given()                             // отправка запроса с передачей данных
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        return user;

    }

    public static String getRandomLogin() {

        return faker.name().username();     // генерация случайного логина

    }

    public static String getRandomPassword() {

        return faker.internet().password();   // генерация случайного пароля

    }

    public static class Registration {

        private Registration() {
        }

        public static RegistrationDto getUser(String status) {

            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);  // создание пользователя

        }

        public static RegistrationDto getRegisteredUser(String status) {

            return sendRequest(getUser(status));  // отправка запроса на регистрацию пользователя

        }

    }

    @Value
    public static class RegistrationDto {

        String login;
        String password;
        String status;

    }

}
