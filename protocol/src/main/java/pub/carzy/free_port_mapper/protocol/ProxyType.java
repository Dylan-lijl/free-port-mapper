package pub.carzy.free_port_mapper.protocol;

/**
 * @author admin
 */
public class ProxyType {
    public static final int NULL = 0b0000;
    public static final int HEARTBEAT = 0b0001;
    /**
     * 认证消息，检测clientKey是否正确
     */
    public static final int AUTH = 0b0010;

    // /** 保活确认消息 */
    // public static final byte TYPE_ACK = 0b0100;

    /**
     * 代理后端服务器建立连接消息
     */
    public static final int CONNECT = 0b1000;

    /**
     * 代理后端服务器断开连接消息
     */
    public static final int DISCONNECT = 0b1_0000;

    /**
     * 代理数据传输
     */
    public static final int TRANSFER = 0b10_0000;
    /**
     * 绑定
     */
    public static final int BIND = 0b100_0000;
    /**
     * 更新客户代理信息
     */
    public static final int UPDATE_CLIENT_PROXY = 0b1000_0000;
}
