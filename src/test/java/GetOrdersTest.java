import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GetOrdersTest {

    private UserClient userClient;
    private DataToCreateNewUser user;
    String accessToken;
    private OrderRequestCredentials ingredientsForCreateNewBurger;
    private OrderSteps orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = DataToCreateNewUser.getRandom();
        orderClient = new OrderSteps();
        ingredientsForCreateNewBurger = OrderRequestCredentials.getRandom();
    }

    @Test
    @DisplayName("Успешное получение заказов авторизованного пользователя")
    public void getOrdersSuccessTest() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.userOrderInfo(accessToken);
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        boolean isGetOrders = responseOrder.extract().path("success");

        assertThat(statusCodeResponseOrder, equalTo(200));
        assertTrue("Заказы не получены", isGetOrders);
    }

    @Test
    @DisplayName("Ошибка получения заказов без авторизации")
    public void getOrdersWithoutAuthorizationErrorTest() {
        ValidatableResponse responseOrder = orderClient.userOrderInfoWithoutToken();
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        boolean isGetOrders = responseOrder.extract().path("success");
        String message = responseOrder.extract().path("message");

        assertThat(statusCodeResponseOrder, equalTo(401));
        assertFalse("Операция успешна", isGetOrders);
        assertThat(message, equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != "") {
            userClient.delete(accessToken);
        }
    }
}
