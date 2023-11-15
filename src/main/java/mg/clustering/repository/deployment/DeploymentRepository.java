package mg.clustering.repository.deployment;

import mg.clustering.model.entity.deployment.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {}