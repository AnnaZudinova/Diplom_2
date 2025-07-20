import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import steps.UserSteps;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreationTests extends BaseTests {
    UserSteps userSteps = new UserSteps();

    @Test
    @DisplayName("Создать уникального пользователя")
    public void checkUserCreationStatusCode200 () {
        Response response = userSteps.createUser();
        response.then().statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    public void checkSameUserCreationStatusCode403() {
        Response firstResponse = userSteps.createUser();
        Response secondResponse = userSteps.createSameUser();
        secondResponse.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("User already exists"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без email")
    public void checkUserCreationWithoutEmailStatusCode403(){
        userSteps.setEmail(null);
        Response response = userSteps.createUser();
        response.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без password")
    public void checkUserCreationWithoutPasswordStatusCode403(){
        userSteps.setPassword(null);
        Response response = userSteps.createUser();
        response.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без name")
    public void checkUserCreationWithoutNameStatusCode403(){
        userSteps.setName(null);
        Response response = userSteps.createUser();
        response.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown () {
        userSteps.deleteUser();
    }
}
