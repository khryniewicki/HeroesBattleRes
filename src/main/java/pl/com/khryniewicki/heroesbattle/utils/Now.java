package pl.com.khryniewicki.heroesbattle.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;

public class Now {

    private static ThreadLocal<Clock> clock =
            ThreadLocal.withInitial(Clock::systemDefaultZone);


    public static void setClock(Clock clock) {
        Now.clock.set(clock);
    }

    public static Instant instant() {
        return Instant.now(clock.get());
    }

    public static ZonedDateTime zonedDateTime(Instant instant) {
        return instant.atZone(Clock.systemDefaultZone().getZone());
    }
}
