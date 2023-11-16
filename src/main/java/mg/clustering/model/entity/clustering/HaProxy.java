package mg.clustering.model.entity.clustering;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.entity.server.Server;

@Getter
@Setter
@Entity
@Table(name = "ha_proxy")
public class HaProxy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "server_id", nullable = false)
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
}