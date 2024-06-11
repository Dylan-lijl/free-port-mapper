package pub.carzy.free_port_mapper.server.util;

/**
 * @author admin
 */
public class HttpThreadLocal {
    public static final ThreadLocal<String> TOKEN = new ThreadLocal<>();
}
