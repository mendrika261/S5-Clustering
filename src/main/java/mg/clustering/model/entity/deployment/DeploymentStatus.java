package mg.clustering.model.entity.deployment;

import lombok.Getter;

@Getter
public enum DeploymentStatus {
  DEPLOYING("Deploying"),
  DEPLOYED("Deployed"),
  FAILED("Failed"),
  PENDING("Pending");

  final String name;

  DeploymentStatus(String name) { this.name = name; }
}
