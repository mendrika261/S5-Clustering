package mg.clustering.repository.server;

import mg.clustering.model.entity.server.ServerApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerApplicationTypeRepository
    extends JpaRepository<ServerApplicationType, Long> {}