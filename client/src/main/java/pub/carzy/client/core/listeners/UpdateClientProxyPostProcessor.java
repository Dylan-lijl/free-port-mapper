package pub.carzy.client.core.listeners;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.carzy.client.core.ChannelManager;
import pub.carzy.client.core.CmdContext;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;
import pub.carzy.free_port_mapper.protocol.ProxyType;
import pub.carzy.free_port_mapper.protocol.entity.ConnectEntity;
import pub.carzy.free_port_mapper.protocol.entity.PortInfo;
import pub.carzy.free_port_mapper.protocol.entity.UpdateClientProxy;
import pub.carzy.free_port_mapper.protocol.listener.AbstractProxyTypePostProcessor;

import java.util.*;

/**
 * 更新客户端代理
 *
 * @author admin
 */
@Component
@Slf4j
public class UpdateClientProxyPostProcessor extends AbstractProxyTypePostProcessor {

    @Override
    public Set<Integer> getTypes() {
        return new LinkedHashSet<>(Collections.singletonList(ProxyType.UPDATE_CLIENT_PROXY));
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, ProxyEntity entity, Object other) {
        CmdContext context = (CmdContext) other;
        //根据对应的信息来替换对应的真实连接
        UpdateClientProxy object = JSONObject.parseObject(new String(entity.getData()), UpdateClientProxy.class);
        ConnectEntity connect = new ConnectEntity();
        connect.setProtocol(object.getOldObj().getProtocol());
        connect.setClientProxy(object.getOldObj().getClientProxy());
        connect.setClientHost(object.getOldObj().getClientHost());
        connect.setClientPort(object.getOldObj().getClientPort());
        boolean equalsProxy = object.getOldObj().getClientProxy().equals(object.getNewObj().getClientProxy());
        List<Channel> list = ChannelManager.findRealServerChannelByConnectEntity(connect);
        if (list.size()>0){
            for (Channel channel : list) {
                String userId = ChannelManager.getUserId(channel);
                //关闭连接
                if (channel.isActive()){
                    channel.close();
                }
                if (!equalsProxy){
                    //代理不同,需要替换对应的bootstrap todo 发送disconnected事件来触发重写构建bootstrap
                    Bootstrap bootstrap = ChannelManager.getBootstrap(channel);
                    if (bootstrap!=null&&bootstrap==context.getRealBootstrapMap().get(userId)){
                        Bootstrap newBootstrap = context.createRealBootstrap(userId);
                    }
                }
            }
        }
    }
}
