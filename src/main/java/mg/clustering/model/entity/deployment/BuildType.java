package mg.clustering.model.entity.deployment;

import lombok.Getter;

@Getter
public enum BuildType {
  MAVEN("maven"),
  GRADLE("gradle"),
  SCRIPT("script"),
  COMMAND("command");

  final String name;

  BuildType(String name) { this.name = name; }
}
