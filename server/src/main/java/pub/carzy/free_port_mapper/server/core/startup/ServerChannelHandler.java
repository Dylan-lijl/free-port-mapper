package pub.carzy.free_port_mapper.server.core.startup;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.listener.ProxyTypePostProcessor;
import pub.carzy.free_port_mapper.server.core.listeners.entity.ProxyChannelManager;

import java.util.List;

/**
 * @author admin
 */
@Slf4j
public class ServerChannelHandler extends SimpleChannelInboundHandler<ProxyEntity> {

    private List<ProxyTypePostProcessor> processors;

    protected ServerChannelHandler(List<ProxyTypePostProcessor> processors) {
        super();
        this.processors = processors;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyEntity proxyMessage) throws Exception {
        log.debug("ProxyEntity received {}", proxyMessage.getType());
        boolean processed = false;
        for (ProxyTypePostProcessor processor : processors) {
            if (processor.getTypes() == null || !processor.getTypes().contains(proxyMessage.getType())) {
                continue;
            }
            if (processor.canProcess(ctx, proxyMessage)) {
                processor.process(ctx, proxyMessage);
                processed = true;
                break;
            }
        }
        log.debug(processed ? "消息被处理" : "消息未被处理");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel userChannel = ProxyChannelManager.getChannel(ctx.channel());
        if (userChannel != null) {
            userChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Channel userChannel = ProxyChannelManager.getChannel(channel);
        //关闭以前的已连接或失活的管道
        if (userChannel != null && userChannel.isActive()) {
            String clientKey = ProxyChannelManager.getClientKey(channel);
            String userId = ProxyChannelManager.getUserId(channel);
            Channel cmdChannel = ProxyChannelManager.getCmdChannel(clientKey);
            if (cmdChannel != null) {
                //移除对应的绑定关系
                ProxyChannelManager.removeChannelFromCmdChannel(cmdChannel, userId);
            } else {
                log.warn("null cmdChannel, clientKey is {}", clientKey);
            }
            // 数据发送完成后再关闭连接，解决http1.0数据传输问题
            userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            userChannel.close();
        } else {
            ProxyChannelManager.removeCmdChannel(channel);
        }

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }
}
