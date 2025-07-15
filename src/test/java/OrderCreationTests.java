import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreationTests extends BaseTests{
    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

    @Before
    public void userCreation() {
        userSteps.createUser();
    }

    @Test
    @Description ("Создание заказа с авторизацией с ингридиентами")
    public void checkOrderCreationWithAuthorizationWithIngredients () {
        Response response= orderSteps.createOrder();

        response.then().statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    //Падает, т.к. заказ создается без авторизации, хотя не должен
    @Test
    @Description ("Создание заказа без авторизации с ингридиентами")
    public void checkOrderCreationWithoutAuthorizationWithIngredients () {
        Response response= orderSteps.createOrderUnauthorized();

        response.then().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @Description ("Создание заказа с авторизацией без ингридиентов")
    public void checkOrderCreationWithAuthorizationWithoutIngredients () {
        Response response= orderSteps.createOrderWithoutIngredients();

        response.then().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description ("Создание заказа с авторизацией с неверным хешем ингредиентов")
    public void checkOrderCreationWithAuthorizationWithNonExistingIngredientsHash () {
        Response response= orderSteps.createOrderWithNonExistingIngredientsHash();

        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown () {
        userSteps.deleteUser();
    }
}
