package mg.clustering;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class S5ClusteringApplicationTests {

    @SneakyThrows
    @Test
    void contextLoads() {
        String ipv4 = "127.0.0.1";
        String[] command = {"ping", "-c", "1", ipv4};
        Process process = Runtime.getRuntime().exec(command);
        System.out.println(process.waitFor());
    }

}
