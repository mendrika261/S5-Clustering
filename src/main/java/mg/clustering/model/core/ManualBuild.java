package mg.clustering.model.core;

import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.entity.deployment.Build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ManualBuild implements BuildSystem {
    List<String[]> commands = new ArrayList<>();
    public static String ARTIFACT_PATH = "target/";

    public void build(Build build) {
        String outPath = Utils.REPOSITORY_PATH + build.getRepository() + "/" + ARTIFACT_PATH + "WEB-INF/classes";
        try {
            new Compiler(Utils.REPOSITORY_PATH + build.getRepository() + "/" + build.getSrcPath(), outPath, outPath).compile();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while compiling source files: "+ e.getMessage());
        }
        // copy webapp
        Utils.copyDirectory(Utils.REPOSITORY_PATH + build.getRepository() + "/" + build.getWebappPath() + "/", Utils.REPOSITORY_PATH + build.getRepository() + "/" + ARTIFACT_PATH);
        // copy web.xml
        Utils.copyDirectory(Utils.REPOSITORY_PATH + build.getRepository() + "/" + build.getWebXmlPath(), Utils.REPOSITORY_PATH + build.getRepository() + "/" + ARTIFACT_PATH + "WEB-INF");
        commands.add(new String[]{"jar", "-cvf", build.getRepository()+"."+build.getArtifactType().getExtension(), "."});
        for (String[] command : getCommands())
            if(!Utils.execute(command, Utils.REPOSITORY_PATH + build.getRepository() + "/" + ARTIFACT_PATH))
                throw new RuntimeException("Error while building war file");
    }
}
