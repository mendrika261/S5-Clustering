package mg.clustering.model.entity.deployment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.ServerApplication;
import mg.clustering.model.entity.server.TransfertMethod;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "deployment")
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "deployment_id", nullable = false)
    private Build build;

    @ManyToOne
    @JoinColumn(name = "config_file_id")
    private ConfigFile configFile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_application_id", nullable = false)
    private ServerApplication serverApplication;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transfert_method_id", nullable = false)
    private TransfertMethod transfertMethod;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeploymentStatus status = DeploymentStatus.PENDING;

    @Column(name = "deployment_message")
    private String deploymentMessage;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime = LocalDateTime.now();
}