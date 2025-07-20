package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import model.Order;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps extends Endpoints {
    UserSteps userSteps = new UserSteps();
    Order order=new Order();

    @Step ("Получить список ингредиентов")
        public List<String> getIngredientIds() {
            Response response = given()
                    .get(GET_INGREDIENTS);
            List<String> ids = response.then()
                    .extract()
                    .body()
                    .jsonPath()
                    .getList("data._id");
            return ids;
        }

    @Step ("Установить значения ингредиентов в заказе")
    public void setOrderIngredients(){
        order.setIngredients(getIngredientIds());
    }

    @Step ("Создать заказ с ингредиентами с авторизацией")
    public Response createOrder() {
        setOrderIngredients();
        return given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", userSteps.accessToken))
                .body(order)
                .post(CREATE_ORDER);
    }
    @Step ("Создать заказ с ингредиентами без авторизации")
    public Response createOrderUnauthorized() {
        setOrderIngredients();
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(CREATE_ORDER);
    }

    @Step ("Создать заказ без ингредиентов")
    public Response createOrderWithoutIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", userSteps.accessToken))
                .post(CREATE_ORDER);
    }

    @Step ("Создать заказ с неверным хешем ингредиентов")
    public Response createOrderWithNonExistingIngredientsHash() {
        List<String> randomIngredients = new ArrayList<>();
        randomIngredients.add(RandomStringUtils.randomAlphanumeric(24));
        randomIngredients.add(RandomStringUtils.randomAlphanumeric(24));
        randomIngredients.add(RandomStringUtils.randomAlphanumeric(24));

        order.setIngredients(randomIngredients);

        return given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", userSteps.accessToken))
                .body(order)
                .post(CREATE_ORDER);
    }

}
