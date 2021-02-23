package pl.com.khryniewicki.heroesbattle.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class HeroDTO implements Serializable {
    private String name;
    private Integer life;
    private Integer mana;
    private Float positionX;
    private Float positionY;


    @Override
    public String toString() {
        return " { " +
                "name=[ " + name  +
                "], life=[ " + life +
                "], mana=[ " + mana +
                "], X=[ " + positionX +
                "], Y=[ " + positionY +
                " }";
    }
}
