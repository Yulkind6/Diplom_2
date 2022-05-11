import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateUserCredentialsTest {

        String email = "abccba@yandex.ru";
        String password = "abccbapassword";
        String name = "ABC";
        String emailUpdate = "abccbanew@yandex.ru";
        String passwordUpdate = "abccbapasswordnew";
        String nameUpdate = "ABCnew";
        String token;

        @Test
        @DisplayName("Успешное обновление информации о пользователе")
        public void updateUserDataSuccessTest() {
                token = UserSteps.createUser(new UserCredentials(email, password, name)).then().extract().path("accessToken");

                Response response = UserSteps.updateUserData(new UserCredentials(emailUpdate, passwordUpdate, nameUpdate), token);
                response.then()
                        .assertThat()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("user.email", equalTo(emailUpdate))
                        .body("user.name", equalTo(nameUpdate));

                UserSteps.getUserData(token).then()
                        .assertThat()
                        .body("user.email", equalTo(emailUpdate))
                        .body("user.name", equalTo(nameUpdate));

                UserSteps.login(new UserCredentials(emailUpdate, passwordUpdate)).then()
                        .statusCode(200);
        }

        @Test
        @DisplayName("Ошибка обновления информации неавторизованного пользователя")
        public void updateUnauthorizedUserDataErrorTest() {
                Response response = given()
                        .header("Content-type", "application/json")
                        .body(new UserCredentials(emailUpdate, passwordUpdate, nameUpdate))
                        .when()
                        .patch(BaseUrl.BASE_URL + "/auth/user");

                response.then()
                        .assertThat()
                        .statusCode(401)
                        .body("success", equalTo(false))
                        .body("message", equalTo("You should be authorised"));
        }

        @After
        public void tearDown() {
                if (token != null) {
                        UserSteps.deleteUser(token);
                        token = null;
                }
        }
}