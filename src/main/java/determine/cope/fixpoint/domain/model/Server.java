package determine.cope.fixpoint.domain.model;

import java.net.InetAddress;
import java.time.Duration;

import lombok.Value;

@Value
public class Server {

    private Duration downTime;

    private InetAddress ip;

    private StatusServer status;

}
