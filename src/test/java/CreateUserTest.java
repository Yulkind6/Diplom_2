import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {

    String email = "abccba@yandex.ru";
    String password = "abccbapassword";
    String name = "ABC";
    String token;

    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUserSuccessTest() {
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
        UserCredentials userCredentials = new UserCredentials(email, password, name);
        token = UserSteps.createUser(userCredentials).then().extract().path("accessToken");

        Response response = UserSteps.createUser(userCredentials);
        response.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без email")
    public void createUserWithoutEmailErrorTest() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setPassword(password);
        userCredentials.setName(name);

        Response response = UserSteps.createUser(userCredentials);
        response.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без password")
    public void createUserWithoutPasswordErrorTest() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(email);
        userCredentials.setName(name);

        Response response = UserSteps.createUser(userCredentials);
        response.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без name")
    public void createUserWithoutNameErrorTest() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(email);
        userCredentials.setPassword(password);

        Response response = UserSteps.createUser(userCredentials);
        response.then()
                .assertThat()
                .statusCode(403)
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
