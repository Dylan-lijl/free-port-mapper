package pub.carzy.free_port_mapper.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
public class ServiceBean {

    @ConfigurationProperties(prefix = "file-config")
    @Bean
    public FileConfig fileConfig() {
        return new FileConfig();
    }
}
