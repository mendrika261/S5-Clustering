package mg.clustering.model.entity.server;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "application_server", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "version"})
})
public class ServerApplicationType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private String version;

    @Override
    public String toString() {
        return this.getName() + " (" + this.getVersion() + ")";
    }
}