import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseUrl {

    final static private String USER_PATH = "/api/auth/";

    @Step
    public ValidatableResponse createUser(DataToCreateNewUser dataToCreateNewUser) {
        return given()
                .spec(getBaseSpec())
                .body(dataToCreateNewUser)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step
    public ValidatableResponse login(UserSteps userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    @Step
    public void delete(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .delete(USER_PATH + "user")
                .then()
                .statusCode(202);
    }

    @Step
    public ValidatableResponse changeData(DataToCreateNewUser dataToCreateNewUser, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(dataToCreateNewUser)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step
    public ValidatableResponse changeDataWithoutToken(DataToCreateNewUser dataToCreateNewUser) {
        return given()
                .spec(getBaseSpec())
                .body(dataToCreateNewUser)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }
}