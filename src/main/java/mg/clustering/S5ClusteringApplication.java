package mg.clustering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class S5ClusteringApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(S5ClusteringApplication.class, args);
    }
}
