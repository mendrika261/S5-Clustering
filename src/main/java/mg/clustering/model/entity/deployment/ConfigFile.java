package mg.clustering.model.entity.deployment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private ConfigFileType fileType;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    public ConfigFile() {
    }

    public ConfigFile(String name) {
        setName(name);
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

    public String getFileName() {
        int start = getName().lastIndexOf("/") == -1 ? 0 : getName().lastIndexOf("/")+1;
        return getName().substring(start);
    }

    @Override
    public String toString() {
        return "(" + getFileType() +" file) " + getName() + " : " + getContent().substring(0, 20) + "...";
    }

    public void writeFile() {
        Utils.writeFile(getName(), getContent());
    }
}