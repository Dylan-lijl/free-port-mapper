package pub.carzy.free_port_mapper.server.core.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;
import pub.carzy.free_port_mapper.server.core.listeners.entity.ProxyChannelManager;
import pub.carzy.free_port_mapper.server.modules.service.ClientInfoService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 连接
 *
 * @author admin
 */
@Component
@Slf4j
public class BindProxyPostProcessor extends AbstractProxyTypePostProcessor {

    @Resource
    private ClientInfoService clientInfoService;

    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.BIND));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        String userId = entity.getUri();
        String clientKey = new String(entity.getData());
        Channel cmdChannel = ProxyChannelManager.getCmdChannel(clientKey);
        if (cmdChannel == null) {
            ctx.channel().close();
            log.warn("找不到对应的命令管道:" + clientKey);
            return;
        }
        Channel userChannel = ProxyChannelManager.getUserChannel(cmdChannel, userId);
        if (userChannel != null) {
            ProxyChannelManager.bindBindTypeChannel(ctx.channel(),userId, clientKey, userChannel);
            // 代理客户端与后端服务器连接成功，修改用户连接为可读状态
            userChannel.config().setOption(ChannelOption.AUTO_READ, true);
        }
    }
}
