package pl.com.khryniewicki.heroesbattle.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class SpellDTO implements Serializable {
    String name;
    Float targetSpellX;
    Float targetSpellY;


    @Override
    public String toString() {
        return "SpellDTO{" +
                "name='" + name + '\'' +
                ", targetSpellX=" + targetSpellX +
                ", targetSpellY=" + targetSpellY +
                '}';
    }
}
