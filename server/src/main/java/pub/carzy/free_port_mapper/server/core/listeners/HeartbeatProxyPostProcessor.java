package pub.carzy.free_port_mapper.server.core.listeners;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;

import java.util.*;

/**
 * 心跳代理
 *
 * @author admin
 */
@Component
@Slf4j
public class HeartbeatProxyPostProcessor extends AbstractProxyTypePostProcessor {
    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.HEARTBEAT));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        ProxyEntity heartbeatMessage = new ProxyEntity();
        heartbeatMessage.setSerialNumber(heartbeatMessage.getSerialNumber());
        heartbeatMessage.setType(ProxyType.HEARTBEAT);
        log.debug("response heartbeat message {}", ctx.channel());
        ctx.channel().writeAndFlush(heartbeatMessage);
    }
}
