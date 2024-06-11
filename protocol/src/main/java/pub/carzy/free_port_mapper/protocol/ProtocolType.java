package pub.carzy.free_port_mapper.protocol;

/**
 * 协议,所有连接本地只有tcp和udp两种
 * 最后两位作为协议传输层协议标志位
 *
 * @author admin
 */
public class ProtocolType {
    public static final int TCP = 0b0001;
    public static final int UDP = 0b0010;
    public static final int HTTP = (0b0001 << 2) + TCP;
    public static final int QUIC = (0b0010 << 2) + UDP;

    public static boolean isTcp(int protocol) {
        return (protocol & 0b0001) == TCP;
    }

    public static boolean isUdp(int protocol) {
        return (protocol & 0b0010) == UDP;
    }
}
