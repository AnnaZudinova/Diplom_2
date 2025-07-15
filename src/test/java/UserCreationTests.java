import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import steps.UserSteps;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreationTests extends BaseTests {
    UserSteps userSteps = new UserSteps();

    @Test
    @Description("Создать уникального пользователя")
    public void checkUserCreationStatusCode200 () {
        Response response = userSteps.createUser();
        response.then().statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @Description("Нельзя создать пользователя, который уже зарегистрирован")
    public void checkSameUserCreationStatusCode403() {
        Response firstResponse = userSteps.createUser();
        Response secondResponse = userSteps.createSameUser();
        secondResponse.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("User already exists"));
    }

    @Test
    @Description("Нельзя создать пользователя без email")
    public void checkUserCreationWithoutEmailStatusCode403(){
        Response response = userSteps.createUserWithEmptyEmail();
        response.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Нельзя создать пользователя без password")
    public void checkUserCreationWithoutPasswordStatusCode403(){
        Response response = userSteps.createUserWithEmptyPassword();
        response.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Нельзя создать пользователя без name")
    public void checkUserCreationWithoutNameStatusCode403(){
        Response response = userSteps.createUserWithEmptyName();
        response.then().statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown () {
        userSteps.deleteUser();
    }
}
