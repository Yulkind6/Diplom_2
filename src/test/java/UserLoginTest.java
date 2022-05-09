import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserLoginTest {

        String email = "abccba@yandex.ru";
        String password = "abccbapassword";
        String name = "ABC";
        String token;

        @Test
        @DisplayName("Успешная авторизация существующего пользователя")
        public void loginSuccessTest() {
        UserSteps.createUser(new UserCredentials(email, password, name));

        Response response = UserSteps.login(new UserCredentials(email, password));
        response.then()
        .assertThat()
        .statusCode(200)
        .body(matchesJsonSchemaInClasspath("createUserResponseJsonScheme.json"))
        .body("success", equalTo(true))
        .body("user.email", equalTo(email))
        .body("user.name", equalTo(name))
        .body("accessToken", notNullValue())
        .body("refreshToken", notNullValue());

        token = response.then()
        .extract()
        .path("accessToken");
        }

        @Test
        @DisplayName("Ошибка авторизации при некорректном email")
        public void loginIncorrectEmailErrorTest() {
        token = UserSteps.createUser(new UserCredentials(email, password, name)).then().extract().path("accessToken");

        Response response = UserSteps.login(new UserCredentials("incorrect_email", password));
        response.then()
        .assertThat()
        .statusCode(401)
        .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
        .body("success", equalTo(false))
        .body("message", equalTo("email or password are incorrect"));
        }

        @Test
        @DisplayName("Ошибка авторизации при некорректном password")
        public void loginIncorrectPasswordErrorTest() {
        token = UserSteps.createUser(new UserCredentials(email, password, name)).then().extract().path("accessToken");

        Response response = UserSteps.login(new UserCredentials(email, "incorrect_password"));
        response.then()
        .assertThat()
        .statusCode(401)
        .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
        .body("success", equalTo(false))
        .body("message", equalTo("email or password are incorrect"));
        }

        @Test
        @DisplayName("Ошибка авторизации без email")
        public void loginWithoutEmailErrorTest() {
        token = UserSteps.createUser(new UserCredentials(email, password, name)).then().extract().path("accessToken");
        UserCredentials UserCredentialsLogin = new UserCredentials();
        UserCredentialsLogin.setPassword(password);

        Response response = UserSteps.login(UserCredentialsLogin);
        response.then()
        .assertThat()
        .statusCode(401)
        .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
        .body("success", equalTo(false))
        .body("message", equalTo("email or password are incorrect"));
        }

        @Test
        @DisplayName("Ошибка авторизации без password")
        public void loginWithoutPasswordErrorTest() {
        token = UserSteps.createUser(new UserCredentials(email, password, name)).then().extract().path("accessToken");
        UserCredentials UserCredentialsLogin = new UserCredentials();
        UserCredentialsLogin.setEmail(email);

        Response response = UserSteps.login(UserCredentialsLogin);
        response.then()
        .assertThat()
        .statusCode(401)
        .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
        .body("success", equalTo(false))
        .body("message", equalTo("email or password are incorrect"));
        }

        @After
        public void tearDown() {
        if (token != null) {
        UserSteps.deleteUser(token);
        token = null;
        }
        }
}
