package mg.clustering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeploymentController {
    @GetMapping("/deployments/add")
    public String addDeployments() {
        return "deployment/add-deployment";
    }

    @GetMapping("/deployments/{deploymentId}/deploy")
    public String deploy() {
        return "deployment/deploy";
    }
}
