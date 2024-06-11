package pub.carzy.client.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import sun.security.pkcs11.wrapper.Constants;

/**
 * @author admin
 */
@Slf4j
public class RealServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private CmdContext cmdContext;
    public RealServerChannelHandler(CmdContext cmdContext) {
        this.cmdContext = cmdContext;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        Channel realServerChannel = ctx.channel();
        Channel channel = ChannelManager.getBindChanel(realServerChannel);
        if (channel == null) {
            // 代理客户端连接断开
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String userId = ChannelManager.getUserId(realServerChannel);
            ProxyEntity proxyEntity = new ProxyEntity();
            proxyEntity.setType(ProxyType.TRANSFER);
            proxyEntity.setUri(userId);
            proxyEntity.setData(bytes);
            channel.writeAndFlush(proxyEntity);
            log.debug("write data to proxy server, {}, {}", realServerChannel, channel);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel();
        String userId = ChannelManager.clearUserId(realServerChannel);
        Channel channel = ChannelManager.getBindChanel(realServerChannel);
        if (channel != null) {
            log.debug("channelInactive, {}", realServerChannel);
            ProxyEntity proxyEntity = new ProxyEntity();
            proxyEntity.setType(ProxyType.DISCONNECT);
            proxyEntity.setUri(userId);
            channel.writeAndFlush(proxyEntity);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel();
        Channel proxyChannel = ChannelManager.getBindChanel(realServerChannel);
        if (proxyChannel != null) {
            proxyChannel.config().setOption(ChannelOption.AUTO_READ, realServerChannel.isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }
}
