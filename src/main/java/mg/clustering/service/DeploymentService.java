package mg.clustering.service;

import mg.clustering.model.entity.deployment.Build;
import mg.clustering.model.entity.deployment.ConfigFile;
import mg.clustering.model.entity.deployment.Deployment;
import mg.clustering.repository.deployment.BuildRepository;
import mg.clustering.repository.deployment.ConfigFileRepository;
import mg.clustering.repository.deployment.DeploymentRepository;
import mg.clustering.repository.server.ServerApplicationRepository;
import mg.clustering.repository.server.ServerRepository;
import mg.clustering.repository.server.TransfertMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final BuildRepository buildRepository;
    private final ServerRepository serverRepository;
    private final ServerApplicationRepository serverApplicationRepository;
    private final TransfertMethodRepository transfertMethodRepository;
    private final ConfigFileRepository configFileRepository;

    public DeploymentService(DeploymentRepository deploymentRepository,
                             BuildRepository buildRepository,
                             ServerRepository serverRepository,
                             ServerApplicationRepository serverApplicationRepository,
                             TransfertMethodRepository transfertMethodRepository,
                             ConfigFileRepository configFileRepository) {
        this.deploymentRepository = deploymentRepository;
        this.buildRepository = buildRepository;
        this.serverRepository = serverRepository;
        this.serverApplicationRepository = serverApplicationRepository;
        this.transfertMethodRepository = transfertMethodRepository;
        this.configFileRepository = configFileRepository;
    }

    @Transactional
    public void makeBuild(Build build) {
        buildRepository.save(build);
        if(!build.cloneGit())
            throw new RuntimeException("Error while cloning git repository " + build.getGitUrl());
    }

    @Transactional
    public void deploy(long buildId, String configFile, String content, Long[] serverList, Long[] serverApplicationList, Long[] transfertMethodList) {
        Build build = buildRepository.findById(buildId).orElseThrow(() -> new RuntimeException("Build to deploy not found"));

        if(serverList == null || serverList.length == 0 || serverApplicationList == null || serverApplicationList.length == 0 || transfertMethodList == null || transfertMethodList.length == 0)
            throw new RuntimeException("You must select at least one server");

        ConfigFile config = new ConfigFile();
        config.setName(configFile);
        config.setContent(content);
        config.setFileType(config.getFileType());
        configFileRepository.saveAndFlush(config);

        List<Deployment> deploymentList = new ArrayList<>();
        Deployment deployment;
        for (int i=0; i< serverList.length; i++) {
            deployment = new Deployment();
            deployment.setBuild(build);
            if ((configFile != null && !configFile.isEmpty()) && (content != null && !content.isEmpty()))
                deployment.setConfigFile(config);
            deployment.setServer(serverRepository.findById(serverList[i]).orElseThrow());
            deployment.setServerApplication(serverApplicationRepository.findById(serverApplicationList[i]).orElseThrow());
            deployment.setTransfertMethod(transfertMethodRepository.findById(transfertMethodList[i]).orElseThrow());
            deploymentRepository.saveAndFlush(deployment);
            deploymentList.add(deployment);
        }

        config.writeFile();
        build.processBuild();
        deploymentList.forEach(Deployment::deploy);
    }
}
