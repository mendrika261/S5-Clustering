package mg.clustering.model.entity.deployment;

import lombok.Getter;

@Getter
public enum ConfigFileType {
    ENV(".*\\.env"),
    PROPERTIES(".*\\.properties"),
    YAML(".*\\.yml"),
    XML(".*\\.xml"),
    JSON(".*\\.json");

    final String expression;

    ConfigFileType(String expression) {
        this.expression = expression;
    }
}
