package mg.clustering.controller;

import mg.clustering.model.entity.server.*;
import mg.clustering.repository.server.OperatingSystemRepository;
import mg.clustering.repository.server.ServerApplicationTypeRepository;
import mg.clustering.repository.server.ServerRepository;
import mg.clustering.service.ServerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ServerController {

    private final OperatingSystemRepository operatingSystemRepository;
    private final ServerApplicationTypeRepository serverApplicationTypeRepository;
    private final ServerRepository serverRepository;
    private final ServerService serverService;

    public ServerController(OperatingSystemRepository operatingSystemRepository,
                            ServerApplicationTypeRepository serverApplicationTypeRepository,
                            ServerRepository serverRepository, ServerService serverService) {
        this.operatingSystemRepository = operatingSystemRepository;
        this.serverApplicationTypeRepository = serverApplicationTypeRepository;
        this.serverRepository = serverRepository;
        this.serverService = serverService;
    }

    @GetMapping("/servers/add")
    public String addServer(@ModelAttribute("server") Server server, Model model) {
        List<OperatingSystem> operatingSystemList = operatingSystemRepository
                .findAll(Sort.by("operatingSystemType", "name"));

        model.addAttribute("operatingSystemList", operatingSystemList);
        return "server/add-server";
    }

    @PostMapping("/servers/add")
    public String addServer(@ModelAttribute Server server, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("server", server);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Verify that your " +
                    Objects.requireNonNull(bindingResult.getFieldError()).getField() + " is valid");
            return "redirect:/servers/add";
        }

        try {
            serverRepository.saveAndFlush(server);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Server with IPv4: " + server.getIpv4() + " already exists");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e);
        }
        return "redirect:/servers/add";
    }


    @GetMapping("/servers/{serverId}/server-apps/add")
    public String addServerApp(@ModelAttribute("serverApplication") ServerApplication serverApplication,
                                     @PathVariable long serverId,
                                     Model model) {
        Optional<Server> serverOptional = serverRepository.findById(serverId);
        if (serverOptional.isEmpty())
            return "redirect:/servers/add";

        List<ServerApplicationType> serverApplicationTypeList = serverApplicationTypeRepository.findAll(Sort.by("name", "version"));

        model.addAttribute("serverApplicationList", serverApplicationTypeList);
        model.addAttribute("server", serverOptional.get());
        return "server/add-server-app";
    }

    @PostMapping("/servers/{serverId}/server-apps/add")
    public String addServerApp(@PathVariable long serverId,
                               @ModelAttribute ServerApplication serverApplication,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("serverApplication", serverApplication);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Verify that your " +
                            Objects.requireNonNull(bindingResult.getFieldError()).getField() + " is valid");
            return "redirect:/servers/{serverId}/server-apps/add";
        }

        try {
            serverService.addServerApplication(serverId, serverApplication);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Server application with port: " + serverApplication.getPort() + " already exists");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/{serverId}/server-apps/add";
    }


    @GetMapping("/servers/{serverId}/transfert-methods/add")
    public String addTransfertMethod(@PathVariable long serverId,
                                     Model model,
                                     @ModelAttribute("transfertMethod") TransfertMethod transfertMethod) {
        Optional<Server> serverOptional = serverRepository.findById(serverId);
        if (serverOptional.isEmpty())
            return "redirect:/servers/add";

        TransfertMethodType[] transfertMethodTypes = TransfertMethodType.values();

        model.addAttribute("transfertMethodTypes", transfertMethodTypes);
        model.addAttribute("server", serverOptional.get());
        return "server/add-transfert-method";
    }

    @PostMapping("/servers/{serverId}/transfert-methods/add")
    public String addTransfertMethod(@PathVariable long serverId,
                                     @ModelAttribute TransfertMethod transfertMethod,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("transfertMethod", transfertMethod);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Verify that your " +
                            Objects.requireNonNull(bindingResult.getFieldError()).getField() + " is valid");
            return "redirect:/servers/{serverId}/transfert-methods/add";
        }

        try {
            serverService.addTransfertMethod(serverId, transfertMethod);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transfert method for " +
                    transfertMethod.getTransfertMethodType().getName() + " and credential " + transfertMethod.getUsername() + "@" + transfertMethod.getServer().getIpv4() + ":" + transfertMethod.getPort() + " already exists");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/{serverId}/transfert-methods/add";
    }
}
