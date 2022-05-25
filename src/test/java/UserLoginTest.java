import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserLoginTest {

        private UserClient userClient;
        private DataToCreateNewUser user;
        private String accessToken;

        @Before
        public void setUp() {
                userClient = new UserClient();
                user = DataToCreateNewUser.getRandom();
        }

        @Test
        @DisplayName("Успешная авторизация существующего пользователя")
        public void loginSuccessTest() {
                userClient.createUser(user);
                ValidatableResponse response = userClient.login(new UserSteps(user.email, user.password));
                int statusCodeResponse = response.extract().statusCode();
                boolean isUserLogged = response.extract().path("success");
                accessToken = response.extract().path("accessToken");
                String refreshToken = response.extract().path("refreshToken");
                String actualEmail = response.extract().path("user.email");
                String actualName = response.extract().path("user.name");

                assertThat(statusCodeResponse, equalTo(200));
                assertTrue("Пользователь не авторизован", isUserLogged);
                assertNotNull(accessToken);
                assertNotNull(refreshToken);
                assertThat("Пользователь авторизовался под другим email", actualEmail, equalTo(user.email));
                assertThat("Пользователь авторизовался под другим name", actualName, equalTo(user.name));
        }

        @Test
        @DisplayName("Ошибка авторизации")
        public void loginIncorrectEmailAndPasswordErrorTest() {
                ValidatableResponse response = userClient.login(UserSteps.getWithNotRealEmailAndPassword(user));
                int statusCodeResponse = response.extract().statusCode();
                boolean isUserUnLogged = response.extract().path("success");
                String message = response.extract().path("message");

                assertThat(statusCodeResponse, equalTo(401));
                assertFalse(isUserUnLogged);
                assertThat(message, equalTo("email or password are incorrect"));
        }

        @After
        public void tearDown() {
                userClient.delete(accessToken);
        }
}
