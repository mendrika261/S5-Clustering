package mg.clustering.model.entity.deployment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.*;
import mg.clustering.model.entity.server.Server;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "build")
public class Build {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "git_url", unique = true, nullable = false)
    private String gitUrl;

    /*@Column(name = "git_branch", nullable = false)
    private String gitBranch; TODO */

    @Column(name = "build_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BuildType buildType;

    @Column(name = "artifact_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "web_xml_path")
    private String webXmlPath;

    @Column(name = "webapp_path")
    private String webappPath;

    @Column(name = "src_path")
    private String srcPath;

    public boolean cloneGit() {
        return Utils.INSTANCE.cloneGit(getGitUrl(), Utils.REPOSITORY_PATH + getRepository()).join();
    }

    public String getRepository() {
        // remove git extension and return the repository name
        return gitUrl.substring(gitUrl.lastIndexOf('/') + 1, gitUrl.lastIndexOf('.')) + "_" + getId();
    }

    public List<ConfigFile> getConfigFiles() {
        try {
            return Utils.INSTANCE.scanConfigFilesIn(
                    ConfigFileType.getExpressions(),
                    Utils.REPOSITORY_PATH+getRepository(), false);
        } catch (IOException e) {
            throw new RuntimeException("Please retry, Error while scanning config files in " + getRepository() + ": " + e.getMessage());
        }
    }

    public void setGitUrl(String gitUrl) {
        if(gitUrl == null || gitUrl.isEmpty())
            throw new IllegalArgumentException("Git url cannot be null or empty");
        if (!gitUrl.endsWith(".git"))
            gitUrl += ".git";
        if (!gitUrl.startsWith("https://"))
            gitUrl = "https://" + gitUrl;
        this.gitUrl = gitUrl;
    }

    public void processBuild() {
        BuildSystem buildSystem;
        switch (getBuildType()) {
            case BuildType.MAVEN -> buildSystem = new MavenBuild();
            case BuildType.MANUAL -> buildSystem = new ManualBuild();
            default -> throw new RuntimeException("Build type not yet supported: " + getBuildType());
        }
        //buildSystem.build(Utils.REPOSITORY_PATH + getRepository());
        buildSystem.build(this);
    }

    public void deleteBuild() {
        Utils.deleteDirectory(Utils.REPOSITORY_PATH + getRepository());
    }

    public void setWebXmlPath(String webXmlPath) {
        if(webXmlPath == null || webXmlPath.isEmpty())
            throw new IllegalArgumentException("Web xml path cannot be null or empty");
        if(!webXmlPath.endsWith("web.xml"))
            throw new IllegalArgumentException("Web xml path must end with web.xml");
        if(webXmlPath.startsWith("/"))
            webXmlPath = webXmlPath.substring(1);
        this.webXmlPath = webXmlPath;
    }

    public void setSrcPath(String srcPath) {
        if(srcPath == null || srcPath.isEmpty())
            throw new IllegalArgumentException("Src path cannot be null or empty");
        if(srcPath.startsWith("/"))
            srcPath = srcPath.substring(1);
        this.srcPath = srcPath;
    }

    public void setWebappPath(String webappPath) {
        if(webappPath == null || webappPath.isEmpty())
            throw new IllegalArgumentException("Webapp path cannot be null or empty");
        if(webappPath.startsWith("/"))
            webappPath = webappPath.substring(1);
        this.webappPath = webappPath;
    }
}