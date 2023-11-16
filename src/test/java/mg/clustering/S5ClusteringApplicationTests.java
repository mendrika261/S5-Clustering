package mg.clustering;

import lombok.SneakyThrows;
import mg.clustering.model.entity.deployment.ConfigFile;
import mg.clustering.model.entity.deployment.HaProxyConfigFile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class S5ClusteringApplicationTests {

    public static String getFileContent(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file content of " + path);
        }
    }

    public static List<ConfigFile> scanConfigFilesIn(String[] regexCondition, String repository) throws IOException {
        List<ConfigFile> configFileList = new ArrayList<>();

        Path path = Paths.get(repository);
        Stream<Path> walk = Files.walk(path);
        walk.filter(Files::isRegularFile).forEach(filePath -> {
            String fileName = filePath.getFileName().toString();
            for (String regex : regexCondition) {
                if (fileName.matches(regex)) {
                    configFileList.add(new ConfigFile(fileName, getFileContent(filePath)));
                }
            }
        });

        return configFileList;
    }

    @SneakyThrows
    @Test
    void contextLoads() {
        HaProxyConfigFile ha = HaProxyConfigFile.parseHaProxyConfigFile(getFileContent(Paths.get("src/test/haproxy.cfg")));
        System.out.println(ha);
    }

}
