package pl.com.khryniewicki.heroesbattle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TokenService {
    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";
    public static final String ADMIN = "admin";

    public boolean isAdmin(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realm_access = jwt.getClaimAsMap(REALM_ACCESS);
        Object roles1 = realm_access.get(ROLES);
        List<String> roles = new ArrayList<>();
        try {
            roles = new ObjectMapper().readValue(roles1.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return roles.stream().anyMatch(r -> r.equals(ADMIN));
    }
}
