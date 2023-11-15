package mg.clustering.controller;

import mg.clustering.model.core.Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeploymentRestController {
    @PostMapping("/api/get_content")
    public String getFileContent(@RequestParam String file) {
        return Utils.getFileContent(file);
    }
}
