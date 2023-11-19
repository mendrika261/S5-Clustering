package mg.clustering.model.entity.clustering;

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

    public HaProxyServer(String serverName, String serverDestination) {
        this.name = serverName;
        String[] parts = serverDestination.split(":");
        this.ip = parts[0];
        if(parts.length == 1)
            this.port = "80";
        else
            this.port = parts[1];
        this.autoCheck = false;
    }

    @Override
    public String toString() {
        return "\tserver " + getName() + " " + getIp() + ":" + getPort() + (isAutoCheck() ? " check" : "");
    }
}
