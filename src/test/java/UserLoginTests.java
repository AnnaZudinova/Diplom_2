import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTests extends BaseTests{
    UserSteps userSteps = new UserSteps();

    @Before
    @Description("Создать пользователя для теста")
    public void createUser () {
        userSteps.createUser();
    }

    @Test
    @Description("Вход существующим пользователем")
    public void checkExistingUserLoginStatusCode200 () {
        Response response = userSteps.existingUserLogin();
        response.then().statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @Description("Вход c несуществующей почтой")
    public void checkLoginWithNonExistingEmail () {

        Response response = userSteps.loginWithNonExistingEmail();
        response.then().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success",equalTo(false));
    }

    @Test
    @Description("Вход c несуществующим паролем")
    public void checkLoginWithNonExistingPassword () {

        Response response = userSteps.loginWithNonExistingPassword();
        response.then().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success",equalTo(false));
    }

    @After
    public void tearDown () {
        userSteps.deleteUser();
    }

}
