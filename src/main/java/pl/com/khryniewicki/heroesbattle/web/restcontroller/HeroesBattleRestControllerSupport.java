package pl.com.khryniewicki.heroesbattle.web.restcontroller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;
import pl.com.khryniewicki.heroesbattle.service.TimeService;
import pl.com.khryniewicki.heroesbattle.service.TokenService;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
public class HeroesBattleRestControllerSupport {

    protected Timer timer;
    public final TokenService tokenService;
    private final RegisterService registerService;
    private final TimeService timeService;

    //    ROLE_CHECKING
    protected boolean isAdmin(@AuthenticationPrincipal Jwt jwt) {
        return tokenService.isAdmin(jwt);
    }

   public void initMap() {
        registerService.initMap();
    }

    public void stopMap() {
        registerService.stopMap();
    }

    public Map<String, Message> getMap(){
       return RegisterService.mapWithHeroes;
    }

    public String registerPlayer(Message message) {
        return registerService.registerPlayer(message);
    }

    protected void clearExistingPlayer(Message message) {
        registerService.clearExistingPlayer(message);
    }

    protected void setLogoutTimeAfterGameStarts(long l) {
        timeService.setLogoutTimeAfterGameStarts(l);
    }

    protected void setLogoutTimeWithoutSecondPlayer(long l) {
        timeService.setLogoutTimeWithoutSecondPlayer(l);
    }

    protected long time_left_to_log_out_waiting_for_second_player(){
        return timeService.time_left_to_log_out_waiting_for_second_player();
    }
    void scheduleClearingTime() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeService.clear_algorithm();
            }
        }, 0, 1000);
    }

    protected Long get_time_left() {
        return timeService.time_to_log_out();
    }

    protected Message register_mock() {
        return registerService.register_mock();

    }

    protected Message clear_map(){
        return registerService.clear_map();
    }

}
