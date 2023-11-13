package mg.clustering.model.entity.deployment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "config_file")
public class ConfigFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "fileType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigFileType fileType;

    @Column(name = "content", nullable = false)
    private String content;
}