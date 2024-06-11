package pub.carzy.free_port_mapper.server.core.listeners;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;
import pub.carzy.free_port_mapper.server.core.listeners.entity.ProxyChannelManager;

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
public class DisconnectProxyPostProcessor extends AbstractProxyTypePostProcessor {

    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.DISCONNECT));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        String clientKey = ProxyChannelManager.getClientKey(ctx.channel());
        // 代理连接没有连上服务器由控制连接发送用户端断开连接消息
        if (clientKey == null) {
            String userId = entity.getUri();
            Channel userChannel = ProxyChannelManager.removeChannelFromCmdChannel(ctx.channel(), userId);
            if (userChannel != null) {
                // 数据发送完成后再关闭连接，解决http1.0数据传输问题
                userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }

        Channel cmdChannel = ProxyChannelManager.getCmdChannel(clientKey);
        if (cmdChannel == null) {
            log.warn("ConnectMessage:error cmd channel key {}", clientKey);
            return;
        }

        Channel userChannel = ProxyChannelManager.removeChannelFromCmdChannel(ctx.channel(), ProxyChannelManager.getUserId(ctx.channel()));
        if (userChannel != null) {
            // 数据发送完成后再关闭连接，解决http1.0数据传输问题
            userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            ProxyChannelManager.clearChannel(ctx.channel());
        }
    }
}
