package mg.clustering.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mg.clustering.model.core.Utils;
import mg.clustering.model.entity.deployment.*;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.ServerApplication;
import mg.clustering.model.entity.server.TransfertMethod;
import mg.clustering.repository.deployment.BuildRepository;
import mg.clustering.service.DeploymentService;
import mg.clustering.service.ServerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeploymentController {
  private final ServerService serverService;
  private final BuildRepository buildRepository;
  private final DeploymentService deploymentService;

  public DeploymentController(ServerService serverService,
                              BuildRepository buildRepository,
                              DeploymentService deploymentService) {
    this.serverService = serverService;
    this.buildRepository = buildRepository;
    this.deploymentService = deploymentService;
  }

  @GetMapping("/deployments/add")
  public String addDeployment(@ModelAttribute("build") Build build,
                              Model model) {
    BuildType[] buildTypes = BuildType.values();
    ArtifactType[] artifactTypes = ArtifactType.values();

    model.addAttribute("buildTypes", buildTypes);
    model.addAttribute("artifactTypes", artifactTypes);
    return "deployment/add-deployment";
  }

  @PostMapping("/deployments/add")
  public String addDeployment(@ModelAttribute Build build,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("build", build);
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute(
          "errorMessage",
          "Verify that your " +
              Objects.requireNonNull(bindingResult.getFieldError()).getField() +
              " is valid");
      return "redirect:/deployments/add";
    }

    try {
      deploymentService.makeBuild(build);
    } catch (DataIntegrityViolationException e) {
      redirectAttributes.addFlashAttribute("errorMessage",
                                           "This build already exists");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
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
      configFileList = build.getConfigFiles();
    } catch (RuntimeException e) {
      model.addAttribute("errorMessage", e.getMessage());
    }

    model.addAttribute("configFileList", configFileList);
    model.addAttribute("serverList", serverList);
    model.addAttribute("build", build);
    return "deployment/deploy";
  }

  @PostMapping("/deployments/{buildId}/deploy")
  public String
  deploy(@PathVariable long buildId, @RequestParam String configFile,
         @RequestParam String configFileContent,
         @RequestParam(value = "server[]", required = false) Long[] serverList,
         @RequestParam(value = "serverApp[]",
                       required = false) Long[] serverApplicationList,
         @RequestParam(value = "transfertMethod[]", required = false)
         Long[] transfertMethodList, RedirectAttributes redirectAttributes) {
    try {
      deploymentService.deploy(buildId, configFile, configFileContent,
                               serverList, serverApplicationList,
                               transfertMethodList);
    } catch (DataIntegrityViolationException e) {
      redirectAttributes.addFlashAttribute(
          "errorMessage",
          "You already deployed this on one the given server config");
    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return "redirect:/deployments/{buildId}/deploy";
  }
}
