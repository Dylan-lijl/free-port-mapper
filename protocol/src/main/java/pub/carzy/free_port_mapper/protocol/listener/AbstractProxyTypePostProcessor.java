package pub.carzy.free_port_mapper.protocol.listener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;

import java.util.Collections;
import java.util.Set;

/**
 * @author admin
 */
public abstract class AbstractProxyTypePostProcessor implements ProxyTypePostProcessor {
    @Override
    public Set<Integer> getTypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean canProcess(ChannelHandlerContext ctx, ProxyEntity proxyMessage) {
        return true;
    }

    @Override
    public void process(ChannelHandlerContext ctx, ProxyEntity proxyMessage,Object other) {
        //可以做公共处理
        doProcess(ctx,proxyMessage,other);
    }

    protected abstract void doProcess(ChannelHandlerContext ctx, ProxyEntity entity,Object other);
    protected ChannelFuture disconnect(Channel cmdChannel, String userId) {
        ProxyEntity proxyEntity = new ProxyEntity();
        proxyEntity.setType(ProxyType.DISCONNECT);
        proxyEntity.setUri(userId);
        return cmdChannel.writeAndFlush(proxyEntity);
    }

}
