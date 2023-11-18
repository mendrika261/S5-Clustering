package mg.clustering.repository.deployment;

import mg.clustering.model.entity.deployment.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    List<Deployment> findAllByServerId(long serverId);
}