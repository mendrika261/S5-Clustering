package mg.clustering.repository.server;

import mg.clustering.model.entity.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {}
