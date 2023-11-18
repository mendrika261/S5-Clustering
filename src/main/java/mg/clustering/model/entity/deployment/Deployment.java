package mg.clustering.model.entity.deployment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.*;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.ServerApplication;
import mg.clustering.model.entity.server.TransfertMethod;
import mg.clustering.model.entity.server.TransfertMethodType;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "deployment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"deployment_id", "server_id", "server_application_id", "transfert_method_id"})
})
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "deployment_id", nullable = false)
    private Build build;

    @Column(name = "build_file")
    private String buildFile;

    @Column(name = "artifact_file")
    private String artifactFile;

    @ManyToOne
    @JoinColumn(name = "config_file_id")
    private ConfigFile configFile;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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

    public String getArtifactFile() {
        ConfigFile artifact;
        try {
            return switch (getBuild().getBuildType()) {
                case BuildType.MAVEN -> {
                    String s = Utils.REPOSITORY_PATH + getBuild().getRepository() + "/" + MavenBuild.ARTIFACT_PATH;
                    List<ConfigFile> c = Utils.INSTANCE.scanConfigFilesIn(new String[]{".*\\." + getBuild().getArtifactType().getExtension()}, s, false);
                    if(c.isEmpty())
                        throw new RuntimeException("Build failed");
                    artifact = c.getFirst();
                    yield artifact.getName();
                }
                default -> throw new IllegalStateException("Unexpected value: " + getBuild().getBuildType());
            };
        } catch (IOException e) {
            throw new RuntimeException("Artifact not found, maybe the build failed");
        }
    }

    public String getLink() {
        String artifactname = getArtifactFile().substring(getArtifactFile().lastIndexOf('/') + 1, getArtifactFile().lastIndexOf('.'));
        return "http://" + getServer().getIpv4() + ":" + getServerApplication().getPort() + "/" + artifactname;
    }

    public void deploy() {
        setStatus(DeploymentStatus.DEPLOYING);
        Transfert transfer;
        switch (getTransfertMethod().getTransfertMethodType()) {
            case TransfertMethodType.SSH ->
                    transfer = new SSHTransfert(
                            getServer().getIpv4(), getTransfertMethod().getUsername(),
                            getTransfertMethod().getPassword(), getTransfertMethod().getPort());
            case TransfertMethodType.FTP ->
                    transfer = new FTPTransfert(
                            getServer().getIpv4(), getTransfertMethod().getUsername(),
                            getTransfertMethod().getPassword(), getTransfertMethod().getPort());
            default -> throw new IllegalStateException("Transfert method " + getTransfertMethod() + " not implemented yet");
        }
        transfer.upload(getArtifactFile(), getServerApplication().getDeploymentPath());
        setStatus(DeploymentStatus.DEPLOYED);
    }
}