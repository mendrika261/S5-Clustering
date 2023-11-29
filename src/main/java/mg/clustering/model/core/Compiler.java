package mg.clustering.model.core;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class Compiler {
    private final String sourceDirectory;
    private final String outDirectory;
    private final String classpath;

    public Compiler(String sourceDirectory, String outDirectory, String classpath) {
        this.sourceDirectory = sourceDirectory;
        this.outDirectory = outDirectory;
        this.classpath = classpath;
    }

    public void compile() throws IOException, InterruptedException {
        List<String> sourceFiles = getSourceFiles(sourceDirectory);
        int remindingFiles = sourceFiles.size();

        while (!sourceFiles.isEmpty()) {
            for (Iterator<String> iterator = sourceFiles.iterator(); iterator.hasNext();) {
                String sourceFile = iterator.next();
                ProcessBuilder processBuilder = new ProcessBuilder("javac", "-parameters", "-cp", classpath + ":" + outDirectory + ":/opt/homebrew/opt/tomcat@10/libexec/lib/*", "-d", outDirectory, sourceFile);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                process.waitFor();
                // show output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                if (process.exitValue() == 0) {
                    iterator.remove();
                }
            }

            if (remindingFiles == sourceFiles.size()) {
                throw new RuntimeException("Error while compiling source files make sure jar files are in the classpath");
            }
        }
    }

    private List<String> getSourceFiles(String sourceDirectory) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(sourceDirectory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(string -> string.endsWith(".java"))
                    .collect(Collectors.toList());
        }
    }
}