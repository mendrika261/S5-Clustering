package mg.clustering.controller;

import mg.clustering.model.entity.server.*;
import mg.clustering.repository.server.OperatingSystemRepository;
import mg.clustering.repository.server.ServerApplicationTypeRepository;
import mg.clustering.repository.server.ServerRepository;
import mg.clustering.service.ServerService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String addServer(Model model) {
        List<OperatingSystem> operatingSystemList = operatingSystemRepository.findAll(Sort.by("name"));

        model.addAttribute("operatingSystemList", operatingSystemList);
        return "server/add-server";
    }

    @PostMapping("/servers/add")
    public String addServer(@ModelAttribute Server server, RedirectAttributes redirectAttributes) {
        try {
            serverRepository.saveAndFlush(server);
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers";
    }


    @GetMapping("/servers/{serverId}/server-apps/add")
    public String addServerApp(@PathVariable String serverId, Model model) {
        List<ServerApplicationType> serverApplicationTypeList = serverApplicationTypeRepository.findAll(Sort.by("name", "version"));

        model.addAttribute("serverApplicationList", serverApplicationTypeList);
        return "server/add-server-app";
    }

    @PostMapping("/servers/{serverId}/server-apps/add")
    public String addServerApp(@PathVariable long serverId,
                               @ModelAttribute ServerApplication serverApplication,
                               RedirectAttributes redirectAttributes) {
        try {
            serverService.addServerApplication(serverId, serverApplication);
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/" + serverId + "/server-apps/add";
    }


    @GetMapping("/servers/{serverId}/transfert-methods/add")
    public String addTransfertMethod(@PathVariable long serverId, Model model) {
        TransfertMethodType[] transfertMethodTypes = TransfertMethodType.values();

        model.addAttribute("transfertMethods", transfertMethodTypes);
        return "server/add-transfert-method";
    }

    @PostMapping("/servers/{serverId}/transfert-methods/add")
    public String addTransfertMethod(@PathVariable long serverId,
                                     @ModelAttribute TransfertMethod transfertMethod,
                                        RedirectAttributes redirectAttributes) {
        try {
            serverService.addTransfertMethod(serverId, transfertMethod);
        } catch (RuntimeException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/" + serverId + "/transfert-methods/add";
    }
}
