package pub.carzy.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import pub.carzy.client.config.ProxyServerConfig;
import pub.carzy.free_port_mapper.protocol.listener.ProxyTypePostProcessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
@Data
public class CmdContext {
    /**
     * 客户端与服务端
     */
    private Bootstrap bootstrap;
    /**
     * 共用线程池
     */
    private NioEventLoopGroup realGroup = new NioEventLoopGroup();
    private Map<String, Bootstrap> realBootstrapMap = new ConcurrentHashMap<>();
    private Channel cmdChannel;
    private Set<ProxyTypePostProcessor> processors = new LinkedHashSet<>();
    private ProxyServerConfig proxyServerConfig;

    public Bootstrap createRealBootstrap(String userId) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(getRealGroup())
                .channel(NioSocketChannel.class);
        realBootstrapMap.put(userId, bootstrap);
        return bootstrap;
    }

    public ChannelFuture connectTcp() {
        return bootstrap.connect(proxyServerConfig.getHost(), proxyServerConfig.getPort());
    }
}
