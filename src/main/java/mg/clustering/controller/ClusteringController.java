package mg.clustering.controller;

import mg.clustering.model.entity.clustering.HaProxy;
import mg.clustering.repository.server.ServerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClusteringController {

    private final ServerRepository serverRepository;

    public ClusteringController(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
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
            serverRepository.save(haProxy.getServer());
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Server is already declared as load balancer");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/haproxy/add";
    }
}
