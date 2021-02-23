package pl.com.khryniewicki.heroesbattle.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.khryniewicki.heroesbattle.utils.Now;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

@Service
@Getter
@Setter
@Slf4j
public class TimeService {
    public static final String SECOND_PLAYER_DO_NOT_ENTER = "Second player did not enter, room cleared, {} minutes left";
    public static final String MAP_CLEAR_AFTER_TIME = "Room cleared after {} minutes";
    private static final String TIME_LEFT_TO_LOG_OUT = "Time left to log out: ";
    public ZonedDateTime timeStamp;
    public long logoutTimeWithoutSecondPlayer = 3 * 60;
    public long logoutTimeAfterGameStarts = 15 * 60;
    private boolean firstPlayerWaiting;

    public void clear_algorithm() {
        long subtract = time_left_to_log_out_waiting_for_second_player();
        long subtract2 = time_left_after_game_starts();

        log.info(TIME_LEFT_TO_LOG_OUT+format_time(time_to_log_out()));
        if (subtract < 0 && firstPlayerWaiting) {
            clear_map();
            firstPlayerWaiting = false;
            log.info(SECOND_PLAYER_DO_NOT_ENTER, format_time(logoutTimeWithoutSecondPlayer));
        }
        if (subtract2 < 0) {
            clear_map();
            log.info(MAP_CLEAR_AFTER_TIME, format_time(logoutTimeAfterGameStarts));
        }
    }

    public void first_player_waiting() {
        setFirstPlayerWaiting(true);
        setTimeStamp();
    }

    public void first_player_not_waiting() {
        setFirstPlayerWaiting(false);
        setTimeStamp();
    }

    public void clear_map() {
        RegisterService.mapWithHeroes.clear();
        setTimeStamp();
    }

    private long time_left_after_game_starts() {
        ZonedDateTime zonedDateTime = getTimeStamp().plusSeconds(logoutTimeAfterGameStarts);
        return Duration.between(now(), zonedDateTime).getSeconds();
    }

    public long time_left_to_log_out_waiting_for_second_player() {
        ZonedDateTime zonedDateTime = getTimeStamp().plusSeconds(logoutTimeWithoutSecondPlayer);
        return Duration.between(now(), zonedDateTime).getSeconds();
    }

    public void setTimeStamp() {
        this.timeStamp = now();
    }

    public ZonedDateTime now() {
        Instant instant = Now.instant();
        return Now.zonedDateTime(instant);
    }

    public static String format_time(long miliseconds) {
        long minutes = miliseconds / (60);
        long seconds = miliseconds % 60;
        return String.format("%d minutes %d sec", minutes, seconds);
    }

    public long time_to_log_out() {
        return isFirstPlayerWaiting() ? time_left_to_log_out_waiting_for_second_player() : time_left_after_game_starts();
    }

    public ZonedDateTime getTimeStamp() {
        return Objects.nonNull(timeStamp) ? timeStamp : now();
    }
}
