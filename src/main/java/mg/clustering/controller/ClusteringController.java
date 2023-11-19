package mg.clustering.controller;

import mg.clustering.model.entity.clustering.HaProxy;
import mg.clustering.repository.clustering.HaProxyRepository;
import mg.clustering.repository.server.ServerRepository;
import mg.clustering.service.ClusteringService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClusteringController {

    private final ServerRepository serverRepository;
    private final HaProxyRepository haProxyRepository;
    private final ClusteringService clusteringService;

    public ClusteringController(ServerRepository serverRepository,
                                HaProxyRepository haProxyRepository, ClusteringService clusteringService) {
        this.serverRepository = serverRepository;
        this.haProxyRepository = haProxyRepository;
        this.clusteringService = clusteringService;
    }

    @GetMapping("haproxy/add")
    public String addHaProxy(@ModelAttribute("haProxy") HaProxy haProxy, Model model) {
        model.addAttribute("serverList", serverRepository.findAll());
        return "clustering/add-haproxy";
    }

    @PostMapping("haproxy/add")
    public String addHaProxy(@ModelAttribute HaProxy haProxy, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("haProxy", haProxy);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Verify your config file ends with .cfg");
            return "redirect:/haproxy/add";
        }
        try {
            haProxyRepository.save(haProxy);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Server is already declared as load balancer");
            return "redirect:/haproxy/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/haproxy/add";
        }
        return "redirect:/servers/" + haProxy.getServer().getId() + "/manage";
    }


    @GetMapping("servers/{serverId}/haproxy/frontends/add")
    public String addFrontend(Model model, @PathVariable long serverId) {
        model.addAttribute("server", serverRepository.findById(serverId).orElseThrow());

        return "clustering/add-frontend";
    }

    @PostMapping("servers/{serverId}/haproxy/frontends/add")
    public String addFrontend(RedirectAttributes redirectAttributes,
                              @PathVariable long serverId,
                              @RequestParam String frontEndName,
                              @RequestParam String bind,
                              @RequestParam String backend) {
        try {
            clusteringService.addFrontend(serverId, frontEndName, bind, backend);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/servers/{serverId}/haproxy/frontends/add";
        }
        return "redirect:/servers/{serverId}/manage";
    }


    @GetMapping("servers/{serverId}/haproxy/backends/add")
    public String addBackend(Model model, @PathVariable long serverId) {
        model.addAttribute("server", serverRepository.findById(serverId).orElseThrow());
        return "clustering/add-backend";
    }

    @PostMapping("servers/{serverId}/haproxy/backends/add")
    public String addBackend(RedirectAttributes redirectAttributes,
                             @PathVariable long serverId,
                             @RequestParam String name,
                             @RequestParam String balance,
                             @RequestParam String serverName,
                             @RequestParam String serverDestination) {
        try {
            clusteringService.addBackend(serverId, name, balance, serverName, serverDestination);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/servers/{serverId}/haproxy/backends/add";
        }
        return "redirect:/servers/{serverId}/manage";
    }

    @GetMapping("/servers/{serverId}/haproxy/backends")
    public String addServer(Model model, @PathVariable long serverId) {
        model.addAttribute("server", serverRepository.findById(serverId).orElseThrow());
        return "clustering/add-server";
    }

    @PostMapping("/servers/{serverId}/haproxy/backends")
    public String addServer(RedirectAttributes redirectAttributes,
                            @PathVariable long serverId,
                            @RequestParam String backend,
                            @RequestParam String serverName,
                            @RequestParam String serverDestination) {
        try {
            clusteringService.addServer(serverId, backend, serverName, serverDestination);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/servers/{serverId}/haproxy/backends";
        }
        return "redirect:/servers/{serverId}/manage";
    }

    @GetMapping("/servers/{serverId}/haproxy/backends/delete/{backendName}")
    public String deleteBackend(RedirectAttributes redirectAttributes,
                                @PathVariable long serverId,
                                @PathVariable String backendName) {
        try {
            clusteringService.deleteBackend(serverId, backendName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/{serverId}/manage";
    }

    @GetMapping("/servers/{serverId}/haproxy/frontends/delete/{frontendName}")
    public String deleteFrontend(RedirectAttributes redirectAttributes,
                                @PathVariable long serverId,
                                @PathVariable String frontendName) {
        try {
            clusteringService.deleteFrontend(serverId, frontendName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/{serverId}/manage";
    }

    @GetMapping("/servers/{serverId}/haproxy/backends/{backendName}/delete/{serverName}")
    public String deleteServer(RedirectAttributes redirectAttributes,
                                @PathVariable long serverId,
                                @PathVariable String backendName,
                                @PathVariable String serverName) {
        try {
            clusteringService.deleteServer(serverId, backendName, serverName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/servers/{serverId}/manage";
    }
}
