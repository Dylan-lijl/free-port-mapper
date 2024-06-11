package pub.carzy.free_port_mapper.server.core.listeners.entity;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import pub.carzy.free_port_mapper.protocol.ProtocolType;
import pub.carzy.free_port_mapper.protocol.entity.PortInfo;
import pub.carzy.free_port_mapper.server.modules.dto.inner.PortMappingByProcessor;
import pub.carzy.free_port_mapper.server.modules.service.impl.SystemInfoServiceImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
@Slf4j
public class ProxyChannelManager {
    public static final String ALL_NETWORK = "0.0.0.0";
    /**
     * 提供者密钥对应的管道(一一对应),{key:client_key,value:channel}
     */
    public static final Map<String, Channel> CMD_CHANNEL_MAP = new ConcurrentHashMap<>();
    public static final Map<PortInfo, String> CLIENT_MAPPING_TO_KEY = new ConcurrentHashMap<>();
    private static final AttributeKey<String> CLIENT_KEY = AttributeKey.newInstance("client_key");
    private static final AttributeKey<String> USER_ID = AttributeKey.newInstance("userId");
    private static final AttributeKey<Map<String, Channel>> IDS = AttributeKey.newInstance("ids");
    public static final AttributeKey<Channel> CHANNEL = AttributeKey.newInstance("channel");
    public static final AttributeKey<Set<PortInfo>> TCP_PORT_INFO = AttributeKey.newInstance("port_info");

    public static Channel findChannelByKey(String key) {
        return CMD_CHANNEL_MAP.get(key);
    }

    public static String findKeyByAddress(int protocol, String host, Integer port) {
        PortInfo info = new PortInfo(protocol, host, port);
        return CLIENT_MAPPING_TO_KEY.get(info);
    }

    public static Channel putChannel(String key, Channel channel) {
        return CMD_CHANNEL_MAP.put(key, channel);
    }

    public static void bindChannelAndPort(List<PortMappingByProcessor> mappings, String clientKey, Channel channel) {
        Set<PortInfo> infos = new LinkedHashSet<>();
        synchronized (CLIENT_MAPPING_TO_KEY) {
            for (PortMappingByProcessor processor : mappings) {
                PortInfo portInfo = new PortInfo(processor.getProtocol(), processor.getServerHost(), processor.getServerPort());
                infos.add(portInfo);
                if (ALL_NETWORK.equals(processor.getServerHost().trim())) {
                    SystemInfoServiceImpl.list.forEach(item -> {
                        if (item.getIp() != null) {
                            infos.add(new PortInfo(processor.getProtocol(), item.getIp(), processor.getServerPort()));
                        }
                    });
                }
            }
            infos.forEach(item -> CLIENT_MAPPING_TO_KEY.put(item, clientKey));
        }
        channel.attr(CLIENT_KEY).set(clientKey);
        channel.attr(TCP_PORT_INFO).set(infos);
        channel.attr(IDS).set(new ConcurrentHashMap<>());
        //绑定信息 todo
    }

    public static void bindUserChannelAndCmdChannel(Channel cmdChannel, String userId, Channel userChannel) {
        userChannel.attr(USER_ID).set(userId);
        // userChannel.attr()
        cmdChannel.attr(IDS).get().put(userId, userChannel);
    }

    public static Channel getCmdChannel(String clientKey) {
        return CMD_CHANNEL_MAP.get(clientKey);
    }

    public static Channel getUserChannel(Channel cmdChannel, String userId) {
        return cmdChannel.attr(IDS).get().get(userId);
    }

    public static void bindBindTypeChannel(Channel channel, String userId, String clientKey, Channel userChannel) {
        channel.attr(USER_ID).set(userId);
        channel.attr(CLIENT_KEY).set(clientKey);
        channel.attr(CHANNEL).set(userChannel);
        userChannel.attr(CHANNEL).set(channel);
    }

    public static Channel getChannel(Channel channel) {
        return channel.attr(CHANNEL).get();
    }

    public static String getUserId(Channel channel) {
        return channel.attr(USER_ID).get();
    }

    public static Channel removeChannelFromCmdChannel(Channel cmdChannel, String userId) {
        if (cmdChannel.attr(IDS).get() == null) {
            return null;
        }
        return cmdChannel.attr(IDS).get().remove(userId);
    }

    public static void clearChannel(Channel channel) {
        channel.attr(CHANNEL).set(null);
        channel.attr(CLIENT_KEY).set(null);
        channel.attr(USER_ID).set(null);
    }

    public static String getClientKey(Channel channel) {
        return channel.attr(CLIENT_KEY).get();
    }

    public static void removeCmdChannel(Channel channel) {
        log.warn("channel closed, clear user channels, " + channel);
        Set<PortInfo> infos = channel.attr(TCP_PORT_INFO).get();
        if (infos == null) {
            return;
        }

        String clientKey = getClientKey(channel);
        Channel channel0 = CMD_CHANNEL_MAP.remove(clientKey);
        //不是同一个连接就替换
        if (channel != channel0) {
            CMD_CHANNEL_MAP.put(clientKey, channel);
        } else {
            synchronized (CMD_CHANNEL_MAP) {
                for (PortInfo info : infos) {
                    CLIENT_MAPPING_TO_KEY.remove(info);
                    if (ALL_NETWORK.equals(info.getHost())) {
                        SystemInfoServiceImpl.list.forEach(item -> {
                            CLIENT_MAPPING_TO_KEY.remove(new PortInfo(info.getProtocol(), item.getIp(), info.getPort()));
                        });
                    }
                }
            }
        }

        if (channel.isActive()) {
            log.info("disconnect proxy channel {}", channel);
            channel.close();
        }

        Map<String, Channel> userChannels = channel.attr(IDS).get();
        if (userChannels != null && !userChannels.isEmpty()) {
            Iterator<String> ite = userChannels.keySet().iterator();
            while (ite.hasNext()) {
                Channel userChannel = userChannels.get(ite.next());
                if (userChannel.isActive()) {
                    userChannel.close();
                    log.info("disconnect user channel {}", userChannel);
                }
            }
        }
    }

    public static void closeChannel(PortInfo portInfo) {
        Set<String> keys = findKeysByInfo(portInfo);
        //将对应的端口的channel关闭
        if (!keys.isEmpty()) {
            synchronized (CMD_CHANNEL_MAP) {
                keys.forEach(key -> {
                    Channel remove = CMD_CHANNEL_MAP.remove(key);
                    //这里得通知客户端需要关闭 todo
                    if (remove != null) {
                        if (remove.isActive()) {
                            log.info("disconnect proxy channel {}", remove);
                            remove.close();
                        }
                        remove.attr(IDS).get().forEach((userId, channel) -> {
                            if (channel.isActive()) {
                                log.info("disconnect user channel {}", channel);
                                channel.close();
                            }
                        });
                    }
                });
            }
        }
    }

    public static Set<String> findKeysByInfo(PortInfo portInfo) {
        Set<String> keys = new LinkedHashSet<>();
        //删除端口信息绑定的key
        synchronized (CLIENT_MAPPING_TO_KEY) {
            keys.add(CLIENT_MAPPING_TO_KEY.remove(portInfo));
            if (ALL_NETWORK.equals(portInfo.getHost())) {
                CLIENT_MAPPING_TO_KEY.keySet().forEach(item -> {
                    if (item.getProtocol().equals(portInfo.getProtocol()) && item.getPort().equals(portInfo.getPort())) {
                        keys.add(CLIENT_MAPPING_TO_KEY.remove(item));
                    }
                });
            }
        }
        return keys;
    }
}
