package pub.carzy.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.client.config.ProxyServerConfig;
import pub.carzy.client.config.ProxySystemConfig;
import pub.carzy.free_port_mapper.protocol.*;
import pub.carzy.free_port_mapper.protocol.listener.ProxyTypePostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @author admin
 */
@Slf4j
@Component
public class CmdTcpProxy {

    @Resource
    private ProxySystemConfig proxySystemConfig;

    @Resource
    private ProxyServerConfig proxyServerConfig;

    @Resource
    private Set<ProxyTypePostProcessor> processors;

    private final CmdContext cmdContext = new CmdContext();

    @PostConstruct
    void start() {
        //创建对应的服务端
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProxyMessageDecoder(proxySystemConfig.getMaxFrameLength(),
                                proxySystemConfig.getLengthFieldOffset(),
                                proxySystemConfig.getLengthFieldLength(),
                                proxySystemConfig.getLengthAdjustment(),
                                proxySystemConfig.getInitialBytesToStrip()));
                        ch.pipeline().addLast(new ProxyMessageEncoder());
                        ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME - 10, 0));
                        ch.pipeline().addLast(new ClientChannelHandler(cmdContext));
                    }
                });
        //保存到上下文
        cmdContext.setBootstrap(bootstrap);
        cmdContext.setProcessors(processors);
        cmdContext.setProxyServerConfig(proxyServerConfig);
        connect();
    }

    void connect() {
        cmdContext.getBootstrap().connect(proxyServerConfig.getHost(), proxyServerConfig.getPort()).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // 连接成功，向服务器发送客户端认证信息（clientKey）
                cmdContext.setCmdChannel(future.channel());
                ProxyEntity proxyEntity = new ProxyEntity();
                proxyEntity.setType(ProxyType.AUTH);
                proxyEntity.setData(proxyServerConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
                future.channel().writeAndFlush(proxyEntity);
                log.info("connect proxy server success, {}", future.channel());
            } else {
                log.warn("connect proxy server failed", future.cause());

                // 连接失败，发起重连
                // reconnectWait();
                // connectProxyServer();
            }
        });
    }
}
