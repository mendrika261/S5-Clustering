package mg.clustering.repository.server;

import mg.clustering.model.entity.server.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatingSystemRepository
    extends JpaRepository<OperatingSystem, Long> {}