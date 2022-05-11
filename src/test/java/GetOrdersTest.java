import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetOrdersTest {

    String email = "abccba@yandex.ru";
    String password = "abccbapassword";
    String name = "ABC";
    public ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    String token;

    @Test
    @DisplayName("Успешное получение заказов авторизованного пользователя")
    public void getOrdersSuccessTest() {
        token = UserSteps.createUser(new UserCredentials(email, password, name)).then().extract().path("accessToken");
        int orderNumber = OrderSteps.createOrderByAuthUser(new OrderRequestCredentials(ingredients), token).then().extract().path("order.number");

        Response response = OrderSteps.getUserOrders(token);
        response.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders[0]._id", notNullValue())
                .body("orders[0].ingredients", equalTo(ingredients))
                .body("orders[0].status", equalTo("done"))
                .body("orders[0].name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue())
                .body("orders[0].number", equalTo(orderNumber))
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Ошибка получения заказов без авторизации")
    public void getOrdersWithoutAuthorizationErrorTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(BaseUrl.BASE_URL + "/orders");
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
            UserSteps.deleteUser(token);
            token = null;
        }
    }
}