package pl.com.khryniewicki.heroesbattle.web.restcontroller;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;
import pl.com.khryniewicki.heroesbattle.service.TimeService;
import pl.com.khryniewicki.heroesbattle.service.TokenService;
import pl.com.khryniewicki.heroesbattle.web.dto.ExtendedMessage;
import pl.com.khryniewicki.heroesbattle.web.dto.HeroDTO;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;
import pl.com.khryniewicki.heroesbattle.web.dto.SpellDTO;

import java.util.Map;


@RestController
@Slf4j
@Getter
@Setter
@RequestMapping(value = "/game")
@CrossOrigin({"http://localhost:4200", "http://localhost:8445", "https://heroes-battle.khryniewicki.pl"})
public class HeroesBattleRestController extends HeroesBattleRestControllerSupport {
    public HeroesBattleRestController(TokenService tokenService, RegisterService registerService, TimeService timeService) {
        super(tokenService, registerService, timeService);
    }

    @MessageMapping("/hero/{id}")
    @SendTo("/topic/hero/{id}")
    public HeroDTO getCoordinates(HeroDTO heroDTO, @DestinationVariable("id") String id) {
        log.info("Hero" + id + ": x :{},y {}", heroDTO.getPositionX(), heroDTO.getPositionY());
        return heroDTO;
    }

    @MessageMapping("/spell/{id}")
    @SendTo("/topic/spell/{id}")
    public SpellDTO getSpell(SpellDTO spellDTO, @DestinationVariable("id") String id) {
        log.info("Spell" + id + ":" + spellDTO.toString());
        return spellDTO;
    }

    @MessageMapping("/room")
    @SendTo("/topic/room")
    public Message action(Message message) {
        log.info(message.toString());
        String channel;
        if (message.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            clearExistingPlayer(message);
            return message;
        } else {
            channel = registerPlayer(message);
        }
        return new Message.Builder()
                .channel(channel)
                .build();
    }

    @GetMapping("/time-left-to-log-in")
    public Long time_to_log_in() {
        long subtract = time_left_to_log_out_waiting_for_second_player();
        return subtract > 0 ? subtract : 0;
    }

    @GetMapping("/time-left")
    public Long time_left() {
        return get_time_left();
    }

    @GetMapping("/map")
    public Map<String, Message> get_map() {
        return getMap();
    }

    @GetMapping("/extended-message")
    public ExtendedMessage get_extended_message() {
        return new ExtendedMessage(getMap(), get_time_left());
    }

}
