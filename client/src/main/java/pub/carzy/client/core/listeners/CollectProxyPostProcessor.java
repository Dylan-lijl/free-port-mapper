package pub.carzy.client.core.listeners;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.proxy.Socks5ProxyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.client.core.*;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.entity.ConnectEntity;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 连接
 *
 * @author admin
 */
@Component
@Slf4j
public class CollectProxyPostProcessor extends AbstractProxyTypePostProcessor {

    @Resource
    private CmdTcpProxy proxy;

    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.CONNECT));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        CmdContext context = (CmdContext) other;
        Channel cmdChannel = ctx.channel();
        String userId = entity.getUri();
        ConnectEntity connectEntity = JSONObject.parseObject(new String(entity.getData()), ConnectEntity.class);
        //todo 连接后端服务器
        Bootstrap realBootstrap = context.createRealBootstrap(userId);
        realBootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //只有代理路径正确且能正常解析时才生效
                if (StrUtil.isNotBlank(connectEntity.getClientProxy())) {
                    //
                    String[] split = connectEntity.getClientProxy().split(":");
                    if (split.length == 2) {
                        //解析成所需要的网络地址
                        try {
                            pipeline.addLast(new Socks5ProxyHandler(new InetSocketAddress(split[0].trim(), Integer.parseInt(split[1].trim()))));
                        } catch (Exception e) {
                            log.warn(connectEntity.getClientProxy() + "解析代理路径出现异常:" + e.getMessage());
                        }
                    }
                }
                pipeline.addLast(new RealServerChannelHandler(context));
            }
        });
        realBootstrap.connect(connectEntity.getClientHost(), connectEntity.getClientPort()).addListener((ChannelFutureListener) future -> {

            // 连接后端服务器成功
            if (future.isSuccess()) {
                final Channel realServerChannel = future.channel();
                log.debug("连接成功:" + connectEntity);
                realServerChannel.config().setOption(ChannelOption.AUTO_READ, false);
                //建立tcp连接用于传递数据
                context.connectTcp().addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        //绑定channel
                        ChannelManager.bindChannel(channelFuture.channel(), realServerChannel);
                        //发送连接消息
                        ProxyEntity proxyEntity = new ProxyEntity();
                        proxyEntity.setType(ProxyType.BIND);
                        proxyEntity.setUri(userId);
                        proxyEntity.setData(context.getProxyServerConfig().getSecretKey().getBytes(StandardCharsets.UTF_8));
                        //使用建立好的连接发送出去
                        channelFuture.channel().writeAndFlush(proxyEntity).addListener((ChannelFutureListener) writeFuture -> {
                            if (writeFuture.isSuccess()) {
                                realServerChannel.config().setOption(ChannelOption.AUTO_READ, true);
                                ChannelManager.saveChannel(userId, realServerChannel);
                                ChannelManager.saveInfoChannel(realBootstrap,connectEntity, realServerChannel);
                            } else {
                                disconnect(cmdChannel, userId);
                            }
                        });
                    } else {

                    }
                });
            } else {
                disconnect(cmdChannel, userId).addListener((ChannelFutureListener) channelFuture -> {
                    log.warn("连接失败:" + connectEntity);
                });
            }
        });
    }

}
