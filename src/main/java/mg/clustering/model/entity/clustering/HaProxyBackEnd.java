package mg.clustering.model.entity.clustering;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HaProxyBackEnd {
    String name;
    String balance = "roundrobin";
    List<HaProxyServer> haProxyServers = new ArrayList<>();

    public HaProxyBackEnd(String name, String balance) {
        setName(name);
        setBalance(balance);
    }

    public HaProxyBackEnd() {
    }

    public static HaProxyBackEnd parseHaProxyBackEnd(String string) {
        HaProxyBackEnd haProxyBackEnd = new HaProxyBackEnd();
        String[] lines = string.split("\n");
        for (String line : lines) {
            line = line.trim();
            if(line.startsWith("backend")) {
                haProxyBackEnd.setName(line.substring(line.indexOf("backend")+7).trim());
            } else if(line.startsWith("balance")) {
                haProxyBackEnd.setBalance(line.substring(line.indexOf("balance")+7).trim());
            } else if(line.startsWith("server")) {
                String[] parts = line.split(" ");
                haProxyBackEnd.getHaProxyServers().add(new HaProxyServer(parts[1], parts[2].split(":")[0], parts[2].split(":")[1], parts.length == 4));
            }
        }
        return haProxyBackEnd;
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder("\nbackend " + getName() + "\n");
        if(getHaProxyServers().isEmpty())
            throw new RuntimeException("HaProxy back end must have at least one server");
        a.append("\tbalance ").append(getBalance()).append("\n");
        for (HaProxyServer haProxyServer : getHaProxyServers()) {
            a.append(haProxyServer.toString()).append("\n");
        }
        return a.toString();
    }

    public void addServer(HaProxyServer haProxyServer) {
        for (HaProxyServer server : getHaProxyServers()) {
            if(server.getName().equals(haProxyServer.getName()))
                throw new RuntimeException("Server " + haProxyServer.getName() + " already exists in back end " + getName());

        }
        getHaProxyServers().add(haProxyServer);
    }
}
