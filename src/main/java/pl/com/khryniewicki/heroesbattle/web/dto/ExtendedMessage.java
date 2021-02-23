package pl.com.khryniewicki.heroesbattle.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ExtendedMessage implements Serializable {
    private Map<String, Message> heroesMap;
    private long timeLeft;

    public ExtendedMessage(Map<String, Message> heroesMap, long timeLeft) {
        this.heroesMap = heroesMap;
        this.timeLeft = timeLeft;
    }
}
