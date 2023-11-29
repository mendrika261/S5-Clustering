package mg.clustering.model.entity.server;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.Utils;

@Getter
@Setter
@Entity
@Table(name = "transfert_method", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"server_id", "type", "username", "password"})
})
public class TransfertMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransfertMethodType transfertMethodType;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    @Lob
    private String password;

    @Column(name = "port", nullable = false)
    private String port;

    @Override
    public String toString() {
        return this.getTransfertMethodType().getName() + " (" + this.getUsername() + "@" + this.getServer().getIpv4() + ":" + this.getPort() + ")";
    }

    public void setPort(String port) {
        if (!Utils.isValidPort(port))
            throw new IllegalArgumentException("Invalid port: " + port + " for " + this);
        this.port = port;
    }
}