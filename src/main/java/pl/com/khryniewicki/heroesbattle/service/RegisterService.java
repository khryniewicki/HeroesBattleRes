package pl.com.khryniewicki.heroesbattle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.khryniewicki.heroesbattle.web.dto.Message;
import pl.com.khryniewicki.heroesbattle.web.restcontroller.ConnectionStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterService {
    public static final String ROOM_CLEARED = "Room cleared, no players";
    private static final String PLAYERES_REGISTERED = "{} player with sessionid {} registered";
    private static final String ALL_PLAYERS_IN_GAME = "All players in game";
    private static final String HERO_DISCONNECTED = "Hero with sessionID {} disconnected";
    private static final String ROOM_STARTED = "Room started";
    private static final String ROOM_STOPPED = "Room stopped";
    public static final String MOCK_REGISTERED = "Mock registered: %s";
    public static Map<String, Message> mapWithHeroes;

    private final TimeService timeService;

    public String registerPlayer(Message message) {
        String channel;
        if (isMapWithHeroesInitiatied()) {
            if (mapWithHeroes.size() == 0) {
                channel = "1";
                timeService.first_player_waiting();
                registration(message, channel);
                return channel;
            } else if (mapWithHeroes.size() == 1) {
                channel = getChannel();
                registration(message, channel);
                timeService.first_player_not_waiting();
                return channel;
            } else {
                return ALL_PLAYERS_IN_GAME;
            }
        } else {
            return ROOM_STOPPED;
        }
    }


    public boolean isMapWithHeroesInitiatied() {
        return Objects.nonNull(mapWithHeroes);
    }

    public void initMap() {
        mapWithHeroes = new HashMap<>();
        log.info(ROOM_STARTED);
    }

    public void stopMap() {
        mapWithHeroes = null;
        log.info(ROOM_STOPPED);
    }

    public String getChannel() {
        String channelOccupied = null;
        for (Message message2 : mapWithHeroes.values()) {
            channelOccupied = message2.getChannel();
        }
        if (channelOccupied != null && channelOccupied.equals("2")) {
            return "1";
        } else {
            return "2";
        }
    }

    public void registration(Message message, String channel) {
        mapWithHeroes.put(message.getSessionID(), new Message.Builder()
                .channel(channel)
                .heroType(message.getContent())
                .sessionID(message.getSessionID())
                .status(message.getStatus())
                .playerName(message.getPlayerName())
                .build());
        log.info(PLAYERES_REGISTERED, channel, message.getSessionID());
    }

    public void clearExistingPlayer(Message message) {
        String sessionID = message.getSessionID();

        if (isMapWithHeroesInitiatied() && mapWithHeroes.containsKey(sessionID)) {
            mapWithHeroes.remove(sessionID);
            timeService.first_player_waiting();
            log.info(HERO_DISCONNECTED, sessionID);
        }
    }

    public Message mock_player() {
        return new Message.Builder()
                .status(ConnectionStatus.CONNECTED)
                .sessionID("TESTING SESSION")
                .heroType("Fire Wizard")
                .playerName("MANEKIN")
                .build();
    }

    public Message clear_map() {
        if (isMapWithHeroesInitiatied()) {
            timeService.clear_map();
            return new Message(ROOM_CLEARED);
        } else {
            return new Message(ROOM_STOPPED);
        }
    }

    public Message register_mock() {
        if (isMapWithHeroesInitiatied()) {
            timeService.clear_map();
            Message mock = mock_player();
            registerPlayer(mock);
            String text = String.format(MOCK_REGISTERED, mock.getPlayerName());
            return new Message(text);
        } else {
            return new Message(ROOM_STOPPED);
        }
    }
}
