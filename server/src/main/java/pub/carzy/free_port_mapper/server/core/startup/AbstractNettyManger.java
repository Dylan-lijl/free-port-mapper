package pub.carzy.free_port_mapper.server.core.startup;

import pub.carzy.free_port_mapper.server.config.SettingConfig;
import pub.carzy.free_port_mapper.protocol.listener.ProxyTypePostProcessor;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
public abstract class AbstractNettyManger {
    @Resource
    protected SettingConfig config;

    @Resource
    protected List<ProxyTypePostProcessor> processors;
}
