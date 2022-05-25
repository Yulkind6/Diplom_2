import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ChangingUserDataTest {

    private UserClient userClient;
    DataToCreateNewUser user;
    private int expectedStatus;
    private boolean expectedSuccess;
    private String expectedErrorTextMessage;

    public ChangingUserDataTest(DataToCreateNewUser user, int expectedStatus, boolean expectedSuccess, String expectedErrorTextMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedSuccess = expectedSuccess;
        this.expectedErrorTextMessage = expectedErrorTextMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {DataToCreateNewUser.getWithEmailAndPassword(), 403, false,"Email, password and name are required fields"},
                {DataToCreateNewUser.getWithPasswordAndName(), 403, false,"Email, password and name are required fields"},
                {DataToCreateNewUser.getWithEmailAndName() , 403, false,"Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание пользователя без обязательных полей")
    public void createUsersWithouRequiredField() {
        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean actualSuccess = response.extract().path("success");
        String errorTextMessage = response.extract().path("message");

        assertThat(statusCode, equalTo(expectedStatus));
        assertThat(actualSuccess, equalTo(expectedSuccess));
        assertThat(errorTextMessage, equalTo(expectedErrorTextMessage));
    }
}
