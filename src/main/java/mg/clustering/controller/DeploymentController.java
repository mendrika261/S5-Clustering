package mg.clustering.controller;

import mg.clustering.model.core.Utils;
import mg.clustering.model.entity.deployment.*;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.ServerApplication;
import mg.clustering.model.entity.server.TransfertMethod;
import mg.clustering.repository.deployment.BuildRepository;
import mg.clustering.service.DeploymentService;
import mg.clustering.service.ServerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class DeploymentController {
    private final ServerService serverService;
    private final BuildRepository buildRepository;
    private final DeploymentService deploymentService;

    public DeploymentController(ServerService serverService,
                                BuildRepository buildRepository, DeploymentService deploymentService) {
        this.serverService = serverService;
        this.buildRepository = buildRepository;
        this.deploymentService = deploymentService;
    }

    @GetMapping("/deployments/add")
    public String addDeployment(Model model) {
        BuildType[] buildTypes = BuildType.values();
        ArtifactType[] artifactTypes = ArtifactType.values();

        model.addAttribute("buildTypes", buildTypes);
        model.addAttribute("artifactTypes", artifactTypes);
        return "deployment/add-deployment";
    }

    @PostMapping("/deployments/add")
    public String addDeployment(@ModelAttribute Build build, RedirectAttributes redirectAttributes) {
        try {
            buildRepository.save(build);
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/deployments/add";
    }

    @GetMapping("/deployments/{buildId}/deploy")
    public String deploy(@PathVariable long buildId, Model model) {
        Optional<Build> buildOptional = buildRepository.findById(buildId);
        if (buildOptional.isEmpty())
            return "redirect:/error/404";

        Build build = buildOptional.get();
        List<Server> serverList = serverService.getAllReadyServer();
        List<ConfigFile> configFileList = new ArrayList<>();
        try {
            configFileList = Utils.scanConfigFilesIn(
                    ConfigFileType.getExpressions(),
                    Utils.REPOSITORY_PATH+build.getRepository());
        } catch (IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("configFileList", configFileList);
        model.addAttribute("serverList", serverList);
        model.addAttribute("build", build);
        return "deployment/deploy";
    }

    @PostMapping("/deployments/{buildId}/deploy")
    public String deploy(@PathVariable long buildId,
                         @RequestParam ConfigFile configFile,
                         @RequestParam String content,
                         @RequestParam("server[]") List<Server> serverList,
                         @RequestParam("serverApp[]") List<ServerApplication> serverApplicationList,
                         @RequestParam("transfertMethod[]") List<TransfertMethod> transfertMethodList,
                         RedirectAttributes redirectAttributes) {
        try {
            deploymentService.deploy(buildId, configFile, content, serverList, serverApplicationList, transfertMethodList);
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/deployments/{buildId}/deploy";
    }
}
