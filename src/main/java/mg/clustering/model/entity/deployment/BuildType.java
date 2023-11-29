package mg.clustering.model.entity.deployment;

import lombok.Getter;

@Getter
public enum BuildType {
    MAVEN("maven"),
    MANUAL("manual");

    final String name;

    BuildType(String name) {
        this.name = name;
    }
}
