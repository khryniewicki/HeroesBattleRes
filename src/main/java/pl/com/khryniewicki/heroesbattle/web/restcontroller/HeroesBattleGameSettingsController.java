package pl.com.khryniewicki.heroesbattle.web.restcontroller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;
import pl.com.khryniewicki.heroesbattle.service.TimeService;
import pl.com.khryniewicki.heroesbattle.service.TokenService;
import pl.com.khryniewicki.heroesbattle.web.dto.Authentication;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;

import javax.annotation.PostConstruct;
import java.util.Timer;

@RestController
@RequestMapping(value = "/api/option")
@Slf4j
@CrossOrigin({"http://localhost:4200", "https://heroes.khryniewicki.pl", "https://heroes-battle.khryniewicki.pl"})
public class HeroesBattleGameSettingsController extends HeroesBattleRestControllerSupport {

    public static final String INVALID_CRENDENTIALS = "Invalid crendentials";
    public static final String GAME_STOPPED = "Game stopped";
    public static final String GAME_STARTED = "Game started";
    private TimeService timeService;

    @PostConstruct
    public void init() {
        initMap();
        timer = new Timer();
        timeService.setTimeStamp();
        scheduleClearingTime();
    }

    public HeroesBattleGameSettingsController(TokenService tokenService, RegisterService registerService, TimeService timeService) {
        super(tokenService, registerService, timeService);
        this.timeService = timeService;
    }

    @GetMapping("/check-role")
    public ResponseEntity<?> checkCrendentials(@AuthenticationPrincipal Jwt jwt) {
        Authentication authentication = new Authentication(isAdmin(jwt));
        System.out.println(authentication);
        return ResponseEntity.ok(authentication);
    }

    @GetMapping("/init-map")
    public ResponseEntity<?> initMapController(@AuthenticationPrincipal Jwt jwt) {
        if (isAdmin(jwt)) {
            initMap();
            return ResponseEntity.ok(new Message(GAME_STARTED));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CRENDENTIALS);
        }
    }

    @GetMapping("/stop-map")
    public ResponseEntity<?> stopMapController(@AuthenticationPrincipal Jwt jwt) {
        if (isAdmin(jwt)) {
            stopMap();
            return ResponseEntity.ok(new Message(GAME_STOPPED));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CRENDENTIALS);
        }
    }


    @GetMapping("/test")
    public ResponseEntity<?> test(@AuthenticationPrincipal Jwt jwt) {
        if (isAdmin(jwt)) {
            return ResponseEntity.ok(register_mock());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CRENDENTIALS);
        }
    }



    @GetMapping("/empty-room")
    public ResponseEntity<?> clearmap(@AuthenticationPrincipal Jwt jwt) {
        if (isAdmin(jwt)) {
            return ResponseEntity.ok((clear_map()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CRENDENTIALS);
        }
    }

    @GetMapping("/set-logout-time-in-seconds")
    public ResponseEntity<?> logoutTime(@AuthenticationPrincipal Jwt jwt, @RequestParam("time") Long time) {
        if (isAdmin(jwt)) {
            setLogoutTimeAfterGameStarts(time);
            return ResponseEntity.ok(new Message("Logout time changed to " + time + " sec."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CRENDENTIALS);
        }
    }

    @GetMapping("/set-logout-time-without-player-in-seconds")
    public ResponseEntity<?> logoutTime2(@AuthenticationPrincipal Jwt jwt, @RequestParam("time") Long time) {
        if (isAdmin(jwt)) {
            setLogoutTimeWithoutSecondPlayer(time);
            return ResponseEntity.ok(new Message("Logout time without player changed to " + time + " sec."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_CRENDENTIALS);
        }
    }


}
