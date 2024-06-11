package pub.carzy.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代理服务器的配置
 *
 * @author admin
 */
@Component
@ConfigurationProperties(prefix = "proxy-server")
@Data
public class ProxyServerConfig {
    private String host;
    private Integer port;
    private String secretKey;
}
