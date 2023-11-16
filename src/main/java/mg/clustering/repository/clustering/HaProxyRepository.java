package mg.clustering.repository.clustering;

import mg.clustering.model.entity.clustering.HaProxy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HaProxyRepository extends JpaRepository<HaProxy, Long> {
}