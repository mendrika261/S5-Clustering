package mg.clustering.service;

import mg.clustering.model.core.SSHTransfert;
import mg.clustering.model.core.Transfert;
import mg.clustering.model.core.Utils;
import mg.clustering.model.entity.clustering.HaProxyBackEnd;
import mg.clustering.model.entity.clustering.HaProxyConfigFile;
import mg.clustering.model.entity.clustering.HaProxyFrontEnd;
import mg.clustering.model.entity.clustering.HaProxyServer;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.TransfertMethod;
import mg.clustering.repository.server.ServerRepository;
import org.springframework.stereotype.Service;

@Service
public class ClusteringService {

    private final ServerRepository serverRepository;

    public ClusteringService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void addBackend(long serverId, String name, String balance, String serverName, String serverDestination) {
        Server server = serverRepository.findById(serverId).orElseThrow();
        if (server.getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + server);
        TransfertMethod transfertMethod = server.getTransfertMethods().get(0);
        String path = Utils.CONFIG_PATH + server.getHaProxy().getId() + "-haproxy.cfg";
        Transfert transfert = new SSHTransfert(server.getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());

        HaProxyConfigFile haProxyConfigFile = server.getHaProxy().getHaProxyCfg();
        HaProxyBackEnd haProxyBackEnd = new HaProxyBackEnd(name, balance);
        haProxyBackEnd.addServer(new HaProxyServer(serverName, serverDestination));
        haProxyConfigFile.addBackend(haProxyBackEnd);
        haProxyConfigFile.save(path);

        transfert.upload(path, server.getHaProxy().getConfigFile());
    }

    public void addFrontend(long serverId, String frontEndName, String bind, String backend) {
        Server server = serverRepository.findById(serverId).orElseThrow();
        if (server.getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + server);
        TransfertMethod transfertMethod = server.getTransfertMethods().get(0);
        String path = Utils.CONFIG_PATH + server.getHaProxy().getId() + "-haproxy.cfg";
        Transfert transfert = new SSHTransfert(server.getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());

        HaProxyConfigFile haProxyConfigFile = server.getHaProxy().getHaProxyCfg();
        HaProxyFrontEnd haProxyFrontEnd = new HaProxyFrontEnd(frontEndName, bind, backend);
        haProxyConfigFile.addFrontend(haProxyFrontEnd);
        haProxyConfigFile.save(path);

        transfert.upload(path, server.getHaProxy().getConfigFile());
    }

    public void addServer(long serverId, String backend, String serverName, String serverDestination) {
        Server server = serverRepository.findById(serverId).orElseThrow();
        if (server.getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + server);
        TransfertMethod transfertMethod = server.getTransfertMethods().get(0);
        String path = Utils.CONFIG_PATH + server.getHaProxy().getId() + "-haproxy.cfg";
        Transfert transfert = new SSHTransfert(server.getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());

        HaProxyConfigFile haProxyConfigFile = server.getHaProxy().getHaProxyCfg();
        HaProxyBackEnd haProxyBackEnd = haProxyConfigFile.getHaProxyBackEnds().stream().filter(haProxyBackEnd1 -> haProxyBackEnd1.getName().equals(backend)).findFirst().orElseThrow();
        haProxyBackEnd.addServer(new HaProxyServer(serverName, serverDestination));
        haProxyConfigFile.save(path);

        transfert.upload(path, server.getHaProxy().getConfigFile());
    }

    public void deleteBackend(long serverId, String backendName) {
        Server server = serverRepository.findById(serverId).orElseThrow();
        if (server.getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + server);
        TransfertMethod transfertMethod = server.getTransfertMethods().get(0);
        String path = Utils.CONFIG_PATH + server.getHaProxy().getId() + "-haproxy.cfg";
        Transfert transfert = new SSHTransfert(server.getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());

        HaProxyConfigFile haProxyConfigFile = server.getHaProxy().getHaProxyCfg();
        HaProxyBackEnd haProxyBackEnd = haProxyConfigFile.getHaProxyBackEnds().stream().filter(haProxyBackEnd1 -> haProxyBackEnd1.getName().equals(backendName)).findFirst().orElseThrow();
        String backend = haProxyBackEnd.getName();
        haProxyConfigFile.getHaProxyBackEnds().remove(haProxyBackEnd);

        // Remove all frontends that use this backend
        haProxyConfigFile.getHaProxyFrontEnds().removeIf(haProxyFrontEnd -> haProxyFrontEnd.getDefault_backend().equals(backend));
        haProxyConfigFile.save(path);

        transfert.upload(path, server.getHaProxy().getConfigFile());
    }

    public void deleteFrontend(long serverId, String frontendName) {
        Server server = serverRepository.findById(serverId).orElseThrow();
        if (server.getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + server);
        TransfertMethod transfertMethod = server.getTransfertMethods().get(0);
        String path = Utils.CONFIG_PATH + server.getHaProxy().getId() + "-haproxy.cfg";
        Transfert transfert = new SSHTransfert(server.getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());

        HaProxyConfigFile haProxyConfigFile = server.getHaProxy().getHaProxyCfg();
        haProxyConfigFile.getHaProxyFrontEnds().removeIf(haProxyFrontEnd -> haProxyFrontEnd.getName().equals(frontendName));
        haProxyConfigFile.save(path);

        transfert.upload(path, server.getHaProxy().getConfigFile());
    }

    public void deleteServer(long serverId, String backendName, String serverName) {
        Server server = serverRepository.findById(serverId).orElseThrow();
        if (server.getTransfertMethods().isEmpty())
            throw new RuntimeException("No transfert method found for server " + server);
        TransfertMethod transfertMethod = server.getTransfertMethods().get(0);
        String path = Utils.CONFIG_PATH + server.getHaProxy().getId() + "-haproxy.cfg";
        Transfert transfert = new SSHTransfert(server.getIpv4(), transfertMethod.getUsername(), transfertMethod.getPassword(), transfertMethod.getPort());

        HaProxyConfigFile haProxyConfigFile = server.getHaProxy().getHaProxyCfg();
        HaProxyBackEnd haProxyBackEnd = haProxyConfigFile.getHaProxyBackEnds().stream().filter(haProxyBackEnd1 -> haProxyBackEnd1.getName().equals(backendName)).findFirst().orElseThrow();
        haProxyBackEnd.getHaProxyServers().removeIf(haProxyServer -> haProxyServer.getName().equals(serverName));

        if (haProxyBackEnd.getHaProxyServers().isEmpty())
            haProxyConfigFile.getHaProxyBackEnds().remove(haProxyBackEnd);

        haProxyConfigFile.save(path);

        transfert.upload(path, server.getHaProxy().getConfigFile());
    }
}
