package mg.clustering.model.entity.server;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "operating_system")
public class OperatingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "os_type")
    @Enumerated(EnumType.STRING)
    private OperatingSystemType operatingSystemType;

    @Column(name = "script_extension", nullable = false)
    private String scriptExtension;

    @Override
    public String toString() {
        return getOperatingSystemType() + " - " + getName();
    }
}