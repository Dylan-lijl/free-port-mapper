package pub.carzy.free_port_mapper.server.core;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.entity.PortInfo;
import pub.carzy.free_port_mapper.protocol.entity.UpdateClientProxy;
import pub.carzy.free_port_mapper.server.core.listeners.entity.ProxyChannelManager;
import pub.carzy.free_port_mapper.server.modules.event.UpdatePortMappingEvent;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;
import pub.carzy.free_port_mapper.server.modules.service.PortMappingService;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @author admin
 */
@Component
@Data
public class PortMappingContext {
    @Resource
    private PortMappingService portMappingService;

    @EventListener(classes = UpdatePortMappingEvent.class)
    public void updateDataListener(UpdatePortMappingEvent event) {
        PortMapping old = event.getOld();
        PortMapping newObj = event.getNewObj();
        //状态不一致需要关闭对应的代理
        if (old.getState().compareTo(newObj.getState()) != 0) {
            ProxyChannelManager.closeChannel(new PortInfo(old.getProtocol(), old.getServerHost(), old.getServerPort()));
        }
        //更新客户端信息
        if (!old.getClientHost().equals(newObj.getClientHost())
                || old.getClientPort().compareTo(newObj.getClientPort()) != 0
                || !old.getClientProxy().equals(newObj.getClientProxy())) {
            Set<String> keys = ProxyChannelManager.findKeysByInfo(new PortInfo(newObj.getProtocol(), newObj.getServerHost(), newObj.getServerPort()));
            for (String key : keys) {
                Channel channel = ProxyChannelManager.getCmdChannel(key);
                if (channel != null) {
                    //生成消息并传递
                    ProxyEntity proxy = new ProxyEntity();
                    proxy.setType(ProxyType.UPDATE_CLIENT_PROXY);
                    UpdateClientProxy updateClientProxy = new UpdateClientProxy();
                    updateClientProxy.setOldObj(new PortInfo(old.getProtocol(), old.getServerHost(), old.getServerPort()));
                    updateClientProxy.setNewObj(new PortInfo(newObj.getProtocol(), newObj.getServerHost(), newObj.getServerPort()));
                    proxy.setData(JSONObject.toJSONString(updateClientProxy).getBytes(StandardCharsets.UTF_8));
                    channel.writeAndFlush(proxy);
                }
            }
        }
        //更新服务端的绑定端口以及主机名
        if (old.getProtocol().compareTo(newObj.getProtocol()) != 0
                || !old.getServerHost().equals(newObj.getServerHost())
                || !old.getServerPort().equals(newObj.getServerPort())) {

        }
    }
}
