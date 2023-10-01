package pub.carzy.free_port_mapper.server.config.export_config;

import org.springframework.context.annotation.Configuration;
import pub.carzy.export_file.WebExportFileEnable;

/**
 * 在配置类注册自己的处理器,默认处理器只有集合类型
 * 配置类上加上注解
 * @author admin
 */
@Configuration
@WebExportFileEnable
public class ApplicationBean {

}
