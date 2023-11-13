package mg.clustering.service;

import mg.clustering.model.entity.deployment.Build;
import mg.clustering.model.entity.deployment.ConfigFile;
import mg.clustering.model.entity.deployment.Deployment;
import mg.clustering.model.entity.server.Server;
import mg.clustering.model.entity.server.ServerApplication;
import mg.clustering.model.entity.server.TransfertMethod;
import mg.clustering.repository.deployment.BuildRepository;
import mg.clustering.repository.deployment.DeploymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final BuildRepository buildRepository;

    public DeploymentService(DeploymentRepository deploymentRepository,
                             BuildRepository buildRepository) {
        this.deploymentRepository = deploymentRepository;
        this.buildRepository = buildRepository;
    }

    @Transactional
    public void deploy(long buildId, ConfigFile configFile, String content, List<Server> serverList, List<ServerApplication> serverApplicationList, List<TransfertMethod> transfertMethodList) {
        Build build = buildRepository.findById(buildId).orElseThrow(() -> new RuntimeException("Build to deploy not found"));

        configFile.setContent(content);

        Deployment deployment;
        for (int i=0; i< serverList.size(); i++) {
            deployment = new Deployment();
            deployment.setBuild(build);
            deployment.setConfigFile(configFile);
            deployment.setServer(serverList.get(i));
            deployment.setServerApplication(serverApplicationList.get(i));
            deployment.setTransfertMethod(transfertMethodList.get(i));
            deploymentRepository.save(deployment);
        }
    }
}
