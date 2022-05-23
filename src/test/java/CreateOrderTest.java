import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CreateOrderTest {

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
    @DisplayName("Успешное создание заказа с ингредиентами без логина")
    public void successfullyCreateOrderWithIngredientsAndWithoutLogin() {
        accessToken = "";
        ValidatableResponse responseOrder = orderClient.createOrder(ingredientsForCreateNewBurger, accessToken);
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        boolean isOrderCreated = responseOrder.extract().path("success");
        int orderNumber = responseOrder.extract().path("order.number");

        assertThat(statusCodeResponseOrder, equalTo(200));
        assertTrue("Order is not created", isOrderCreated);
        assertNotNull("Пустой номер заказа", orderNumber);
    }

    @Test
    @DisplayName("Успешное создание заказа с ингредиентами с логином")
    public void successfullyCreateOrderWithIngredientsAndWithLogin() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.createOrder(ingredientsForCreateNewBurger, accessToken);
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        boolean isOrderCreated = responseOrder.extract().path("success");
        int orderNumber = responseOrder.extract().path("order.number");

        assertThat(statusCodeResponseOrder, equalTo(200));
        assertTrue("Order is not created", isOrderCreated);
        assertNotNull("Пустой номе заказа", orderNumber);
    }

    @Test
    @DisplayName("Неуспешное создание заказа с несуществующими ингредиентами с логиномлогином")
    public void unsuccessfullyCreateOrderWithNotRealIngredientsAndWithLogin() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.createOrder(OrderRequestCredentials.getNotRealIngredients(), accessToken);
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        assertThat(statusCodeResponseOrder, equalTo(500));
    }

    @Test
    @DisplayName("Создание заказа с логином без ингредиентов")
    public void unsuccessfullyCreateOrderWithLoginAndWithoutIngredients() {ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse responseOrder = orderClient.createOrder(OrderRequestCredentials.getWithoutIngredients(), accessToken);
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        boolean isOrderCreated = responseOrder.extract().path("success");
        String orderMessage = responseOrder.extract().path("message");

        assertThat(statusCodeResponseOrder, equalTo(400));
        assertFalse("Order is created", isOrderCreated);
        assertThat(orderMessage, equalTo("Ingredient ids must be provided"));
    }

    @After
    public void tearDown() {
        if (accessToken != "") {
            userClient.delete(accessToken);
        }
    }
}
