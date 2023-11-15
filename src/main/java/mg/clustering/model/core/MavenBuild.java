package mg.clustering.model.core;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MavenBuild implements BuildSystem {
  List<String[]> commands = new ArrayList<>();
  public static String ARTIFACT_PATH = "target/";

  public void build(String buildRepo) {
    commands.add(new String[] {"mvn", "clean", "install", "-DskipTests"});

    for (String[] command : getCommands())
      if (!Utils.execute(command, buildRepo))
        throw new RuntimeException("Error while building maven project");
  }
}
