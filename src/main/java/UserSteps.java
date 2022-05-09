import io.qameta.allure.Step;

import java.net.http.HttpRequest;

public class UserSteps {

    @Step("Создание уникального пользователя")
        public static <Response> Response createUser(UserCredentials userCredentials) {
            return given()
                    .header("Content-type", "application/json")
                    .body(userCredentials)
                    .when()
                    .post(BaseUrl.BASE_URL + "/auth/register");
        }

    private static HttpRequest.Builder given() {
        return null;
    }

    @Step("Авторизация пользователя")
        public static <Response> Response login(UserCredentials userCredentials) {
            return given()
                    .header("Content-type", "application/json")
                    .body(userCredentials)
                    .when()
                    .post(BaseUrl.BASE_URL + "/auth/login");
        }

        @Step("Логин под существующим пользователем")
        public static <Response> Response getUserData(String token) {
            return given()
                    .header("Authorization", token)
                    .when()
                    .get(BaseUrl.BASE_URL + "/auth/user");
        }

        @Step("Изменение данных пользователя с авторизацией")
        public static <Response> Response updateUserData(UserCredentials userCredentialsUpdate, String token) {
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", token)
                    .body(userCredentialsUpdate)
                    .when()
                    .patch(BaseUrl.BASE_URL + "/auth/user");
        }

        @Step("Удаление пользователя")
        public static void deleteUser(String token) {
            given()
                    .header("Authorization", token)
                    .when()
                    .delete(BaseUrl.BASE_URL + "/auth/user")
                    .then()
                    .statusCode(202);
        }
}