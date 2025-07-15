package steps;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class OrderSteps extends Endpoints {
    UserSteps userSteps = new UserSteps();
    Order order=new Order();

    @Step
    @DisplayName("Получить список ингредиентов")
    public String[] getIngredientIds() {
        Ingredients ingredients = given()
                .get(GET_INGREDIENTS)
                .body()
                .as(Ingredients.class);

        String[] ids = new String[ingredients.getData().length];
        for (int i = 0; i < ingredients.getData().length; i++) {
            ids[i] = ingredients.getData()[i].get_id();
        }

        return ids;
    }

    @Step
    @DisplayName("Установить значения ингредиентов в заказе")
    public void setOrderIngredients(){
        order.setIngredients(getIngredientIds());
    }

    @Step
    @DisplayName("Создать заказ с ингредиентами с авторизацией")
    public Response createOrder() {
        setOrderIngredients();
        return given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", userSteps.accessToken))
                .body(order)
                .post(CREATE_ORDER);
    }
    @Step
    @DisplayName("Создать заказ с ингредиентами без авторизации")
    public Response createOrderUnauthorized() {
        setOrderIngredients();
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(CREATE_ORDER);
    }

    @Step
    @DisplayName("Создать заказ без ингредиентов")
    public Response createOrderWithoutIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", userSteps.accessToken))
                .post(CREATE_ORDER);
    }

    @Step
    @DisplayName("Создать заказ с неверным хешем ингредиентов")
    public Response createOrderWithNonExistingIngredientsHash() {
        String[] randomIngredients = new String[] {RandomStringUtils.randomAlphanumeric(24),RandomStringUtils.randomAlphanumeric(24),RandomStringUtils.randomAlphanumeric(24)};
        order.setIngredients(randomIngredients);

        return given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", userSteps.accessToken))
                .body(order)
                .post(CREATE_ORDER);
    }

}
