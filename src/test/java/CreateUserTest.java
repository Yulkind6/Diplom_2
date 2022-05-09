import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class CreateUserTest {

    String email = "abccba@yandex.ru";
    String password = "abccbapassword";
    String name = "ABC";
    String token;

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    public <Response> void createUserSuccessTest() {
        Response response = UserSteps.createUser(new UserCredentials(email, password, name));
        response.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        token = response.then()
                .extract()
                .path("accessToken");
        UserSteps.getUserData(token).then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Ошибка при создании уже зарегистрированного пользователя")
    public void createRegisteredUserErrorTest() {
        UserCredentials User = new UserCredentials(email, password, name);
        token = UserSteps.createUser(User).then().extract().path("accessToken");

        Response response = UserSteps.createUser(User);
        response.then()
                .assertThat()
                .statusCode(403)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без email")
    public void createUserWithoutEmailErrorTest() {
        UserCredentials User = new UserCredentials();
        User.setPassword(password);
        User.setName(name);

        Response response = UserSteps.createUser(User);
        response.then()
                .assertThat()
                .statusCode(403)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без password")
    public void createUserWithoutPasswordErrorTest() {
        UserCredentials User = new UserCredentials();
        User.setEmail(email);
        User.setName(name);

        Response response = UserSteps.createUser(User);
        response.then()
                .assertThat()
                .statusCode(403)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без name")
    public void createUserWithoutNameErrorTest() {
        UserCredentials User = new UserCredentials();
        User.setEmail(email);
        User.setPassword(password);

        Response response = UserSteps.createUser(User);
        response.then()
                .assertThat()
                .statusCode(403)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.deleteUser(token);
            token = null;
        }
    }
}
