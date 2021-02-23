package pl.com.khryniewicki.heroesbattle.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Authentication implements Serializable {
    private boolean credentials;

    public Authentication(boolean credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "credentials=" + credentials +
                '}';
    }
}
