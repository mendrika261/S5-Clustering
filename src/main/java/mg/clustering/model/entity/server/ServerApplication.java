package mg.clustering.model.entity.server;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.Utils;

@Getter
@Setter
@Entity
@Table(name = "server_application")
public class ServerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_application_type_id", nullable = false)
    private ServerApplicationType serverApplicationType;

    @Column(name = "deployment_path", nullable = false)
    private String deploymentPath;

    @Column(name = "port", nullable = false)
    private String port;

    @Override
    public String toString() {
        return this.getServerApplicationType() + " on " + this.getServer();
    }

    public void setPort(String port) {
        if (!Utils.isValidPort(port))
            throw new IllegalArgumentException("Invalid port: " + port + " for " + this);
        this.port = port;
    }
}