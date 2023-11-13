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

    @Column(name = "fileType")
    @Enumerated(EnumType.STRING)
    private ConfigFileType fileType;

    @Column(name = "content", nullable = false)
    private String content;

    public ConfigFile() {
    }

    public ConfigFile(String name, String content) {
        setName(name);
        setContent(content);
    }

    public ConfigFileType getFileType() {
        if(getName() == null)
            return null;
        return switch (getName().substring(getName().lastIndexOf(".") + 1)) {
            case "properties" -> ConfigFileType.PROPERTIES;
            case "xml" -> ConfigFileType.XML;
            case "yaml" -> ConfigFileType.YAML;
            case "env" -> ConfigFileType.ENV;
            case "json" -> ConfigFileType.JSON;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return "(" + getFileType() +" file) " + getName() + " : " + getContent().substring(0, 20) + "...";
    }
}