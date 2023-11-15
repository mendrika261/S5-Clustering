package mg.clustering.model.entity.server;

import lombok.Getter;

@Getter
public enum OperatingSystemType {
  WINDOWS("Windows"),
  LINUX("Linux"),
  UNIX("Unix");

  final String name;

  OperatingSystemType(String name) { this.name = name; }
}
