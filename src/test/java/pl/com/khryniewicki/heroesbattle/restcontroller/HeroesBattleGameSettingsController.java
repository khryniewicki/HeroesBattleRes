package pl.com.khryniewicki.heroesbattle.restcontroller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.com.khryniewicki.heroesbattle.ResourceServerApp;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;
import pl.com.khryniewicki.heroesbattle.web.dto.Authentication;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.com.khryniewicki.heroesbattle.service.RegisterService.MOCK_REGISTERED;
import static pl.com.khryniewicki.heroesbattle.web.restcontroller.HeroesBattleGameSettingsController.GAME_STARTED;
import static pl.com.khryniewicki.heroesbattle.web.restcontroller.HeroesBattleGameSettingsController.GAME_STOPPED;

//Before running this live test make sure both authorization server and resource server are running   
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {ResourceServerApp.class})
public class HeroesBattleGameSettingsController extends HeroesBattleGameSettingsControllerSetUp {
    public static final String TEST = "/api/option/test";
    public static final String START_MAP = "/api/option/init-map";
    public static final String STOP_MAP = "/api/option/stop-map";
    public static final String CHECK_ROLE = "/api/option/check-role";
    public static final String EMPTY_ROOM = "/api/option/empty-room";


    @Test
    @Order(1)
    public void should_start_heroes_map_and_return_message_when_use_admin_user_and_init_map() {
        final String accessToken = obtainAccessToken(CLIENT_ID, USERNAME, PASSWORD);
        final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken)
                .get(RESOURCE_SERVER + START_MAP);
        assertEquals(200, response.getStatusCode());


        String test = response.then().extract().body().asString();
        Message message = message_mapper(test);
        assertEquals(message.getContent(), GAME_STARTED);
    }

    @Test
    @Order(2)
    public void should_return_playerName_when_use_admin_user_and_test_map() {
        final String accessToken = obtainAccessToken(CLIENT_ID, USERNAME, PASSWORD);
        final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken)
                .get(RESOURCE_SERVER + TEST);
        assertEquals(200, response.getStatusCode());

        String test = response.then().extract().body().asString();
        Message message = message_mapper(test);
        assertEquals(message.getContent(), String.format(MOCK_REGISTERED, "MANEKIN"));
    }

    @Test
    @Order(3)
    public void should_confirm_user_is_admin_and_return_authentication_when_use_admin_user_and_check_role() {
        final String accessToken = obtainAccessToken(CLIENT_ID, USERNAME, PASSWORD);
        final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken)
                .get(RESOURCE_SERVER + CHECK_ROLE);
        assertEquals(200, response.getStatusCode());

        String test = response.then().extract().body().asString();
        Authentication authentication = authentication_mapper(test);
        assertTrue(authentication.isCredentials());
    }

    @Test
    @Order(4)
    public void should_clear_heroes_map_and_return_message_when_use_admin_user_and_empty_room() {

        final String accessToken = obtainAccessToken(CLIENT_ID, USERNAME, PASSWORD);
        final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken)
                .get(RESOURCE_SERVER + EMPTY_ROOM);
        assertEquals(200, response.getStatusCode());

        String test = response.then().extract().body().asString();
        Message message = message_mapper(test);
        assertEquals(message.getContent(), RegisterService.ROOM_CLEARED);
    }

    @Test
    @Order(5)

    public void should_make_heroes_map_null_and_return_message_when_use_admin_user_and_stop_map() {
        final String accessToken = obtainAccessToken(CLIENT_ID, USERNAME, PASSWORD);
        final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken)
                .get(RESOURCE_SERVER + STOP_MAP);
        assertEquals(200, response.getStatusCode());

        String test = response.then().extract().body().asString();
        Message message = message_mapper(test);
        assertEquals(message.getContent(), GAME_STOPPED);
    }
}
