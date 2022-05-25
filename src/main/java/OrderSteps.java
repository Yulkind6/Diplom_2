import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderSteps extends BaseUrl {

    private static final String ORDER_PATH = "/api/orders";

    @Step
    public ValidatableResponse createOrder(OrderRequestCredentials ingredientsToCreateNewBurger, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(ingredientsToCreateNewBurger)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step
    public ValidatableResponse userOrderInfo(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step
    public ValidatableResponse userOrderInfoWithoutToken() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}