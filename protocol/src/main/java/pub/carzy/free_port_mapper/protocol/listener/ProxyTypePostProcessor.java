package pub.carzy.free_port_mapper.protocol.listener;

import io.netty.channel.ChannelHandlerContext;
import pub.carzy.free_port_mapper.protocol.ProxyEntity;

import java.util.Set;

/**
 * @author admin
 */
public interface ProxyTypePostProcessor {
    /**
     * 获取支持的类型
     * @return 类型
     */
    Set<Integer> getTypes();

    /**
     * 是否可以处理
     * @return ll
     */
    boolean canProcess(ChannelHandlerContext ctx, ProxyEntity proxyMessage);

    /**
     * 处理
     * @param ctx 管道上下文
     * @param proxyMessage 消息
     */
    default void process(ChannelHandlerContext ctx, ProxyEntity proxyMessage){
        process(ctx,proxyMessage,null);
    }

    void process(ChannelHandlerContext ctx, ProxyEntity proxyMessage,Object other);
}
