package pl.com.khryniewicki.heroesbattle.GameSettings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.com.khryniewicki.heroesbattle.ResourceServerApp;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;
import pl.com.khryniewicki.heroesbattle.service.TimeService;
import pl.com.khryniewicki.heroesbattle.utils.Now;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ResourceServerApp.class})
public class HeroesBattleTimeRestControllerTest extends HeroesBattleRestControllerSetup {

    @Test
    public void should_register_player_and_clear_when_no_one_log_in_within_logoutTimeWithoutSecondPlayer(@Autowired RegisterService registerService, @Autowired TimeService timeService) {
//      SET CLOCK AND CHECK IF MAP IS CLEARED
        Clock baseClock = Clock.systemDefaultZone();
        Clock clock = Clock.offset(baseClock, Duration.ZERO);
        Now.setClock(clock);
        Map<String, Message> mapWithHeroes = RegisterService.mapWithHeroes;
        mapWithHeroes.clear();
        int size = mapWithHeroes.size();
        Assertions.assertEquals(0, size);

//      REGISTER FIRST PLAYER

        registerService.registerPlayer(firewizard());
        size = mapWithHeroes.size();
        Assertions.assertEquals(1, size);
        timeService.clear_algorithm();
        Assertions.assertEquals(1, size);

//      CHECK TIME AFTER 2 MINUTES
        int offset=2;
        clock = Clock.offset(baseClock, Duration.ofMinutes(offset));
        Now.setClock(clock);
        timeService.clear_algorithm();
        long abs = Math.abs(timeService.time_to_log_out() - (timeService.getLogoutTimeWithoutSecondPlayer() - offset * 60));
        Assertions.assertTrue(abs <= 1);
//      DEREGISTER FIRST PLAYER IF NO ONE ENTERED WITHIN TIME : logoutTimeWithoutSecondPlayer

        clock = Clock.offset(baseClock, Duration.ofMinutes(4));
        Now.setClock(clock);
        timeService.clear_algorithm();
        size = mapWithHeroes.size();
        Assertions.assertEquals(0, size);
    }

    @Test
    public void should_register_two_players_and_clear_when_time_reaches_logoutTimeAfterGameStarts(@Autowired RegisterService registerService, @Autowired TimeService timeService) {
//      SET CLOCK AND CHECK IF MAP IS CLEARED
        Clock baseClock = Clock.systemDefaultZone();
        Clock clock = Clock.offset(baseClock, Duration.ZERO);
        Now.setClock(clock);
        Map<String, Message> mapWithHeroes = RegisterService.mapWithHeroes;
        mapWithHeroes.clear();
        int size = mapWithHeroes.size();
        Assertions.assertEquals(0, size);

//      REGISTER FIRST PLAYER
        registerService.registerPlayer(firewizard());
        size = mapWithHeroes.size();
        Assertions.assertEquals(1, size);
        timeService.clear_algorithm();
        Assertions.assertEquals(1, size);

        //      REGISTER SECOND PLAYER AFTER 2 MINUTES
        clock = Clock.offset(baseClock, Duration.ofMinutes(2));
        Now.setClock(clock);
        registerService.registerPlayer(icewizard());
        size = mapWithHeroes.size();
        Assertions.assertEquals(2, size);
        timeService.clear_algorithm();
        Assertions.assertEquals(2, size);

        //      SHOULD NOT CLEAR, BECAUSE BOTH PLAYERS ARE REGISTERED
        clock = Clock.offset(baseClock, Duration.ofMinutes(4));
        Now.setClock(clock);
        timeService.clear_algorithm();
        size = mapWithHeroes.size();
        Assertions.assertEquals(2, size);

//      KICK OFF PLAYERS AFTER TIME: logoutTimeAfterGameStarts
        clock = Clock.offset(baseClock, Duration.ofMinutes(17));
        Now.setClock(clock);
        timeService.clear_algorithm();
        size = mapWithHeroes.size();
        Assertions.assertEquals(0, size);
    }

    @Test
    public void should_register_two_players_then_one_deregister_and_clear_when_time_reaches_logoutTimeAfterGameStarts(@Autowired RegisterService registerService, @Autowired TimeService timeService) {
//      SET CLOCK AND CHECK IF MAP IS CLEARED
        Clock baseClock = Clock.systemDefaultZone();
        Clock clock = Clock.offset(baseClock, Duration.ZERO);
        Now.setClock(clock);
        Map<String, Message> mapWithHeroes = RegisterService.mapWithHeroes;
        mapWithHeroes.clear();
        int size = mapWithHeroes.size();
        Assertions.assertEquals(0, size);

//      REGISTER FIRST PLAYER
        registerService.registerPlayer(firewizard());
        size = mapWithHeroes.size();
        Assertions.assertEquals(1, size);
        timeService.clear_algorithm();
        Assertions.assertEquals(1, size);
        long abs = Math.abs(timeService.time_to_log_out() - (timeService.getLogoutTimeWithoutSecondPlayer()));
        Assertions.assertTrue(abs <= 1);

        //      REGISTER SECOND PLAYER AFTER 2 MINUTES
        clock = Clock.offset(baseClock, Duration.ofMinutes(2));
        Now.setClock(clock);
        registerService.registerPlayer(icewizard());
        size = mapWithHeroes.size();
        Assertions.assertEquals(2, size);
        timeService.clear_algorithm();
        Assertions.assertEquals(2, size);
        abs = Math.abs(timeService.time_to_log_out() - (timeService.getLogoutTimeAfterGameStarts()));
        Assertions.assertTrue(abs <= 1);

        //      SHOULD NOT CLEAR, BECAUSE BOTH PLAYERS ARE REGISTERED
        clock = Clock.offset(baseClock, Duration.ofMinutes(4));
        Now.setClock(clock);
        timeService.clear_algorithm();
        size = mapWithHeroes.size();
        Assertions.assertEquals(2, size);
        abs = Math.abs(timeService.time_to_log_out() - (timeService.getLogoutTimeAfterGameStarts() - 2 * 60));
        Assertions.assertTrue(abs <= 1);

        //     SHOULD DEREGISTER PLAYER
        clock = Clock.offset(baseClock, Duration.ofMinutes(8));
        Now.setClock(clock);
        registerService.clearExistingPlayer(deregisterIceWizard());
        timeService.clear_algorithm();
        size = mapWithHeroes.size();
        Assertions.assertEquals(1, size);

//      KICK OFF PLAYERS AFTER TIME: logoutTimeWithoutSecondPlayer
        clock = Clock.offset(baseClock, Duration.ofMinutes(12));
        Now.setClock(clock);
        timeService.clear_algorithm();
        size = mapWithHeroes.size();
        Assertions.assertEquals(0, size);
        abs = Math.abs(timeService.time_to_log_out() - (timeService.getLogoutTimeAfterGameStarts()));
        Assertions.assertTrue(abs <= 1);

    }


}
