package pub.carzy.client.core;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.listener.ProxyTypePostProcessor;

/**
 * @author fengfei
 */
@Slf4j
public class ClientChannelHandler extends SimpleChannelInboundHandler<ProxyEntity> {

    private CmdContext cmdContext;

    public ClientChannelHandler(CmdContext cmdContext) {
        this.cmdContext = cmdContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyEntity proxyEntity) throws Exception {
        log.debug("recieved proxy message, type is {}", proxyEntity.getType());
        boolean processed = false;
        for (ProxyTypePostProcessor processor : cmdContext.getProcessors()) {
            if (processor.getTypes() == null || !processor.getTypes().contains(proxyEntity.getType())) {
                continue;
            }
            if (processor.canProcess(ctx, proxyEntity)) {
                processor.process(ctx, proxyEntity,cmdContext);
                processed = true;
                break;
            }
        }
        log.debug(processed ? "消息被处理" : "消息未被处理");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ChannelManager.getBindChanel(ctx.channel());
        if (realServerChannel != null) {
            realServerChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 控制连接
        if (cmdContext.getCmdChannel() == ctx.channel()) {
            cmdContext.setCmdChannel(null);
            ChannelManager.clearRealServerChannels();
            // channelStatusListener.channelInactive(ctx);移除代理连接
        } else {
            // 数据传输连接
            Channel realServerChannel = ChannelManager.getBindChanel(ctx.channel());
            if (realServerChannel != null && realServerChannel.isActive()) {
                realServerChannel.close();
            }
        }
        // ClientChannelMannager.removeProxyChanel(ctx.channel());移除代理连接
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

}
