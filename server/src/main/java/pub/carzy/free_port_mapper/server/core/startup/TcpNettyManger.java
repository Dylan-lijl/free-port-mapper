package pub.carzy.free_port_mapper.server.core.startup;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import pub.carzy.free_port_mapper.protocol.IdleCheckHandler;
import pub.carzy.free_port_mapper.protocol.ProxyMessageDecoder;
import pub.carzy.free_port_mapper.protocol.ProxyMessageEncoder;
import pub.carzy.free_port_mapper.server.config.PackageConfig;

import javax.annotation.PostConstruct;

/**
 * @author admin
 */
public class TcpNettyManger extends AbstractNettyManger {

    protected ServerBootstrap bootstrap = new ServerBootstrap();

    @PostConstruct
    void init() {
        PackageConfig packageConfig = config.getPackageConfig();
        bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                socketChannel.pipeline().addLast(new ProxyMessageDecoder(packageConfig.getMaxFrameLength(),
                        packageConfig.getLengthFieldOffset(),
                        packageConfig.getLengthFieldLength(),
                        packageConfig.getLengthAdjustment(),
                        packageConfig.getInitialBytesToStrip()));
                socketChannel.pipeline().addLast(new ProxyMessageEncoder());
                socketChannel.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME, 0));
                socketChannel.pipeline().addLast(new ServerChannelHandler(processors));
            }
        });
    }
}
