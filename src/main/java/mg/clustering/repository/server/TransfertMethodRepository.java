package mg.clustering.repository.server;

import mg.clustering.model.entity.server.TransfertMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransfertMethodRepository
    extends JpaRepository<TransfertMethod, Long> {}
