package pub.carzy.free_port_mapper.server.core.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;
import pub.carzy.free_port_mapper.server.core.listeners.entity.ProxyChannelManager;
import pub.carzy.free_port_mapper.server.modules.dto.inner.ClientDetailByProcessor;
import pub.carzy.free_port_mapper.server.modules.service.ClientInfoService;

import javax.annotation.Resource;
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
public class AuthProxyPostProcessor extends AbstractProxyTypePostProcessor {

    @Resource
    private ClientInfoService clientInfoService;

    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.AUTH));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        String clientKey = new String(entity.getData());
        //根据密钥查找对应的数据
        ClientDetailByProcessor detail = clientInfoService.findByProcessor(clientKey);
        if (detail == null || detail.getMappings() == null || detail.getMappings().isEmpty()) {
            log.info("error clientKey {}, {}", clientKey, ctx.channel());
            ctx.channel().close();
            return;
        }
        //
        Channel oldChannel = ProxyChannelManager.putChannel(clientKey, ctx.channel());
        //如果老的管道存在则需要清理数据
        if (oldChannel != null) {
            log.debug("关闭旧的连接:" + clientKey);
            oldChannel.close();
        }
        log.debug("绑定密钥和端口关系");
        ProxyChannelManager.bindChannelAndPort(detail.getMappings(), clientKey, ctx.channel());
    }
}
