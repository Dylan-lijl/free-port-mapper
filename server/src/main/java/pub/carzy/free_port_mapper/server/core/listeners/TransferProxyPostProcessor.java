package pub.carzy.free_port_mapper.server.core.listeners;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
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
public class TransferProxyPostProcessor extends AbstractProxyTypePostProcessor {

    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.TRANSFER));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        Channel userChannel = ProxyChannelManager.getChannel(ctx.channel());
        if (userChannel != null) {
            ByteBuf buf = ctx.alloc().buffer(entity.getData().length);
            buf.writeBytes(entity.getData());
            userChannel.writeAndFlush(buf);
        }
    }
}
