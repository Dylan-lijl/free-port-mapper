package pub.carzy.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
@ConfigurationProperties(prefix = "proxy-system")
@Data
public class ProxySystemConfig {
    /**
     * 最大帧长度
     */
    private int maxFrameLength = 2*1024*1024;
    /**
     * 位移
     */
    private int lengthFieldOffset = 0;
    /**
     *字段长度
     */
    private int lengthFieldLength = 4;
    /**
     * 长度调整
     */
    private int lengthAdjustment = 4;
    /**
     * 跳过字节数
     */
    private int initialBytesToStrip = 4;
}
