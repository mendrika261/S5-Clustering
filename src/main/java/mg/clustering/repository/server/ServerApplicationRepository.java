package mg.clustering.repository.server;

import mg.clustering.model.entity.server.ServerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerApplicationRepository
    extends JpaRepository<ServerApplication, Long> {}
