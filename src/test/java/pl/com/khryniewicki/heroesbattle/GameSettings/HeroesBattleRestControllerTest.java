package pl.com.khryniewicki.heroesbattle.GameSettings;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.com.khryniewicki.heroesbattle.ResourceServerApp;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;

import java.util.Map;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ResourceServerApp.class })
public class HeroesBattleRestControllerTest extends HeroesBattleRestControllerSetup {
    @Autowired
    private RegisterService registerService;
    private Map<String, Message> mapWithHeroes;

    @AfterEach
    void end() {
        mapWithHeroes.clear();
    }

    @Test
    public void registerOnePlayer() {
        //When
        mapWithHeroes=RegisterService.mapWithHeroes;
        registerService.registerPlayer(firewizard());
        int size = mapWithHeroes.size();
        //Then
        Assertions.assertEquals(1, size);
    }

    @Test
    public void registerTwoPlayers() {
        mapWithHeroes=RegisterService.mapWithHeroes;
        String channel1 = registerService.registerPlayer(firewizard());
        String channel2 = registerService.registerPlayer(icewizard());

        int size = mapWithHeroes.size();
        Message firstRegistration = (Message) mapWithHeroes.values().toArray()[0];
        Message secondRegistration = (Message) mapWithHeroes.values().toArray()[1];

        String firstChannel = firstRegistration.getChannel();
        String secondChannel = secondRegistration.getChannel();

        Assertions.assertEquals(2, size);
        Assertions.assertEquals(channel1, firstChannel);
        Assertions.assertEquals(channel2, secondChannel);

    }

    @Test
    public void registerTwoPlayersAndReregisterPlayer() {
        deregistered = deregisterIceWizard();
        fireWizard = firewizard();
        iceWizard = icewizard();
        iceWizard2 = icewizard2();
        mapWithHeroes=RegisterService.mapWithHeroes;

        //first_player_registration
        String channel1 =registerService.registerPlayer(iceWizard);
        Assertions.assertEquals("1", channel1);
        //second_player_registration

        String channel2 =registerService.registerPlayer(fireWizard);
        Assertions.assertEquals("2", channel2);

        //first_player_deregistration
        registerService.clearExistingPlayer(deregistered);
        Message second = mapWithHeroes.get(fireWizard.getSessionID());
        String secondChannel = second.getChannel();
        Assertions.assertEquals("2", secondChannel);

        //third_player_registration_to_first_channel
        String channel1_again = registerService.registerPlayer(iceWizard2);
        Assertions.assertEquals("1", channel1_again);

        Message first = mapWithHeroes.get(iceWizard2.getSessionID());
        String firstChannel = first.getChannel();
        Assertions.assertEquals("1", firstChannel);

        //two_players_logged
        int size = mapWithHeroes.size();
        Assertions.assertEquals(2, size);
    }
}
