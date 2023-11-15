package mg.clustering.repository.deployment;

import mg.clustering.model.entity.deployment.ConfigFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigFileRepository extends JpaRepository<ConfigFile, Long> {
}