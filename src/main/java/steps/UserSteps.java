package steps;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import model.RegistrationResponse;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserSteps extends Endpoints{
    protected String email = new Random().nextInt() + "-test@ya.ru";
    protected String password = RandomStringUtils.randomAlphabetic(10);
    protected String name = RandomStringUtils.randomAlphabetic(10);
    protected String accessToken;

    User user = new User(email,password,name);


    @Step
    @DisplayName("Создать уникального пользователя")
    public Response createUser() {
        Response response= given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(CREATE_USER);

        RegistrationResponse registrationResponse = response.as(RegistrationResponse.class);
        accessToken = registrationResponse.getAccessToken();

        return response;
    }

    @Step
    @DisplayName("Создать неуникального пользователя")
    public Response createSameUser() {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(CREATE_USER);
    }

    @Step
    @DisplayName("создать пользователя c пустым полем Email")
    public Response createUserWithEmptyEmail() {
        user.setEmail(null);

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(CREATE_USER);
    }

    @Step
    @DisplayName("создать пользователя с пустым полем password")
    public Response createUserWithEmptyPassword() {
        user.setPassword(null);

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(CREATE_USER);
    }

    @Step
    @DisplayName("создать пользователя с пустым полем name")
    public Response createUserWithEmptyName() {
        user.setName(null);

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(CREATE_USER);
    }

    @Step
    @DisplayName("Авторизация существующего пользователя")
    public Response existingUserLogin () {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(LOGIN);
    }

    @Step
    @DisplayName("Авторизация с несуществующей почтой")
    public Response loginWithNonExistingEmail () {
        user.setEmail(new Random().nextInt() + "-test@gmail.com");

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(LOGIN);
    }

    @Step
    @DisplayName("Авторизация с несуществующим паролем")
    public Response loginWithNonExistingPassword () {
        user.setPassword(RandomStringUtils.randomAlphabetic(7));

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(LOGIN);
    }

       @Step
       @DisplayName("Удалить пользователя")
       public void deleteUser(){
       try  {given()
                .contentType(ContentType.JSON)
               .header(new Header("Authorization", accessToken))
                .and()
                .body(user)
                .delete(DELETE_USER);}
       catch (Exception e) {
           System.out.println(e);}
    }


}
