package mg.clustering.model.entity.deployment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HaProxyServer {
    String name;
    String ip;
    String port;
    boolean autoCheck;

    public HaProxyServer(String name, String ip, String port, boolean autoCheck) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.autoCheck = autoCheck;
    }

    @Override
    public String toString() {
        return "\tserver " + getName() + " " + getIp() + ":" + getPort() + (isAutoCheck() ? " check" : "");
    }
}
