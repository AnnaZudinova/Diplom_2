package steps;

import io.qameta.allure.Step;
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

    @Step ("Установить email")
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Step ("Установить пароль")
    public void setPassword(String password) {
        user.setPassword(password);
    }

    @Step ("Установить имя")
    public void setName(String name) {
        user.setName(name);
    }

    @Step ("Создать уникального пользователя")
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

    @Step ("Создать неуникального пользователя")
    public Response createSameUser() {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(CREATE_USER);
    }

    @Step ("Авторизация существующего пользователя")
    public Response existingUserLogin () {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(LOGIN);
    }

    @Step ("Авторизация с несуществующей почтой")
    public Response loginWithNonExistingEmail () {
        user.setEmail(new Random().nextInt() + "-test@gmail.com");

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(LOGIN);
    }

    @Step ("Авторизация с несуществующим паролем")
    public Response loginWithNonExistingPassword () {
        user.setPassword(RandomStringUtils.randomAlphabetic(7));

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .post(LOGIN);
    }

       @Step ("Удалить пользователя")
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
