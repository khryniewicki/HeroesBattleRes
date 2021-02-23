package pl.com.khryniewicki.heroesbattle.GameSettings;


import pl.com.khryniewicki.heroesbattle.web.dto.Message;
import pl.com.khryniewicki.heroesbattle.web.restcontroller.ConnectionStatus;

public class HeroesBattleRestControllerSetup {
    protected Message fireWizard;
    protected Message iceWizard;
    protected Message iceWizard2;
    protected Message deregistered;

    protected Message icewizard2() {
        return new Message.Builder()
                .heroType("IceWizard")
                .sessionID("asdajsdoasdsadasda")
                .playerName("damian")
                .status(ConnectionStatus.CONNECTED)
                .build();
    }

    protected Message icewizard() {
        return new Message.Builder()
                .heroType("IceWizard")
                .playerName("radek")
                .sessionID("asdajsdoxzxczasda")
                .status(ConnectionStatus.CONNECTED)
                .build();
    }

    protected Message firewizard() {
        return new Message.Builder()
                .heroType("FireWizard")
                .sessionID("asdajsdoasda")
                .status(ConnectionStatus.CONNECTED)
                .playerName("konrad")
                .build();
    }

    protected Message deregisterIceWizard() {
        return new Message.Builder()
                .heroType("IceWizard")
                .playerName("radek")
                .sessionID("asdajsdoxzxczasda")
                .status(ConnectionStatus.DISCONNECTED)
                .build();
    }
}
