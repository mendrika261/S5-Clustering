package mg.clustering.model.entity.clustering;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.SSHTransfert;
import mg.clustering.model.core.Transfert;
import mg.clustering.model.core.Utils;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.TransfertMethod;

@Getter
@Setter
@Entity
@Table(name = "ha_proxy")
public class HaProxy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "server_id")
    private Server server;

    @Column(name = "config_file", nullable = false)
    private String configFile;

    public void setConfigFile(String configFile) {
        if (configFile == null || configFile.isEmpty())
            throw new RuntimeException("Config file cannot be empty");
        if(!configFile.endsWith(".cfg"))
            throw new RuntimeException("Config file must end with .cfg");
        this.configFile = configFile;
    }

    public HaProxyConfigFile getHaProxyCfg() {
        if (getServer().getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + getServer());
        TransfertMethod transfertMethod = getServer().getTransfertMethods().get(0);
        try {
            Transfert transfert = new SSHTransfert(getServer().getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());
            String path = Utils.CONFIG_PATH + getId() + "-haproxy.cfg";
            transfert.download(getConfigFile(), path);
            String content = Utils.getFileContent(path);
            return HaProxyConfigFile.parseHaProxyConfigFile(content);
        } catch (Exception e) {
            throw new RuntimeException("Error while getting HAProxy config file via " + transfertMethod + " : " + e.getMessage());
        }
    }
}