package pl.com.khryniewicki.heroesbattle.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pl.com.khryniewicki.heroesbattle.web.dto.Authentication;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;

import java.util.HashMap;
import java.util.Map;

public class HeroesBattleGameSettingsControllerSetUp {
   protected final static String AUTH_SERVER = "http://localhost:8085/auth/realms/heroes_battle/protocol/openid-connect";
   protected final static String RESOURCE_SERVER = "http://localhost:8445/resource-server";
   protected final static String CLIENT_ID = "login-app";
   protected final static String CLIENT_SECRET = "55fd3447-7ae1-43e2-9390-c6de0dfff988";
   protected final static String USERNAME = "admin";
   protected final static String PASSWORD = "123";

//    protected Map<String, Message> heroes_map_mapper(String abcdef) {
//        Map<String, Message> map = null;
//        try {
//            map = new ObjectMapper().readValue(abcdef, new TypeReference<>() {
//            });
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return map;
//    }
    protected Message message_mapper(String msg) {
        Message message = null;
        try {
            message = new ObjectMapper().readValue(msg, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }

    protected Authentication authentication_mapper(String auth) {
        Authentication authentication = null;
        try {
            authentication = new ObjectMapper().readValue(auth, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return authentication;
    }
    protected String obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        params.put("scope", "openid user");
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, CLIENT_SECRET).and()
                .with().params(params).when().post(AUTH_SERVER + "/token");
        return response.jsonPath().getString("access_token");
    }
}
