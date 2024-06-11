package pub.carzy.free_port_mapper.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.server.modules.model.ClientInfo;
import pub.carzy.free_port_mapper.server.util.JwtUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    @ConfigurationProperties(prefix = "my-setting")
    @Bean
    public SettingConfig settingConfig() {
        return new SettingConfig();
    }

    @ConfigurationProperties(prefix = "jwt")
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @ConfigurationProperties(prefix = "auth")
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Bean
    public Set<String> blacklist() {
        return new HashSet<>();
    }

    @Bean
    private Map<Integer, ClientInfo> loginMap(){
        return new ConcurrentHashMap<>();
    }
}
