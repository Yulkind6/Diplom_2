import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Получение данных об ингредиентах")
    public static Response getIngredients() {
        return given()
                .when()
                .get(BaseUrl.BASE_URL + "/ingredients");
    }

    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BaseUrl.BASE_URL + "/orders");
    }

    @Step("Создание заказа авторизованного пользовател")
    public static Response createOrderByAuthUser(OrderRequestCredentials OrderRequestCredentials, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(OrderRequestCredentials)
                .when()
                .post(BaseUrl.BASE_URL + "/orders");
    }

    @Step("Создание заказа неавторизованного пользователя")
    public static Response createOrderByUnauthUser(OrderRequestCredentials OrderRequestCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(OrderRequestCredentials)
                .when()
                .post(BaseUrl.BASE_URL + "/orders");
    }
}