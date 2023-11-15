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

    public static String[] getExpressions() {
        ConfigFileType[] configFileTypes = ConfigFileType.values();
        String[] regexCondition = new String[configFileTypes.length];
        for (int i=0; i<configFileTypes.length; i++) {
            regexCondition[i] = configFileTypes[i].getExpression();
        }
        return regexCondition;
    }
}
