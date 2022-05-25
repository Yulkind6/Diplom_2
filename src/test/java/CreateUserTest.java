import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CreateUserTest {

    private UserClient userClient;
    private DataToCreateNewUser user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = DataToCreateNewUser.getRandom();
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUserSuccessTest() {
        ValidatableResponse response = userClient.createUser(user);
        int statusCodePositiveResponseCreate = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        ValidatableResponse responseUserLogged = userClient.login(new UserSteps(user.email, user.password));
        String refreshToken = responseUserLogged.extract().path("refreshToken");
        accessToken = responseUserLogged.extract().path("accessToken");

        assertThat(statusCodePositiveResponseCreate, equalTo(200));
        assertTrue("User is not created", isUserCreated);
        assertNotNull("Пустой accessToken", accessToken);
        assertNotNull("Пустой refreshToken", refreshToken);
    }

    @Test
    @DisplayName("Ошибка при создании уже зарегистрированного пользователя")
    public void createRegisteredUserErrorTest() {
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(user);
        int statusCodeNegativeResponse = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");

        assertThat(statusCodeNegativeResponse, equalTo(403));
        assertFalse(isSuccess);
        assertThat("User already exists", message, (equalTo("User already exists")));
    }
    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}


