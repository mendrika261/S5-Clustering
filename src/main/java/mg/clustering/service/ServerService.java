package mg.clustering.service;

import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.ServerApplication;
import mg.clustering.model.entity.server.TransfertMethod;
import mg.clustering.repository.server.ServerApplicationRepository;
import mg.clustering.repository.server.ServerRepository;
import mg.clustering.repository.server.TransfertMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ServerService {

    private final ServerApplicationRepository serverApplicationRepository;
    private final TransfertMethodRepository transfertMethodRepository;
    private final ServerRepository serverRepository;

    public ServerService(ServerApplicationRepository serverApplicationRepository,
                         TransfertMethodRepository transfertMethodRepository, ServerRepository serverRepository) {
        this.serverApplicationRepository = serverApplicationRepository;
        this.transfertMethodRepository = transfertMethodRepository;
        this.serverRepository = serverRepository;
    }


    public void addServerApplication(long serverId, ServerApplication serverApplication) {
        Server server = serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Server not found"));
        serverApplication.setServer(server);
        server.getServerApplications().add(serverApplication);
        serverApplicationRepository.saveAndFlush(serverApplication);
    }

    public void addTransfertMethod(long serverId, TransfertMethod transfertMethod) {
        Server server = serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Server not found"));
        transfertMethod.setServer(server);
        server.getTransfertMethods().add(transfertMethod);
        transfertMethodRepository.saveAndFlush(transfertMethod);
    }

    public List<Server> getAllReadyServer() {
        List<Server> serverList = serverRepository.findAll();

        serverList.removeIf(server -> !server.isReachable().join());

        return serverList;
    }
}
