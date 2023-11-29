package mg.clustering.model.core;

import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.entity.deployment.Build;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MavenBuild implements BuildSystem {
    List<String[]> commands = new ArrayList<>();
    public static String ARTIFACT_PATH = "target/";

    public void build(Build build) {
        commands.add(new String[]{"mvn", "clean", "install", "-DskipTests"});

        for (String[] command : getCommands())
            if(!Utils.execute(command, Utils.REPOSITORY_PATH + build.getRepository()))
                throw new RuntimeException("Error while building maven project");
    }
}
