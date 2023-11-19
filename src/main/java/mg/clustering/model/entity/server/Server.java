package mg.clustering.model.entity.server;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.Utils;
import mg.clustering.model.entity.clustering.HaProxy;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@Entity
@Table(name = "server")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operating_system_id", nullable = false)
    private OperatingSystem operatingSystem;

    @Column(name = "ipv_4", unique = true, nullable = false)
    private String ipv4;

    @OneToMany(mappedBy = "server", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ServerApplication> serverApplications = new ArrayList<>();

    @OneToMany(mappedBy = "server", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TransfertMethod> transfertMethods = new ArrayList<>();

    @OneToOne(mappedBy = "server", orphanRemoval = true, fetch = FetchType.EAGER)
    private HaProxy haProxy;

    @Override
    public String toString() {
        return this.getName() + " (" + this.getIpv4() + ")";
    }

    public String getName() {
        if(this.name == null)
            return "Server unnamed";
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name: " + name);
        this.name = name;
    }

    public void setIpv4(String ipv4) {
        if (!Utils.isValidIpV4(ipv4))
            throw new IllegalArgumentException("Invalid IPv4: " + ipv4 + " for " + this);
        this.ipv4 = ipv4;
    }

    @Async
    public CompletableFuture<Boolean> isReachableAsync() {
        return Utils.INSTANCE.ping(getIpv4());
    }

    public boolean isReachable() {
        return Utils.INSTANCE.ping(getIpv4()).join();
    }

    public boolean isLoadBalancer() {
        return haProxy != null;
    }
}
