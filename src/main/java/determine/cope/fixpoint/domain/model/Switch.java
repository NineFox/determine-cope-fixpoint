package determine.cope.fixpoint.domain.model;

import java.net.InetAddress;
import java.util.List;

import lombok.Value;

@Value
public class Switch {

    private InetAddress ip;

    private List<Server> servers;

    public StatusSwitch getStatus() {

        if (servers.stream().anyMatch(server -> server.getStatus().equals(StatusServer.NORMAL))) {
            return StatusSwitch.NORMAL;
        } else {
            return StatusSwitch.ABNORMAL;
        }
    }
}
