package mg.clustering.model.entity.deployment;

import lombok.Getter;

@Getter
public enum ArtifactType {
  JAR("jar"),
  WAR("war"),
  EAR("ear");

  final String extension;

  ArtifactType(String extension) { this.extension = extension; }
}
