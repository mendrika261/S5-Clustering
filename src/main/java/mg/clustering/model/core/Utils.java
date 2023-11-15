package mg.clustering.model.core;

import mg.clustering.model.entity.deployment.ConfigFile;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Utils {
    public static String PWD = System.getProperty("user.dir")+"/";
    public static String REPOSITORY_PATH = PWD+"repository/";
    public static Utils INSTANCE = new Utils();

    public static boolean isValidIpV4(String ipv4) {
        return ipv4.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    public static boolean isValidPort(String port) {
        return port.matches("^[0-9]{1,5}$");
    }

    public static boolean execute(String[] command, String workingDirectory) {
        try {
            Process process = Runtime.getRuntime().exec(command, null, Paths.get(workingDirectory).toFile());
            return process.waitFor() == 0;
        } catch (Exception e) {
            throw new RuntimeException("Error while executing command " + String.join(" ", command) + " in " + workingDirectory + " : " + e.getMessage());
        }
    }

    public static void writeFile(String name, String content) {
        try {
            Files.writeString(Paths.get(name), content);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing file " + name + ": " + e.getMessage());
        } catch (SecurityException e) {
            throw new RuntimeException("Cannot access file " + name + ": " + e.getMessage());
        }
    }

    @Async
    public CompletableFuture<Boolean> ping(String ipv4) {
        String[] command = {"ping", "-c", "1", "-W", "1", ipv4};
        try {
            Process process = Runtime.getRuntime().exec(command);
            return CompletableFuture.completedFuture(process.waitFor() == 0);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
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

    public static String getFileContent(String path) {
        return getFileContent(Paths.get(path));
    }

    @Cacheable("configFiles")
    public List<ConfigFile> scanConfigFilesIn(String[] regexConditions, String repository, boolean includeContent)
            throws IOException {
        // start time
        long startTime = System.currentTimeMillis();
        try (Stream<Path> walk = Files.walk(Paths.get(repository))) {
            return walk.filter(Files::isRegularFile)
                    .filter(filePath -> matchesAnyRegex(filePath.getFileName().toString(), regexConditions))
                    .map(filePath -> includeContent ?
                            new ConfigFile(filePath.toFile().getPath(), getFileContent(filePath)) :
                            new ConfigFile(filePath.toFile().getPath()))
                    .collect(Collectors.toList());
        }
        finally {
            // end time
            long endTime = System.currentTimeMillis();
            // total time
            long totalTime = endTime - startTime;
            System.out.println("Scanning config files in " + repository + " took " + totalTime + " ms");
        }
    }

    public boolean matchesAnyRegex(String fileName, String[] regexConditions) {
        for (String regex : regexConditions) {
            if (fileName.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    @Async
    public CompletableFuture<Boolean> cloneGit(String gitUrl, String destinationPath) {
        try {
            Git git = Git.cloneRepository()
                    .setURI(gitUrl)
                    .setDirectory(Paths.get(destinationPath).toFile())
                    .call();
            git.close();
            return CompletableFuture.completedFuture(git.getRepository().getDirectory().exists());
        } catch (GitAPIException e) {
            throw new RuntimeException("Error while cloning git repository " + gitUrl + " : " + e.getMessage());
        }
    }
}
