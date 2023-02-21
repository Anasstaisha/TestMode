package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").val(registeredUser.getLogin());
        $("[data-test-id=password] input").val(registeredUser.getPassword());
        $("[class=button__content]").click();
        $("h2").should(exactText("Личный кабинет")).should(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").val(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").val(notRegisteredUser.getPassword());
        $("[class=button__content]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль")).should(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").val(blockedUser.getLogin());
        $("[data-test-id=password] input").val(blockedUser.getPassword());
        $("[class=button__content]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован")).should(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").val(wrongLogin);
        $("[data-test-id=password] input").val(registeredUser.getPassword());
        $("[class=button__content]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль")).should(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").val(registeredUser.getLogin());
        $("[data-test-id=password] input").val(wrongPassword);
        $("[class=button__content]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль")).should(visible);
    }
}
