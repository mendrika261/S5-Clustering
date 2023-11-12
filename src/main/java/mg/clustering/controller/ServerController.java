package mg.clustering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ServerController {
    @GetMapping("/servers/add")
    public String addServer() {
        return "server/add-server";
    }

    @GetMapping("/servers/{serverId}/server-apps/add")
    public String addServerApp(@PathVariable String serverId) {
        return "server/add-server-app";
    }

    @GetMapping("/servers/{serverId}/transfert-methods/add")
    public String addTransfertMethod(@PathVariable String serverId) {
        return "server/add-transfert-method";
    }
}
