package determine.cope.fixpoint.domain.model;

import java.net.InetAddress;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Value;

@Value
@JsonPropertyOrder({ "logDateTime", "ip", "pingResponseTime" })
public class MonitoringLog {

    @JsonProperty("logDateTime")
    private LocalDateTime logDateTime;

    @JsonProperty("ip")
    private InetAddress ip;

    @JsonProperty("pingResponseTime")
    private String pingResponseTime;
}
