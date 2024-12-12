package ru.netology.pattern;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.pattern.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.pattern.DataGenerator.Registration.getUser;
import static ru.netology.pattern.DataGenerator.getRandomLogin;
import static ru.netology.pattern.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    void setup() {

        open("http://localhost:9999");

    }

    @Test
    @DisplayName("Should add active registered user successfully")
    void shouldBeLoginIfRegisteredActiveUser() {

        var registeredUser = getRegisteredUser("active");
        // логин registered, пароль user
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if unregistered user")
    void shouldGetErrorIfUnregisteredUser() {
        // логин любой
        var unregisteredUser = getUser("active");

        $("[data-test-id=login] input").setValue(unregisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(unregisteredUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification]")
                .shouldHave(Condition.text("Ошибка \n Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        // логин blockedUser, пароль registeredUser
        var blockedUser = getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification]")
                .shouldHave(Condition.text("Ошибка \n Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login is wrong")
    void shouldGetErrorIfWrongLogin() {
        //  логин любой, пароль - registered
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification]")
                .shouldHave(Condition.text("Ошибка \n Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if password is wrong ")
    void shouldGetErrorIfWrongPassword() {
        // логин registered, пароль любой
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id=error-notification]")
                .shouldHave(Condition.text("Ошибка \n Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);

    }
}
