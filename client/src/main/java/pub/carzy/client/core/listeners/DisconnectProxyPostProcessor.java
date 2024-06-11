package pub.carzy.client.core.listeners;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.client.core.ChannelManager;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;

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
        Channel realServerChannel = ChannelManager.getBindChanel(ctx.channel());
        log.debug("handleDisconnectMessage:" + realServerChannel);
        if (realServerChannel != null) {
            //清除
            ChannelManager.clearChannel(ctx.channel());
            //缓存
            ChannelManager.cacheChannel(ctx.channel());
            //写空块并关闭连接
            realServerChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
