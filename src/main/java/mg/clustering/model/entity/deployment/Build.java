package mg.clustering.model.entity.deployment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "build")
public class Build {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "build_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BuildType buildType;

    @Column(name = "build_file")
    private String buildFile;

    @Column(name = "artifact_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "artifact_file")
    private String artifactFile;
}