import com.github.javafaker.Faker;

public class UserSteps {

    public String email;
    public String password;

    public UserSteps() {
    }

    public UserSteps(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserSteps setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserSteps setPassword(String password) {
        this.password = password;
        return this;
    }

    public static UserSteps getWithNotRealEmailAndPassword(DataToCreateNewUser user) {
        Faker faker = new Faker();
        return new UserSteps().setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password(6,10));
    }
}