package pub.carzy.free_port_mapper.server.core.startup;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.protocol.ProtocolType;
import pub.carzy.free_port_mapper.server.core.PortMappingContext;
import pub.carzy.free_port_mapper.server.core.handler.TcpUserChannelHandler;
import pub.carzy.free_port_mapper.server.core.handler.UdpUserChannelHandler;
import pub.carzy.free_port_mapper.server.modules.dto.request.ListClientInfoRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;
import pub.carzy.free_port_mapper.server.modules.service.ClientInfoService;
import pub.carzy.free_port_mapper.server.modules.service.PortMappingService;
import pub.carzy.free_port_mapper.server.util.statics.ClientInfoState;
import pub.carzy.free_port_mapper.server.util.statics.PortMappingState;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射服务器
 * 监听对应的端口
 *
 * @author admin
 */
@Component
@Slf4j
public class PortMappingServer {

    /**
     * 布尔值,决定是否直接启动所有用户的端口
     */
    private Boolean isStartAll = true;

    @Resource
    private ClientInfoService clientInfoService;

    @Resource
    private PortMappingService portMappingService;

    @Resource
    private PortMappingContext context;
    /**
     * tcp 监听端口
     */
    private ServerBootstrap tcpBootstrap;
    /**
     * udp 监听端口
     */
    private Map<String, Bootstrap> udpBootstrap;

    @PostConstruct
    public void start() {
        tcpBootstrap = new ServerBootstrap();
        tcpBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new TcpUserChannelHandler(context));
            }
        });
        udpBootstrap = new HashMap<>();
        NioEventLoopGroup udpGroup = new NioEventLoopGroup();
        if (isStartAll) {
            ListClientInfoRequest request = new ListClientInfoRequest();
            request.setPageNum(1);
            request.setPageSize(20);
            CommonPage<ListClientInfoResponse> responses = null;
            while (true) {
                responses = clientInfoService.pageList(request);
                if (responses.getList().size() == 0) {
                    break;
                }
                // 监听端口
                for (ListClientInfoResponse response : responses.getList()) {
                    if (response.getState() == ClientInfoState.DISABLED) {
                        continue;
                    }
                    // 监听端口
                    List<PortMapping> list = portMappingService.findByUserId(response.getId());
                    // 1. 监听端口
                    for (PortMapping mapping : list) {
                        if (mapping.getState() == PortMappingState.DISABLED) {
                            continue;
                        }
                        // 1. 监听端口
                        if (ProtocolType.isTcp(mapping.getProtocol())) {
                            tcpBootstrap.bind(mapping.getServerHost(), mapping.getServerPort()).addListener((ChannelFutureListener) channelFuture -> {
                                if (channelFuture.isSuccess()) {
                                    log.info("监听端口成功:" + mapping.getServerHost() + "-->" + mapping.getServerPort());
                                } else {
                                    log.error("监听端口失败:" + mapping.getServerHost() + "-->" + mapping.getServerPort());
                                }
                            });
                        } else if (ProtocolType.isUdp(mapping.getProtocol())) {
                            Bootstrap bootstrap = new Bootstrap();
                            bootstrap.group(udpGroup)
                                    .channel(NioDatagramChannel.class)
                                    .option(ChannelOption.SO_BROADCAST, true)
                                    .handler(new ChannelInitializer<DatagramChannel>() {
                                        @Override
                                        protected void initChannel(DatagramChannel ch) throws Exception {
                                            ch.pipeline().addLast(new UdpUserChannelHandler());
                                        }
                                    });

                            // Bind UDP port
                            bootstrap.bind(mapping.getServerHost(), mapping.getServerPort()).addListener((ChannelFutureListener) channelFuture -> {
                                if (channelFuture.isSuccess()) {
                                    log.info("监听端口成功:" + mapping.getServerHost() + "-->" + mapping.getServerPort());
                                    udpBootstrap.put(mapping.getServerHost() + "|" + mapping.getServerPort(), bootstrap);
                                } else {
                                    log.error("监听端口失败:" + mapping.getServerHost() + "-->" + mapping.getServerPort());
                                }
                            });
                        } else {

                        }
                    }
                }
                request.setPageNum(request.getPageNum() + 1);
                request.setPageSize(20);
                responses = null;
            }
        }
    }
}
