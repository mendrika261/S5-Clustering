package mg.clustering.repository.deployment;

import mg.clustering.model.entity.deployment.Build;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRepository extends JpaRepository<Build, Long> {}