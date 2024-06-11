package pub.carzy.free_port_mapper.server.config;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class PackageConfig {
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
