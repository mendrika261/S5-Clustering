package mg.clustering.controller;

import mg.clustering.model.entity.deployment.ArtifactType;
import mg.clustering.model.entity.deployment.BuildType;
import mg.clustering.model.entity.deployment.ConfigFile;
import mg.clustering.model.entity.deployment.ConfigFileType;
import mg.clustering.model.entity.server.Server;
import mg.clustering.service.ServerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class DeploymentController {
    private final ServerService serverService;

    public DeploymentController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/deployments/add")
    public String addDeployments(Model model) {
        BuildType[] buildTypes = BuildType.values();
        ArtifactType[] artifactTypes = ArtifactType.values();

        model.addAttribute("buildTypes", buildTypes);
        model.addAttribute("artifactTypes", artifactTypes);
        return "deployment/add-deployment";
    }

    @GetMapping("/deployments/{deploymentId}/deploy")
    public String deploy(@PathVariable String deploymentId, Model model) {
        List<ConfigFile> configFileList = new ArrayList<>();
        List<Server> serverList = serverService.getAllReadyServer();

        model.addAttribute("configFileList", configFileList);
        model.addAttribute("serverList", serverList);
        return "deployment/deploy";
    }
}
