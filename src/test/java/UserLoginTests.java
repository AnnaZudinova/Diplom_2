import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTests extends BaseTests{
    UserSteps userSteps = new UserSteps();

    @Before
    @DisplayName("Создать пользователя для теста")
    public void createUser () {
        userSteps.createUser();
    }

    @Test
    @DisplayName("Вход существующим пользователем")
    public void checkExistingUserLoginStatusCode200 () {
        Response response = userSteps.existingUserLogin();
        response.then().statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @DisplayName("Вход c несуществующей почтой")
    public void checkLoginWithNonExistingEmail () {

        Response response = userSteps.loginWithNonExistingEmail();
        response.then().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message",equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход c несуществующим паролем")
    public void checkLoginWithNonExistingPassword () {

        Response response = userSteps.loginWithNonExistingPassword();
        response.then().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message",equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown () {
        userSteps.deleteUser();
    }

}
