package pub.carzy.free_port_mapper.server.config;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class SettingConfig {
    /**
     * 密码
     */
    private String password = "root";
    /**
     * 端口
     */
    private Integer port = 8080;
    /**
     * 登录时长 默认6小时
     */
    private Integer duration = 6 * 60 * 60;

    private PackageConfig packageConfig = new PackageConfig();
}
