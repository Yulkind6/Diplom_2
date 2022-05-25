import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataToCreateNewUser {

    public String email;
    public String password;
    public String name;

    public DataToCreateNewUser() {
    }

    public DataToCreateNewUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static DataToCreateNewUser getRandom() {
        Faker faker = new Faker();

        final String emailUser = faker.internet().emailAddress();
        final String passwordUser = faker.internet().password(6, 10);
        final String nameUser = faker.name().firstName();
        return new DataToCreateNewUser(emailUser, passwordUser, nameUser);
    }

    public static DataToCreateNewUser getWithPasswordAndName() {
        Faker faker = new Faker();

        return new DataToCreateNewUser().setPassword(faker.internet().password(6, 10))
                .setName(faker.name().firstName());
    }

    public static DataToCreateNewUser getWithEmailAndName() {
        Faker faker = new Faker();
        return new DataToCreateNewUser().setEmail(faker.internet().emailAddress())
                .setName(faker.name().firstName());
    }

    public static DataToCreateNewUser getWithEmailAndPassword() {
        Faker faker = new Faker();
        return new DataToCreateNewUser().setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password(6, 10));
    }

    public static DataToCreateNewUser getEmail() {
        Faker faker = new Faker();
        return new DataToCreateNewUser().setEmail(faker.internet().emailAddress());
    }

    public static DataToCreateNewUser getPassword() {
        Faker faker = new Faker();
        return new DataToCreateNewUser().setPassword(faker.internet().password(6,10));
    }

    public static DataToCreateNewUser getName() {
        Faker faker = new Faker();
        return new DataToCreateNewUser().setName(faker.name().firstName());
    }

    public DataToCreateNewUser setEmail (String email) {
        this.email = email;
        return this;
    }

    public DataToCreateNewUser setPassword (String password) {
        this.password = password;
        return this;
    }

    public DataToCreateNewUser setName (String name) {
        this.name = name;
        return this;
    }
}