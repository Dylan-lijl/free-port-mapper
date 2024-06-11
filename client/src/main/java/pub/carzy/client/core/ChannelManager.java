package pub.carzy.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.util.AttributeKey;
import pub.carzy.free_port_mapper.protocol.entity.ConnectEntity;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 管道管理者
 *
 * @author admin
 */
public class ChannelManager {
    public static final AttributeKey<Channel> CHANNEL = AttributeKey.valueOf("channel");
    public static final AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");
    public static final AttributeKey<Bootstrap> BOOTSTRAP = AttributeKey.valueOf("bootstrap");
    public static final AttributeKey<ConnectEntity> CONNECT_INFO = AttributeKey.valueOf("connectInfo");
    public static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();
    public static final Queue<Channel> proxyChannelQueue = new ConcurrentLinkedQueue<>();
    public static final int MAX_POOL_SIZE = 1024;

    public static void bindChannel(Channel dataChannel, Channel distChannel) {
        dataChannel.attr(CHANNEL).set(distChannel);
        distChannel.attr(CHANNEL).set(dataChannel);
    }

    public static void saveChannel(String userId, Channel realServerChannel) {
        CHANNEL_MAP.put(userId, realServerChannel);
        realServerChannel.attr(USER_ID).set(userId);
    }

    public static Channel getBindChanel(Channel channel) {
        return channel.attr(CHANNEL).get();
    }

    public static String getUserId(Channel channel) {
        return channel.attr(USER_ID).get();
    }

    public static void clearChannel(Channel channel) {
        channel.attr(CHANNEL).set(null);
    }

    public static String clearUserId(Channel channel) {
        return channel.attr(USER_ID).getAndSet(null);
    }

    public static void cacheChannel(Channel channel) {
        if (proxyChannelQueue.size() > MAX_POOL_SIZE) {
            channel.close();
        } else {
            channel.config().setOption(ChannelOption.AUTO_READ, true);
            proxyChannelQueue.offer(channel);
        }
    }

    public static void clearRealServerChannels() {
        Iterator<Map.Entry<String, Channel>> ite = CHANNEL_MAP.entrySet().iterator();
        while (ite.hasNext()) {
            Channel realServerChannel = ite.next().getValue();
            if (realServerChannel.isActive()) {
                realServerChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
        CHANNEL_MAP.clear();
    }

    public static void saveInfoChannel(Bootstrap realBootstrap, ConnectEntity connectEntity, Channel realServerChannel) {
        realServerChannel.attr(BOOTSTRAP).set(realBootstrap);
        realServerChannel.attr(CONNECT_INFO).set(connectEntity);
    }

    public static List<Channel> findRealServerChannelByConnectEntity(ConnectEntity connect) {
        List<Channel> channels = new ArrayList<>();
        CHANNEL_MAP.forEach((k,v)->{
            if (v.attr(CONNECT_INFO).get().equals(connect)){
                channels.add(v);
            }
        });
        return channels;
    }

    public static Bootstrap getBootstrap(Channel channel) {
        return channel.attr(BOOTSTRAP).get();
    }
}
