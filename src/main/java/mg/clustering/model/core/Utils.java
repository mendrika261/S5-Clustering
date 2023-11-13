package mg.clustering.model.core;

import mg.clustering.model.entity.deployment.ConfigFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    public static String PWD = System.getProperty("user.dir")+"/";
    public static String REPOSITORY_PATH = PWD+"repository/";

    public static boolean isValidIpV4(String ipv4) {
        return ipv4.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    public static boolean isValidPort(String port) {
        return port.matches("^[0-9]{1,5}$");
    }

    public static boolean ping(String ipv4) {
        String[] command = {"ping", "-c", "1", ipv4};
        try {
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPortOpen(String ipv4, String port) {
        String[] command = {"nc", "-z", ipv4, port};
        try {
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

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
}
