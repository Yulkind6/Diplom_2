import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class OrderRequestCredentials extends BaseUrl {

    private static final String INGREDIENTS_PATH = "/api/ingredients";
    public ArrayList<Object> ingredients;
    public static Faker faker = new Faker();


    public OrderRequestCredentials(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    @Step
    public static OrderRequestCredentials getRandom(){

        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(200);

        ArrayList<Object> ingredients = new ArrayList<>();

        int bunIndex = nextInt(0,2);
        int mainIndex = nextInt(0,9);
        int sauceIndex = nextInt(0,4);

        List<Object> bunIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'bun'}._id");
        List<Object> mainIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'main'}._id");
        List<Object> sauceIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'sauce'}._id");

        ingredients.add(bunIngredients.get(bunIndex));
        ingredients.add(mainIngredients.get(mainIndex));
        ingredients.add(sauceIngredients.get(sauceIndex));

        return new OrderRequestCredentials(ingredients);
    }

    public static OrderRequestCredentials getWithoutIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        return new OrderRequestCredentials(ingredients);
    }

    public static OrderRequestCredentials getNotRealIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        return new OrderRequestCredentials(ingredients);
    }

}