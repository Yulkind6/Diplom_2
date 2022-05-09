import java.util.ArrayList;

public class OrderRequestCredentials {
    public ArrayList<String> ingredients;

    public OrderRequestCredentials() {
        this.ingredients = new ArrayList<>();
        }
        public OrderRequestCredentials(ArrayList<String> ingredients) {
            this.ingredients = ingredients;
        }
        public void setIngredient(String ingredient) {
            ingredients.add(ingredient);
    }
}