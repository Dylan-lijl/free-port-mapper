package pub.carzy.free_port_mapper.server.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import pub.carzy.free_port_mapper.protocol.ProtocolType;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.server.core.PortMappingContext;
import pub.carzy.free_port_mapper.server.core.listeners.entity.ProxyChannelManager;
import pub.carzy.free_port_mapper.server.modules.service.PortMappingService;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author admin
 */
@Slf4j
public class TcpUserChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final AtomicLong userIdProducer = new AtomicLong(0);

    private final PortMappingContext context;

    public TcpUserChannelHandler(PortMappingContext context) {
        this.context = context;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        // 当出现异常就关闭连接
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        // 通知代理客户端
        Channel userChannel = ctx.channel();
        Channel proxyChannel = ProxyChannelManager.getChannel(userChannel);
        if (proxyChannel == null) {
            log.warn("该端口还没有代理客户端:" + userChannel.localAddress());
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String userId = ProxyChannelManager.getUserId(userChannel);
            ProxyEntity proxyEntity = new ProxyEntity();
            proxyEntity.setType(ProxyType.TRANSFER);
            proxyEntity.setUri(userId);
            proxyEntity.setData(bytes);
            proxyChannel.writeAndFlush(proxyEntity);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel userChannel = ctx.channel();
        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
        String key = ProxyChannelManager.findKeyByAddress(ProtocolType.TCP, sa.getHostName(), sa.getPort());
        if (key == null) {
            log.warn("没有对应的提供端提供服务:" + sa.getHostName() + "-->" + sa.getPort());
            ctx.channel().close();
        } else {
            Channel cmdChannel = ProxyChannelManager.findChannelByKey(key);
            if (cmdChannel == null) {
                log.warn("没有连接的提供端提供服务:" + sa.getHostName() + "-->" + sa.getPort());
                ctx.channel().close();
            } else {
                String userId = newUserId();
                String data = context.getPortMappingService().findByKeyAndHostAndPort(key, sa.getHostName(), sa.getPort(), ProtocolType.TCP);
                if (data == null) {
                    log.warn("没有对应的提供端代理信息:" + sa.getHostName() + "-->" + sa.getPort());
                    ctx.channel().close();
                }
                // 用户连接到代理服务器时，设置用户连接不可读，等待代理后端服务器连接成功后再改变为可读状态
                userChannel.config().setOption(ChannelOption.AUTO_READ, false);
                //绑定信息
                ProxyChannelManager.bindUserChannelAndCmdChannel(cmdChannel, userId, userChannel);
                ProxyEntity proxyEntity = new ProxyEntity();
                proxyEntity.setType(ProxyType.CONNECT);
                proxyEntity.setUri(userId);
                proxyEntity.setData(data == null ? null : data.getBytes(StandardCharsets.UTF_8));
                //使用命令连接发送连接消息
                cmdChannel.writeAndFlush(proxyEntity);
            }
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 通知代理客户端
        Channel userChannel = ctx.channel();
        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
        String key = ProxyChannelManager.findKeyByAddress(ProtocolType.TCP, sa.getHostName(), sa.getPort());
        if (key == null) {
            log.warn("(Inactive)没有对应的提供端提供服务:" + sa.getHostName() + "-->" + sa.getPort());
            ctx.channel().close();
        } else {
            Channel cmdChannel = ProxyChannelManager.findChannelByKey(key);
            if (cmdChannel == null) {
                log.warn("(Inactive)没有连接的提供端提供服务:" + sa.getHostName() + "-->" + sa.getPort());
                ctx.channel().close();
            } else {
                // 用户连接断开，从控制连接中移除
                String userId = ProxyChannelManager.getUserId(userChannel);
                ProxyChannelManager.removeChannelFromCmdChannel(cmdChannel, userId);
                Channel proxyChannel = ProxyChannelManager.getChannel(userChannel);
                if (proxyChannel != null && proxyChannel.isActive()) {
                    ProxyChannelManager.clearChannel(proxyChannel);
                    proxyChannel.config().setOption(ChannelOption.AUTO_READ, true);
                    // 通知客户端，用户连接已经断开
                    ProxyEntity proxyEntity = new ProxyEntity();
                    proxyEntity.setType(ProxyType.DISCONNECT);
                    proxyEntity.setUri(userId);
                    proxyChannel.writeAndFlush(proxyEntity);
                }
            }
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        // 通知代理客户端
        Channel userChannel = ctx.channel();
        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
        String key = ProxyChannelManager.findKeyByAddress(ProtocolType.TCP, sa.getHostName(), sa.getPort());
        if (key == null) {
            log.warn("没有对应的提供端提供服务:" + sa.getHostName() + "-->" + sa.getPort());
            ctx.channel().close();
        } else {
            Channel cmdChannel = ProxyChannelManager.findChannelByKey(key);
            if (cmdChannel == null) {
                log.warn("没有连接的提供端提供服务:" + sa.getHostName() + "-->" + sa.getPort());
                ctx.channel().close();
            } else {
                Channel proxyChannel = ProxyChannelManager.getChannel(userChannel);
                if (proxyChannel != null) {
                    proxyChannel.config().setOption(ChannelOption.AUTO_READ, userChannel.isWritable());
                }
            }
        }
        super.channelWritabilityChanged(ctx);
    }

    /**
     * 为用户连接产生ID
     *
     * @return
     */
    private static String newUserId() {
        return String.valueOf(userIdProducer.incrementAndGet());
    }
}
