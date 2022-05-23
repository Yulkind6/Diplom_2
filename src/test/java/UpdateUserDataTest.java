import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class UpdateUserDataTest {

        private UserClient userClient;
        private DataToCreateNewUser user;
        private String accessToken;

        @Before
        public void setUp() {
                userClient = new UserClient();
                user = DataToCreateNewUser.getRandom();
        }

        @Test
        @DisplayName("Успешное обновление информации о пользователе (email, name)")
        public void updateUserEmailAndNameSuccessTest() {
                ValidatableResponse responseCreatedUser = userClient.createUser(user);
                accessToken = responseCreatedUser.extract().path("accessToken");
                DataToCreateNewUser newUserData = DataToCreateNewUser.getRandom();
                ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
                String actualEmail = responseChangeData.extract().path("user.email");
                String actualName = responseChangeData.extract().path("user.name");
                ValidatableResponse responseLoginWithNewData = userClient.login(new UserSteps(newUserData.email, newUserData.password));
                int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
                int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

                assertThat(statusCodeResponseChangeData, equalTo(200));
                assertThat("У пользователя не изменились данные email", actualEmail, equalTo(newUserData.email));
                assertThat("У пользователя не изменились данные name", actualName, equalTo(newUserData.name));
                assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
        }

        @Test
        @DisplayName("Успешное обновление информации о пользователе (email)")
        public void updateUserEmailSuccessTest() {
                ValidatableResponse responseCreatedUser = userClient.createUser(user);
                accessToken = responseCreatedUser.extract().path("accessToken");
                DataToCreateNewUser newUserData = DataToCreateNewUser.getEmail();
                ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
                String actualEmail = responseChangeData.extract().path("user.email");
                String actualName = responseChangeData.extract().path("user.name");
                ValidatableResponse responseLoginWithNewData = userClient.login(new UserSteps(newUserData.email, user.password));
                int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
                int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

                assertThat(statusCodeResponseChangeData, equalTo(200));
                assertThat("У пользователя не изменились данные email", actualEmail, equalTo(newUserData.email));
                assertThat("У пользователя не изменились данные name", actualName, equalTo(user.name));
                assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
        }

        @Test
        @DisplayName("Успешное обновление информации о пользователе (password)")
        public void updateUserPasswordSuccessTest() {
                ValidatableResponse responseCreatedUser = userClient.createUser(user);
                accessToken = responseCreatedUser.extract().path("accessToken");
                DataToCreateNewUser newUserData =  DataToCreateNewUser.getPassword();
                ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
                String actualEmail = responseChangeData.extract().path("user.email");
                String actualName = responseChangeData.extract().path("user.name");
                ValidatableResponse responseLoginWithNewData = userClient.login(new UserSteps(user.email, newUserData.password));
                int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
                int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

                assertThat(statusCodeResponseChangeData, equalTo(200));
                assertThat("У пользователя не изменились данные email", actualEmail, equalTo(user.email));
                assertThat("У пользователя не изменились данные name", actualName, equalTo(user.name));
                assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
        }

        @Test
        @DisplayName("Успешное обновление информации о пользователе (name)")
        public void updateUserNameSuccessTest() {
                ValidatableResponse responseCreatedUser = userClient.createUser(user);
                accessToken = responseCreatedUser.extract().path("accessToken");
                DataToCreateNewUser newUserData =  DataToCreateNewUser.getName();
                ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
                String actualEmail = responseChangeData.extract().path("user.email");
                String actualName = responseChangeData.extract().path("user.name");
                ValidatableResponse responseLoginWithNewData = userClient.login(new UserSteps(user.email, user.password));
                int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
                int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

                assertThat(statusCodeResponseChangeData, equalTo(200));
                assertThat("У пользователя не изменились данные email", actualEmail, equalTo(user.email));
                assertThat("У пользователя не изменились данные name", actualName, equalTo(newUserData.name));
                assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
        }

        @Test
        @DisplayName("Ошибка обновления информации неавторизованного пользователя")
        public void updateUnauthorizedUserDataErrorTest() {
                userClient.createUser(user);
                DataToCreateNewUser newUserData = DataToCreateNewUser.getRandom();
                ValidatableResponse responseChangeDataWithoutToken = userClient.changeDataWithoutToken(newUserData);
                boolean isEmail = responseChangeDataWithoutToken.extract().path("success");
                String message = responseChangeDataWithoutToken.extract().path("message");
                ValidatableResponse responseLoginWithNewData = userClient.login(new UserSteps(newUserData.email, newUserData.password));
                int statusCodeResponseChangeData = responseChangeDataWithoutToken.extract().statusCode();
                int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

                assertThat(statusCodeResponseChangeData, equalTo(401));
                assertFalse(isEmail);
                assertThat(message, equalTo("You should be authorised"));
                assertThat(statusCodeResponseLoginWithNewData, equalTo(401));
        }

        @After
        public void tearDown() {
                userClient.delete(accessToken);
        }
}