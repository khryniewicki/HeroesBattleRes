package pl.com.khryniewicki.heroesbattle.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.khryniewicki.heroesbattle.web.restcontroller.ConnectionStatus;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class Message implements Serializable {
    private String channel;
    private String content;
    private String sessionID;
    private String playerName;
    private ConnectionStatus status;

    public Message(Builder builder) {
        this.channel = builder.channel;
        this.content = builder.content;
        this.sessionID = builder.sessionID;
        this.playerName = builder.playerName;
        this.status = builder.status;
    }


    public Message(String content) {
        this.content = content;
    }

    public static class Builder {
        private String channel;
        private String content;
        private String sessionID;
        private String playerName;
        private ConnectionStatus status;

        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder heroType(String heroType) {
            this.content = heroType;
            return this;
        }

        public Builder sessionID(String sessionID) {
            this.sessionID = sessionID;
            return this;
        }

        public Builder playerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public Builder status(ConnectionStatus status) {
            this.status = status;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", playerName='" + playerName + '\'' +
                ", status=" + status +
                '}';
    }
}
